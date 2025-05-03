package com.example.galleryapp.util

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object ImagePaddingSource {
    // Основное значение отступа, используемое по умолчанию.
    val defaultPadding: Dp = 8.dp

    // Отступ для элементов сетки (например, между изображениями).
    val gridItemPadding: Dp = 4.dp

    // Отступы для полноэкранного отображения изображений.
    val fullScreenPadding: Dp = 16.dp

    /**
     * Пример функции для динамического расчёта отступа в зависимости от ширины экрана.
     * Если требуется подстраивать отступы под разные размеры устройств, можно использовать эту функцию.
     */
    fun calculateDynamicPadding(screenWidth: Dp): Dp {
        // Примитивная логика: если экран широкий, отступ немного увеличивается.
        return if (screenWidth > 600.dp) 12.dp else defaultPadding
    }
}