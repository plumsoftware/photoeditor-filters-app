package ru.plumsoftware.photoeditor.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.plumsoftware.photoeditor.repositories.SavedImageRepository
import ru.plumsoftware.photoeditor.utilities.Coroutines
import java.io.File

class SavedImagesViewModel(private val savedImagesRepository: SavedImageRepository) : ViewModel() {

    private val savedImagesDataState = MutableLiveData<SavedImagesDataState>()
    val savedImagesUiState: LiveData<SavedImagesDataState> get() = savedImagesDataState

    fun loadSavedImages() {
        Coroutines.io {
            runCatching {
                emitSavedImagesUiState(isLoading = true)
                savedImagesRepository.loadSavedImages()
            }.onSuccess { savedImages ->
                if (savedImages.isNullOrEmpty()) {
                    emitSavedImagesUiState(error = "Не найдено изображений")
                } else {
                    emitSavedImagesUiState(savedImages = savedImages)
                }
            }.onFailure {
                emitSavedImagesUiState(error = it.message.toString())
            }
        }
    }

    private fun emitSavedImagesUiState(
        isLoading: Boolean = false,
        savedImages: List<Pair<File, Bitmap>>? = null,
        error: String? = null
    ) {
        val dataState = SavedImagesDataState(isLoading, savedImages, error)
        savedImagesDataState.postValue(dataState)
    }

    data class SavedImagesDataState(
        val isLoading: Boolean,
        val savedImages: List<Pair<File, Bitmap>>?,
        val error: String?
    )
}