package ru.plumsoftware.photoeditor.listeners

import java.io.File

interface SavedImagesListener {
    fun onImageClicked(file: File)
}