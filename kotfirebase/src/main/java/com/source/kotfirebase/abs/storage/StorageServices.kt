package com.source.kotfirebase.abs.storage

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.source.kotfirebase.data.StorageResult

interface StorageServices {
    fun uploadBitmap(fileName: String, storagePath: String = "", bitmap: Bitmap): LiveData<StorageResult>
    fun uploadFileStream(fileName: String, storagePath: String = "", filePath: String): LiveData<StorageResult>
    fun uploadFile(fileName: String, storagePath: String = "", filePath: String): LiveData<StorageResult>
}