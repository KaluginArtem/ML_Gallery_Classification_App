package com.example.galleryapp.presentation.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.galleryapp.data.repository.ImageRepository
import com.example.galleryapp.data.repository.MLRepository
import com.example.galleryapp.ml.ImageClassifier
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

// Универсальная функция для загрузки Bitmap из URI
fun loadBitmapFromUri(context: android.content.Context, uri: Uri): Bitmap? {
    return try {
        context.contentResolver.openInputStream(uri)?.use { stream ->
            BitmapFactory.decodeStream(stream)
        }
    } catch (e: Exception) {
        null
    }
}

@HiltViewModel
class MLFoldersViewModel @Inject constructor(
    application: Application,
    private val imageRepository: ImageRepository,
    private val classifier: ImageClassifier,
    val mlRepository: MLRepository
) : AndroidViewModel(application) {

    private val _photos = MutableStateFlow<List<Uri>>(emptyList())
    val photos: StateFlow<List<Uri>> = _photos

    init {
        loadAndClassifyPhotos()
    }

    private fun loadAndClassifyPhotos() {
        viewModelScope.launch {
            val photoList = withContext(Dispatchers.IO) {
                imageRepository.loadImagesFromGallery()
            }
            _photos.value = photoList

            val classificationCache = mutableMapOf<Uri, String>()

            photoList.forEach { uri ->
                if (!classificationCache.containsKey(uri)) {
                    val result = withContext(Dispatchers.IO) {
                        val bitmap = loadBitmapFromUri(getApplication(), uri)
                        bitmap?.let { classifier.classifyImage(it) }
                    }
                    result?.let { label ->
                        classificationCache[uri] = label
                        mlRepository.updateResults(classificationCache.toMap())
                    }
                }
            }
        }
    }
}