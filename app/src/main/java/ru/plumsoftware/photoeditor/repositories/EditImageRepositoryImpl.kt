package ru.plumsoftware.photoeditor.repositories

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorMatrixFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageRGBFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSaturationFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSepiaToneFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.plumsoftware.photoeditor.data.ImageFilter
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class EditImageRepositoryImpl(private val context: Context) : EditImageRepository {

    override suspend fun getImageFilters(image: Bitmap): List<ImageFilter> {
        val gpuImage = GPUImage(context).apply {
            setImage(image)

        }
        val imageFilters: ArrayList<ImageFilter> = ArrayList()

//        region:: Filters

//        Normal
        GPUImageFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilter(
                    name = "Обычный",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

//        Retro
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.2f, 0.0f,
                0.1f, 0.1f, 1.0f, 0.0f,
                1.0f, 0.0f, 0.0f, 1.0f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilter(
                    name = "Ретро",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

//        Just
        GPUImageColorMatrixFilter(
            0.9f,
            floatArrayOf(
                0.4f, 0.6f, 0.5f, 0.0f,
                0.0f, 0.4f, 1.0f, 0.0f,
                0.05f, 0.1f, 0.4f, 0.4f,
                1.0f, 1.0f, 1.0f, 1.0f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilter(
                    name = "Джаст",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

//        Hume
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.25f, 0.0f, 0.2f, 0.0f,
                0.0f, 1.0f, 0.2f, 0.0f,
                0.0f, 0.3f, 1.0f, 0.3f,
                0.0f, 0.0f, 0.0f, 1.0f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilter(
                    name = "Хьюм",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

//        Desert
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0.6f, 0.4f, 0.2f, 0.05f,
                0.0f, 0.8f, 0.3f, 0.05f,
                0.3f, 0.3f, 0.5f, 0.08f,
                0.0f, 0.0f, 0.0f, 1.0f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilter(
                    name = "Пустыня",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

//        Old times
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.0f, 0.05f, 0.0f, 0.0f,
                -0.2f, 1.1f, -0.2f, 0.11f,
                0.2f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilter(
                    name = "Старые времена",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

//        Limo
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.0f, 0.0f, 0.08f, 0.0f,
                0.4f, 1.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.1f,
                0.0f, 0.0f, 0.0f, 1.0f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilter(
                    name = "Лимузин",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

//        Sepia
        GPUImageSepiaToneFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilter(
                    name = "Сепия",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

//        Solar
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.5f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                0f, 0f, 0f, 1f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilter(
                    name = "Солнечный",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

//        Wole
        GPUImageSaturationFilter(2.0f).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilter(
                    name = "Вол",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

//        Neutron
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0f, 1f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0.6f, 1f, 0f,
                0f, 0f, 0f, 1f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilter(
                    name = "Нейтрон",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

//        Bright
        GPUImageRGBFilter(1.1f, 1.3f, 1.6f).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilter(
                    name = "Яркость",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

//        Milk
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0f, 1f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0.64f, 0.5f, 0f,
                0f, 0f, 0f, 1f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilter(
                    name = "Молочный",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

//        BW
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0.0f, 1.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 1.0f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilter(
                    name = "Б-В",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

//        Clue
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilter(
                    name = "Подсказка",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

//        Muli
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.0f, 0.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilter(
                    name = "Мули",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

//        Aero
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0.0f, 0.0f, 1.0f, 0.0f,
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilter(
                    name = "Аэро",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

//        Classic
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0.763f, 0.0f, 0.2062f, 0f,
                0.0f, 0.9416f, 0.0f, 0f,
                0.1623f, 0.2614f, 0.8052f, 0f,
                0f, 0f, 0f, 1f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilter(
                    name = "Классика",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

//        Atom
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0.5162f, 0.3799f, 0.3247f, 0f,
                0.039f, 1.0f, 0f, 0f,
                -0.4773f, 0.461f, 1.0f, 0f,
                0f, 0f, 0f, 1f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilter(
                    name = "Атом",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

//        Mars
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                0.0f, 0.0f, 0.5183f, 0.3183f,
                0.0f, 0.5497f, 0.5416f, 0f,
                0.5237f, 0.5269f, 0.0f, 0f,
                0f, 0f, 0f, 1f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilter(
                    name = "Марс",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

//        Yeli
        GPUImageColorMatrixFilter(
            1.0f,
            floatArrayOf(
                1.0f, -0.3831f, 0.3883f, 0.0f,
                0.0f, 1.0f, 0.2f, 0.0f,
                -0.1961f, 0.0f, 1.0f, 0.0f,
                0f, 0f, 0f, 1f
            )
        ).also { filter ->
            gpuImage.setFilter(filter)
            imageFilters.add(
                ImageFilter(
                    name = "Й-ели",
                    filter = filter,
                    filterPreview = gpuImage.bitmapWithFilterApplied
                )
            )
        }

//        endregion

        return imageFilters
    }

    override suspend fun saveFilteredImage(filteredBitmap: Bitmap): Uri? {
//        val mediaStoreDirectory = File(
//            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
//            "Saved Images"
//        )
//        if (!mediaStoreDirectory.exists())
//            mediaStoreDirectory.mkdirs()
//        val fileName = "IMG_${System.currentTimeMillis()}.jpg"
//        val file = File(mediaStoreDirectory, fileName)
//        saveFile(file, filteredBitmap)
//        return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        return try {
            val mediaStoreDirectory = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "Saved"
            )
            if (!mediaStoreDirectory.exists())
                mediaStoreDirectory.mkdirs()
            val fileName = "IMG_${System.currentTimeMillis()}.jpg"
            val file = File (mediaStoreDirectory, fileName)
            saveFile(file, filteredBitmap)
            FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        } catch (exception: Exception) {
            Toast.makeText(context, exception.message.toString(), Toast.LENGTH_LONG).show()
            null
        }
    }

    override suspend fun prepareImagePreview(imageUri: Uri): Bitmap? {
        getInputStreamFromUri(uri = imageUri)?.let { inputStream ->
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            val width = context.resources.displayMetrics.widthPixels
            val height = ((originalBitmap.height * width) / originalBitmap.width)
            return Bitmap.createScaledBitmap(originalBitmap, width, height, false)
        } ?: return null
    }

    private fun getInputStreamFromUri(uri: Uri): InputStream? {
        return context.contentResolver.openInputStream(uri)
    }

//    private fun saveFile(file: File, bitmap: Bitmap) {
//        with(FileOutputStream(file)) {
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
//            flush()
//            close()
//        }
//    }

    private suspend fun saveFile(file: File, bitmap: Bitmap) {
        withContext(Dispatchers.IO) { // используем контекст ввода/вывода
            FileOutputStream(file).use { fileOutputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
                fileOutputStream.flush()
                fileOutputStream.close()
            }
        }
    }
}