package com.example.galleryapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.galleryapp.data.repository.FolderRepository
import com.example.galleryapp.presentation.ui.components.GalleryFolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoldersViewModel @Inject constructor(
    private val repository: FolderRepository
) : ViewModel() {

    private val _folders = MutableStateFlow<List<GalleryFolder>>(emptyList())
    val folders: StateFlow<List<GalleryFolder>> = _folders

    fun loadFolders() {
        viewModelScope.launch {
            _folders.value = repository.loadFoldersFromGallery()
        }
    }
}