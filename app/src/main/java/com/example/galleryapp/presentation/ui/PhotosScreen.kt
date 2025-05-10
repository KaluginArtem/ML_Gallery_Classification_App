package com.example.galleryapp.presentation.ui

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.galleryapp.ml.ImageClassifier
import com.example.galleryapp.presentation.ui.components.GalleryItem
import com.example.galleryapp.presentation.viewmodel.GalleryViewModel
import com.example.galleryapp.util.ImagePaddingSource
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotosScreen(imageUris: List<Uri>, onImageClick: (Uri) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(ImagePaddingSource.gridItemPadding)
    ) {
        items(imageUris) { uri ->
            GalleryItem(uri = uri, onClick = { onImageClick(uri) })
        }
    }
}

@Composable
fun PhotosScreenWrapper(viewModel: GalleryViewModel = hiltViewModel()) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val photos by viewModel.photos.collectAsState(initial = emptyList())
    var dateInput by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.loadPhotos()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = dateInput,
                onValueChange = { dateInput = it },
                label = { Text("Введите дату (DD.MM.YYYY)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    focusManager.clearFocus() // скрыть клавиатуру
                    val dateRange = parseFullDate(dateInput)
                    if (dateRange != null) {
                        viewModel.searchPhotos(dateRange.first, dateRange.second)
                    } else {
                        viewModel.loadPhotos()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Найти")
            }
        }
        PhotosScreen(imageUris = photos, onImageClick = { uri ->
            selectedImageUri = uri
        })
    }

    selectedImageUri?.let { uri ->
        FullScreenImageDialog(imageUri = uri) {
            selectedImageUri = null
        }
    }
}

fun parseFullDate(input: String): Pair<Long, Long>? {
    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    sdf.isLenient = false
    return try {
        val date = sdf.parse(input) ?: return null
        val calendar = Calendar.getInstance().apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val start = calendar.timeInMillis

        calendar.apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        val end = calendar.timeInMillis
        Pair(start, end)
    } catch (e: Exception) {
        null
    }
}