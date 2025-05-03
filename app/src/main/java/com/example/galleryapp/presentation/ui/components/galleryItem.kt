package com.example.galleryapp.presentation.ui.components

import android.net.Uri
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun GalleryItem(uri: Uri, onClick: () -> Unit) {
    AsyncImage(
        model = uri,
        contentDescription = "Gallery image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .clickable {
                onClick()
            }
    )
}