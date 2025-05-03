package com.example.galleryapp.ml

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageClassifier @Inject constructor(context: Context) {

    // Размер входного изображения (как у модели)
    private val inputWidth = 150
    private val inputHeight = 150
    private val channels = 3
    private val modelInputSize = 4 * inputWidth * inputHeight * channels  // float32

    // Интерпретатор модели
    private val interpreter: Interpreter

    // Метки (предполагается, что порядок меток соответствует порядку выходных нейронов)
    private val labels: List<String>

    init {
        // Загружаем модель из папки assets
        val modelBuffer = FileUtil.loadMappedFile(context, "model.tflite")
        interpreter = Interpreter(modelBuffer)
        // Загружаем метки из assets/labels.txt
        labels = FileUtil.loadLabels(context, "labels.txt")
    }

    /**
     * Выполняет инференс на переданном Bitmap.
     * Bitmap масштабируется до 150x150, нормализуется (делится на 255) и передаётся в модель.
     * Возвращает метку с максимальной вероятностью.
     */
    fun classifyImage(bitmap: Bitmap): String {
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputWidth, inputHeight, true)
        val inputBuffer = convertBitmapToByteBuffer(resizedBitmap)
        val outputShape = intArrayOf(1, labels.size)
        val outputBuffer = TensorBuffer.createFixedSize(outputShape, org.tensorflow.lite.DataType.FLOAT32)

        interpreter.run(inputBuffer, outputBuffer.buffer.rewind())

        val probabilities = outputBuffer.floatArray
        val maxIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: -1
        return if (maxIndex >= 0) labels[maxIndex] else "Unknown"
    }

    /**
     * Преобразует Bitmap в ByteBuffer с нормализацией.
     */
    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(modelInputSize)
        byteBuffer.order(ByteOrder.nativeOrder())
        val pixels = IntArray(inputWidth * inputHeight)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        for (pixel in pixels) {
            val r = ((pixel shr 16) and 0xFF) / 255.0f
            val g = ((pixel shr 8) and 0xFF) / 255.0f
            val b = (pixel and 0xFF) / 255.0f
            byteBuffer.putFloat(r)
            byteBuffer.putFloat(g)
            byteBuffer.putFloat(b)
        }
        return byteBuffer
    }
}