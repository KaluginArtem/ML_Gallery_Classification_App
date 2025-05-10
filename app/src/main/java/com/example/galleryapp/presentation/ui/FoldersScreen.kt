package com.example.galleryapp.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.galleryapp.presentation.ui.components.GalleryFolder
import com.example.galleryapp.presentation.viewmodel.FoldersViewModel
import com.example.galleryapp.util.ImagePaddingSource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoldersScreen(
    viewModel: FoldersViewModel = hiltViewModel(),
    onFolderClick: (GalleryFolder) -> Unit = {}
) {
    val folders by viewModel.folders.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        viewModel.loadFolders()
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(ImagePaddingSource.defaultPadding)
    ) {
        items(folders) { folder ->
            GalleryFolderItem(folder = folder, onClick = { onFolderClick(folder) })
        }
    }
}

@Composable
fun GalleryFolderItem(folder: GalleryFolder, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = folder.coverUri,
            contentDescription = folder.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x55000000))
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
            ) {
                Text(text = folder.name, color = Color.White)
                Text(text = "(${folder.count})", color = Color.White)
            }
        }
    }
}