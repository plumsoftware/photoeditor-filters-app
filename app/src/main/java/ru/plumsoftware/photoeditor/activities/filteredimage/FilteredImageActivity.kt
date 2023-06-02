package ru.plumsoftware.photoeditor.activities.filteredimage

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.annotation.RequiresApi
import com.yandex.mobile.ads.common.*
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import ru.plumsoftware.photoeditor.R
import ru.plumsoftware.photoeditor.activities.editimage.EditImageActivity
import ru.plumsoftware.photoeditor.databinding.ActivityFilteredImageBinding
import ru.plumsoftware.photoeditor.dialogs.ProgressDialog
import ru.plumsoftware.photoeditor.utilities.displayToast

class FilteredImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilteredImageBinding
    private lateinit var fileUri: Uri

    //    Ads
    private var interstitialAd: InterstitialAd? = null
    private var adRequest: AdRequest? = null

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilteredImageBinding.inflate(layoutInflater)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        setContentView(binding.root)
        displayFilteredImage()
        setListeners()
        initAds(this)
    }

    private fun displayFilteredImage() {
        intent.getParcelableExtra<Uri>(EditImageActivity.KEY_FILTERED_IMAGE_URI)?.let { imageUri ->
            fileUri = imageUri
            binding.imageFilteredImage.setImageURI(fileUri)
        }
    }

    private fun setListeners() {
        binding.fabShare.setOnClickListener {
            with(Intent(Intent.ACTION_SEND)) {
                putExtra(Intent.EXTRA_STREAM, fileUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                type = "image/*"
                startActivity(this)
            }
        }
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
        showAds(this)
    }
}