package com.example.galleryapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Photos : Screen("photos", "Photos", Icons.Filled.Photo)
    object Folders : Screen("folders", "Folders", Icons.Filled.Folder)
    object ML : Screen("ml", "ML Folders", Icons.Filled.Star)
    // Маршрут для показа содержимого папки с параметрами bucketId и folderName
    object FolderPhotos : Screen("folderPhotos", "Folder Photos", Icons.Filled.Folder) {
        const val routePattern = "folder/{bucketId}/{folderName}"
        fun createRoute(bucketId: String, folderName: String) = "folder/$bucketId/$folderName"
    }
    object MLFolderPhotos : Screen("mlfolderphotos", "MLFolderPhotos", Icons.Filled.Folder) {
        // Шаблон маршрута для ML папок
        const val routePattern = "mlfolderphotos/{folderName}"
        fun createRoute(folderName: String) = "mlfolderphotos/$folderName"
    }

}