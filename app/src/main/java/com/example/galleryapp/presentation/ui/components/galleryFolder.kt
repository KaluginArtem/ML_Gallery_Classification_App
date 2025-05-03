package com.example.galleryapp.presentation.ui.components

import android.net.Uri

data class GalleryFolder(
    val id: String,
    val name: String,
    val coverUri: Uri,
    var count: Int
)