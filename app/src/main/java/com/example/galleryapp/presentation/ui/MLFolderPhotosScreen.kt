package com.example.galleryapp.presentation.ui

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.galleryapp.presentation.viewmodel.MLFolderPhotosViewModel
import com.example.galleryapp.presentation.ui.components.GalleryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MLFolderPhotosScreen(
    onPhotoClick: (Uri) -> Unit,
    onBackClick: () -> Unit,
    viewModel: MLFolderPhotosViewModel = hiltViewModel()
) {
    val photosForCategory by viewModel.photosForCategory.collectAsState()
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    TopAppBar(
        title = { Text(text = "ML Folder (${photosForCategory.size})") },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Text("Назад", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(photosForCategory) { uri ->
            GalleryItem(uri = uri, onClick = { selectedImageUri = uri })
        }
    }

    selectedImageUri?.let { uri ->
        FullScreenImageDialog(
            imageUri = uri,
            onDismiss = { selectedImageUri = null }
        )
    }
}