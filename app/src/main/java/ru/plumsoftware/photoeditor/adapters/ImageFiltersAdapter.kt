package ru.plumsoftware.photoeditor.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.plumsoftware.photoeditor.R
import ru.plumsoftware.photoeditor.data.ImageFilter
import ru.plumsoftware.photoeditor.databinding.ItemContainerFilterBinding
import ru.plumsoftware.photoeditor.listeners.ImageFilterListener
import ru.plumsoftware.photoeditor.settings.Settings

class ImageFiltersAdapter(
    private val imageFilters: List<ImageFilter>,
    private val imageFilterListener: ImageFilterListener
) :
    RecyclerView.Adapter<ImageFiltersAdapter.ImageFilterViewHolder>() {

    private var selectedFilterPosition = 0
    private var previouslySelectedFilterPosition = 0

    inner class ImageFilterViewHolder(val binding: ItemContainerFilterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageFilterViewHolder {
        val binding =
            ItemContainerFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageFilterViewHolder(binding)
    }

    override fun getItemCount() = imageFilters.size

    override fun onBindViewHolder(
        holder: ImageFilterViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        with(holder) {
            with(imageFilters[position]) {
                binding.imageFilterPreview.setImageBitmap(filterPreview)
                binding.textFilterName.text = name
                binding.root.setOnClickListener {
                    if (position != selectedFilterPosition) {
                        imageFilterListener.onFilterSelected(this)
                        previouslySelectedFilterPosition = selectedFilterPosition
                        selectedFilterPosition = position
                        with(this@ImageFiltersAdapter) {
                            notifyItemChanged(previouslySelectedFilterPosition, Unit)
                            notifyItemChanged(selectedFilterPosition, Unit)
                        }
                        with(Settings(binding.root.context)) {
                            if (this.getBooleanPreference(Settings.SHOW_MORE_TIP_1, true)) {
                                displayTipsDialog(
                                    "Если зажать изображение, то можно увидеть оригинал, без фильтра.",
                                    binding.imageFilterPreview.context,
                                    this@with
                                )
                            }
                        }
                    }
                }
            }
            binding.textFilterName.setTextColor(
                ContextCompat.getColor(
                    binding.textFilterName.context,
                    if (selectedFilterPosition == position) R.color.seed else R.color.gray
                )
            )
        }


    }

    private fun displayTipsDialog(message: String?, context: Context, s: Settings) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("\uD83D\uDCA1Подсказка")
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton("ПОНЯТНО") { dialog, which ->

        }
        builder.setNegativeButton("БОЛЬШЕ НЕ ПОКАЗЫВАТЬ") { dialog, which ->
            s.saveBooleanPreference(Settings.SHOW_MORE_TIP_1, false)
        }
        val dialog = builder.create()
        dialog.show()
    }
}