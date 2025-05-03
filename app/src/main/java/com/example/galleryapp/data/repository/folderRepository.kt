package com.example.galleryapp.data.repository

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.example.galleryapp.presentation.ui.components.GalleryFolder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderRepository @Inject constructor(
    private val context: Context
) {
    fun loadFoldersFromGallery(): List<GalleryFolder> {
        val folderMap = mutableMapOf<String, GalleryFolder>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_ID
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        val query = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )

        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val bucketNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val bucketIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)

            while (cursor.moveToNext()) {
                val imageId = cursor.getLong(idColumn)
                val imageUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    imageId
                )
                val folderName = cursor.getString(bucketNameColumn)
                val bucketId = cursor.getString(bucketIdColumn)

                // Если папка уже существует – обновляем количество фотографий
                if (folderMap.containsKey(bucketId)) {
                    folderMap[bucketId]?.let { it.count += 1 }
                } else {
                    // Создаём новую папку с первой фотографией в качестве обложки
                    folderMap[bucketId] = GalleryFolder(
                        id = bucketId,
                        name = folderName,
                        coverUri = imageUri,
                        count = 1
                    )
                }
            }
        }

        return folderMap.values.toList()
    }

    fun loadImagesFromFolder(bucketId: String): List<Uri> {
        val images = mutableListOf<Uri>()
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val selection = "${MediaStore.Images.Media.BUCKET_ID} = ?"
        val selectionArgs = arrayOf(bucketId)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        val query = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        query?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
                )
                images.add(contentUri)
            }
        }
        return images
    }
}