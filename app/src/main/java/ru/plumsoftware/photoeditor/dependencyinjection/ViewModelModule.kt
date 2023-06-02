package ru.plumsoftware.photoeditor.dependencyinjection

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.plumsoftware.photoeditor.viewmodels.EditImageViewModel
import ru.plumsoftware.photoeditor.viewmodels.SavedImagesViewModel

val viewModelModule = module {
    viewModel { EditImageViewModel(editImageRepository = get()) }
    viewModel { SavedImagesViewModel(savedImagesRepository = get()) }
}