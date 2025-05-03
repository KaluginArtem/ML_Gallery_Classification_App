package com.example.galleryapp.domain.model

data class GalleryFolder(
    val id: String,
    val name: String,
    val coverUri: String,  // или Uri, если исключить зависимости на Android
    val count: Int
)