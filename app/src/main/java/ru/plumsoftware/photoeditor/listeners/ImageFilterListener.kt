package ru.plumsoftware.photoeditor.listeners

import ru.plumsoftware.photoeditor.data.ImageFilter

interface ImageFilterListener {
    fun onFilterSelected(imageFilter: ImageFilter)
}