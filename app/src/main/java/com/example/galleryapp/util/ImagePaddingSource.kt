package com.example.galleryapp.util

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object ImagePaddingSource {
    val defaultPadding: Dp = 8.dp

    val gridItemPadding: Dp = 4.dp

    fun calculateDynamicPadding(screenWidth: Dp): Dp {
        return if (screenWidth > 600.dp) 12.dp else defaultPadding
    }
}