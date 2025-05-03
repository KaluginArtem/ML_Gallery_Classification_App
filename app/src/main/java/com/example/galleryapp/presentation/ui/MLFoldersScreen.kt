package com.example.galleryapp.presentation.ui

import android.net.Uri
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.galleryapp.presentation.viewmodel.MLFoldersViewModel


@Composable
fun MLFoldersScreen(
    onFolderClick: (String) -> Unit,
    viewModel: MLFoldersViewModel = hiltViewModel()
) {
    // Получаем результаты из репозитория через viewModel.mlResults
    val mlResults by viewModel.mlRepository.mlResults.collectAsState()

    val groupedResults = remember(mlResults) {
        mlResults.entries.groupBy({ it.value }, { it.key })
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        groupedResults.forEach { (category, uris) ->
            item {
                FolderCard(
                    category = category,
                    representativeUri = if (uris.isNotEmpty()) uris.first() else Uri.EMPTY,
                    photoCount = uris.size,
                    onClick = { onFolderClick(category) }
                )
            }
        }
    }
}