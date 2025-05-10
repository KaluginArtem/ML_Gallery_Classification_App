package com.example.galleryapp.data.repository

import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MLRepository @Inject constructor() {
    private val _mlResults = MutableStateFlow<Map<Uri, String>>(emptyMap())
    val mlResults: StateFlow<Map<Uri, String>> = _mlResults

    fun updateResults(results: Map<Uri, String>) {
        _mlResults.value = results
    }
}

