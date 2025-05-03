package com.example.galleryapp.presentation.ui

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.galleryapp.presentation.ui.components.GalleryItem
import com.example.galleryapp.presentation.viewmodel.FolderPhotosViewModel
import com.example.galleryapp.util.ImagePaddingSource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderPhotosScreen(
    bucketId: String,
    folderName: String,
    onBack: () -> Unit,
    viewModel: FolderPhotosViewModel = hiltViewModel()
) {
    // Подписываемся на StateFlow из ViewModel
    val images by viewModel.images.collectAsState(initial = emptyList())
    // Загружаем изображения для данной папки один раз при изменении bucketId
    LaunchedEffect(bucketId) {
        viewModel.loadImages(bucketId)
    }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = folderName) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(ImagePaddingSource.gridItemPadding)
        ) {
            items(images) { imageUri ->
                // При клике сохраняем выбранный URI для полноэкранного просмотра
                GalleryItem(uri = imageUri, onClick = { selectedImageUri = imageUri })
            }
        }
    }

    selectedImageUri?.let { uri ->
        FullScreenImageDialog(
            imageUri = uri,
            onDismiss = { selectedImageUri = null }
        )
    }
}