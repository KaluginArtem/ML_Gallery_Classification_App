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

    private val inputWidth = 150
    private val inputHeight = 150
    private val channels = 3
    private val modelInputSize = 4 * inputWidth * inputHeight * channels  // float32

    private val interpreter: Interpreter

    private val labels: List<String>

    init {
        val modelBuffer = FileUtil.loadMappedFile(context, "model.tflite")
        interpreter = Interpreter(modelBuffer)
        labels = FileUtil.loadLabels(context, "labels.txt")
    }


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