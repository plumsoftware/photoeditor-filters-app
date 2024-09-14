package ru.plumsoftware.photoeditor.activities.savedimages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.FileProvider
import com.yandex.mobile.ads.common.*
import com.yandex.mobile.ads.interstitial.InterstitialAd
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.plumsoftware.photoeditor.activities.editimage.EditImageActivity
import ru.plumsoftware.photoeditor.activities.filteredimage.FilteredImageActivity
import ru.plumsoftware.photoeditor.adapters.SavedImagesAdapter
import ru.plumsoftware.photoeditor.databinding.ActivitySavedImagesBinding
import ru.plumsoftware.photoeditor.listeners.SavedImagesListener
import ru.plumsoftware.photoeditor.utilities.displayToast
import ru.plumsoftware.photoeditor.viewmodels.SavedImagesViewModel
import java.io.File

class SavedImagesActivity : AppCompatActivity(), SavedImagesListener {

    private lateinit var binding: ActivitySavedImagesBinding
    private val viewModel: SavedImagesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupObservers()
        setListeners()
        viewModel.loadSavedImages()
    }

    private fun setupObservers() {
        viewModel.savedImagesUiState.observe(this) {
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
        }
    }

    private fun setListeners() {
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }
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