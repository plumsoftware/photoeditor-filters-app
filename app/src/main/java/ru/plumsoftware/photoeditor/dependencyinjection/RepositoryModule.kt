package ru.plumsoftware.photoeditor.dependencyinjection

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.plumsoftware.photoeditor.repositories.EditImageRepository
import ru.plumsoftware.photoeditor.repositories.EditImageRepositoryImpl
import ru.plumsoftware.photoeditor.repositories.SavedImageRepository
import ru.plumsoftware.photoeditor.repositories.SavedImagesRepositoryImpl

val repositoryModule = module {
    factory<EditImageRepository> { EditImageRepositoryImpl(androidContext()) }
    factory<SavedImageRepository> { SavedImagesRepositoryImpl(androidContext()) }
}