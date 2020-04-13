package com.source.kotfirebase.abs.storage

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.source.kotfirebase.data.StorageResult

interface StorageServices {
    fun uploadBitmap(storagePath: String, bitmap: Bitmap): LiveData<StorageResult>
    fun uploadFileStream(storagePath: String, filePath: String): LiveData<StorageResult>
    fun uploadFile(storagePath: String, filePath: String): LiveData<StorageResult>
}