package ru.plumsoftware.photoeditor.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.plumsoftware.photoeditor.databinding.ItemContainerSavedImagesBinding
import ru.plumsoftware.photoeditor.listeners.SavedImagesListener
import java.io.File

class SavedImagesAdapter(
    private val savedImages: List<Pair<File, Bitmap>>,
    private val savedImagesListener: SavedImagesListener
) :
    RecyclerView.Adapter<SavedImagesAdapter.SavedImagesViewHolder>() {

    inner class SavedImagesViewHolder(val binding: ItemContainerSavedImagesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedImagesViewHolder {
        val binding = ItemContainerSavedImagesBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SavedImagesViewHolder(binding)
    }

    override fun getItemCount() = savedImages.size

    override fun onBindViewHolder(holder: SavedImagesViewHolder, position: Int) {
        with(holder) {
            with(savedImages[position]) {
                binding.imageSaved.setImageBitmap(second)
                binding.imageSaved.setOnClickListener {
                    savedImagesListener.onImageClicked(first)
                }
            }
        }
    }
}