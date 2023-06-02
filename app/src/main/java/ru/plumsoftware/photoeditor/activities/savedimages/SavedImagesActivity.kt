package ru.plumsoftware.photoeditor.activities.savedimages

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.FileProvider
import com.yandex.mobile.ads.common.*
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.plumsoftware.photoeditor.activities.editimage.EditImageActivity
import ru.plumsoftware.photoeditor.activities.filteredimage.FilteredImageActivity
import ru.plumsoftware.photoeditor.adapters.SavedImagesAdapter
import ru.plumsoftware.photoeditor.databinding.ActivitySavedImagesBinding
import ru.plumsoftware.photoeditor.dialogs.ProgressDialog
import ru.plumsoftware.photoeditor.listeners.SavedImagesListener
import ru.plumsoftware.photoeditor.utilities.displayToast
import ru.plumsoftware.photoeditor.viewmodels.SavedImagesViewModel
import java.io.File

class SavedImagesActivity : AppCompatActivity(), SavedImagesListener {

    private lateinit var binding: ActivitySavedImagesBinding
    private val viewModel: SavedImagesViewModel by viewModel()

    //    Ads
    private var interstitialAd: InterstitialAd? = null
    private var adRequest: AdRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupObservers()
//        initAds(this)
        setListeners()
        viewModel.loadSavedImages()
    }

    private fun setupObservers() {
        viewModel.savedImagesUiState.observe(this, {
            val savedImagesDataState = it ?: return@observe
            binding.savedImagesProgressBar.visibility =
                if (savedImagesDataState.isLoading) View.VISIBLE else View.GONE
            savedImagesDataState.savedImages?.let { savedImages ->
                displayToast("${savedImages.size} изображений загружено")
                SavedImagesAdapter(savedImages, this).let { adapter ->
                    with(binding.savedImagesRecyclerView) {
                        this.adapter = adapter
                        visibility = View.VISIBLE
                    }
                }
            } ?: kotlin.run {
                savedImagesDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        })
    }

    private fun setListeners() {
        binding.imageBack.setOnClickListener {
            onBackPressed()
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
        super.onBackPressed()
//        showAds(this)
    }

    override fun onImageClicked(file: File) {
        val fileUri =
            FileProvider.getUriForFile(applicationContext, "${packageName}.provider", file)
        Intent(
            applicationContext,
            FilteredImageActivity::class.java
        ).also {
            it.putExtra(EditImageActivity.KEY_FILTERED_IMAGE_URI, fileUri)
            startActivity(it)
        }
    }
}