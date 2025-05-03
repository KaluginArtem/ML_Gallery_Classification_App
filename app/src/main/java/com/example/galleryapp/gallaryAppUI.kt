package com.example.galleryapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.galleryapp.navigation.AppNavigation

@Composable
fun GalleryAppUI() {
    MaterialTheme {
        // Вызываем основной UI с bottom navigation
        AppNavigation()
    }
}