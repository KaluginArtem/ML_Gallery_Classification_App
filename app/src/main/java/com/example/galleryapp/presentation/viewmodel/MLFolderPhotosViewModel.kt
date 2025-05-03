package com.example.galleryapp.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.galleryapp.data.repository.MLRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MLFolderPhotosViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mlRepository: MLRepository
) : ViewModel() {

    private val folderName: String = savedStateHandle["folderName"] ?: ""

    val photosForCategory: StateFlow<List<Uri>> = mlRepository.mlResults
        .map { results -> results.filter { it.value == folderName }.keys.toList() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}