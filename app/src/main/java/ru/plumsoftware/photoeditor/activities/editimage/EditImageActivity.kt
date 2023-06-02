package ru.plumsoftware.photoeditor.activities.editimage

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.yandex.mobile.ads.common.*
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import jp.co.cyberagent.android.gpuimage.GPUImage
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.plumsoftware.photoeditor.activities.filteredimage.FilteredImageActivity
import ru.plumsoftware.photoeditor.activities.main.MainActivity
import ru.plumsoftware.photoeditor.adapters.ImageFiltersAdapter
import ru.plumsoftware.photoeditor.data.ImageFilter
import ru.plumsoftware.photoeditor.databinding.ActivityEditBinding
import ru.plumsoftware.photoeditor.dialogs.ProgressDialog
import ru.plumsoftware.photoeditor.listeners.ImageFilterListener
import ru.plumsoftware.photoeditor.utilities.displayToast
import ru.plumsoftware.photoeditor.utilities.show
import ru.plumsoftware.photoeditor.viewmodels.EditImageViewModel

class EditImageActivity : AppCompatActivity(), ImageFilterListener {

    companion object {
        const val KEY_FILTERED_IMAGE_URI = "filteredImageUri"
    }

    private lateinit var binding: ActivityEditBinding
    private val viewModel: EditImageViewModel by viewModel()
    private lateinit var gpuImage: GPUImage

    //    Ads
    private var interstitialAd: InterstitialAd? = null
    private var adRequest: AdRequest? = null

    //    Image bitmaps
    private lateinit var originalBitmap: Bitmap
    private val filteredBitmap = MutableLiveData<Bitmap>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
//        displayImagePreview()
        setupObservers()
        prepareImagePreview()
        initAds(this)
    }

    private fun setupObservers() {
        viewModel.imagePreviewUiState.observe(this, {
            val dataState = it ?: return@observe
            binding.previewProgressBar.visibility =
                if (dataState.isLoading) View.VISIBLE else View.GONE
            dataState.bitmap?.let { bitmap ->
                originalBitmap = bitmap
                filteredBitmap.value = bitmap

                with(originalBitmap) {
                    gpuImage.setImage(this)
                    binding.imagePreview.setImageBitmap(this)
                    binding.imagePreview.show()
                }
                viewModel.loadImageFilters(bitmap)
            } ?: kotlin.run {
                dataState.error?.let { error ->
                    displayToast(error)
                }
            }
        })

        viewModel.imageFiltersUiState.observe(this, {
            val imageFiltersDataState = it ?: return@observe
            binding.imageFiltersProgressBar.visibility =
                if (imageFiltersDataState.isLoading) View.VISIBLE else View.GONE
            imageFiltersDataState.imageFilters?.let { imageFilters ->
                ImageFiltersAdapter(imageFilters, this).also { adapter ->
                    binding.filtersRecyclerView.adapter = adapter
                }
            } ?: kotlin.run {
                imageFiltersDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        })

        filteredBitmap.observe(this, { bitmap ->
            binding.imagePreview.setImageBitmap(bitmap)
        })

        viewModel.saveFilteredImageUiState.observe(this, {
            val saveFilteredImageDataState = it ?: return@observe
            if (saveFilteredImageDataState.isLoading) {
                binding.imageSave.visibility = View.GONE
                binding.savingProgressBar.visibility = View.VISIBLE
            } else {
                binding.savingProgressBar.visibility = View.GONE
                binding.imageSave.visibility = View.VISIBLE
            }
            saveFilteredImageDataState.uri?.let { savedImageUri ->
//                Toast.makeText(this, savedImageUri.toString(), Toast.LENGTH_LONG).show()
                Intent(
                    applicationContext,
                    FilteredImageActivity::class.java
                ).also { filteredImageIntent ->
                    filteredImageIntent.putExtra(KEY_FILTERED_IMAGE_URI, savedImageUri)
                    startActivity(filteredImageIntent)
                    overridePendingTransition(0, 0)
                    finish()
                }
            } ?: kotlin.run {
                saveFilteredImageDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        })
    }

    private fun prepareImagePreview() {
        gpuImage = GPUImage(applicationContext)
        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let { imageUri ->
            viewModel.prepareImagePreview(imageUri)
        }
    }

    @SuppressLint("Recycle")
    private fun displayImagePreview() {
        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let { imageUri ->
            val inputStream = contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            binding.imagePreview.setImageBitmap(bitmap)
            binding.imagePreview.visibility = View.VISIBLE
        }
    }

    private fun setListeners() {
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }
        binding.imageSave.setOnClickListener {
            filteredBitmap.value?.let { bitmap ->
                viewModel.saveFilteredImage(bitmap)
            }
        }
        binding.imagePreview.setOnLongClickListener {
            binding.imagePreview.setImageBitmap(originalBitmap)
            return@setOnLongClickListener false
        }
        binding.imagePreview.setOnClickListener {
            binding.imagePreview.setImageBitmap(filteredBitmap.value)
        }
    }

    private fun showAds(context: Context) {
        val progressDialog = ProgressDialog(context)
        progressDialog.showDialog()
        interstitialAd?.setInterstitialAdEventListener(object : InterstitialAdEventListener {
            override fun onAdLoaded() {
                progressDialog.dismissDialog()
                interstitialAd?.show()
            }

            override fun onAdFailedToLoad(error: AdRequestError) {
                progressDialog.dismissDialog()
                displayToast(error.description.toString())
                finish()
                overridePendingTransition(0, 0)
            }

            override fun onAdShown() {

            }

            override fun onAdDismissed() {
                progressDialog.dismissDialog()
                finish()
                overridePendingTransition(0, 0)
            }

            override fun onAdClicked() {

            }

            override fun onLeftApplication() {

            }

            override fun onReturnedToApplication() {

            }

            override fun onImpression(data: ImpressionData?) {

            }
        })
        interstitialAd?.loadAd(adRequest!!)
    }

    override fun onFilterSelected(imageFilter: ImageFilter) {
        with(imageFilter) {
            with(gpuImage) {
                setFilter(filter)
                filteredBitmap.value = bitmapWithFilterApplied
            }
        }
    }

    private fun initAds(context: Context) {
        MobileAds.initialize(context, object : InitializationListener {
            override fun onInitializationCompleted() {
                interstitialAd = InterstitialAd(context)
                interstitialAd?.setAdUnitId("R-M-2416472-1")
                adRequest = AdRequest.Builder().build()
            }

        })
    }

    override fun onBackPressed() {
        showAds(this)
    }
}