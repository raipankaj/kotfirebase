package com.source.kotfirebase.abs.storage

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.source.kotfirebase.data.StorageDownloadResult
import com.source.kotfirebase.data.StorageResult
import java.io.File

interface StorageServices {
    fun uploadBitmap(fileName: String, storagePath: String = "", bitmap: Bitmap): LiveData<StorageResult>
    fun uploadFileStream(fileName: String, storagePath: String = "", filePath: String): LiveData<StorageResult>
    fun uploadFile(fileName: String, storagePath: String = "", filePath: String): LiveData<StorageResult>

    fun downloadFile(fileName: String, storagePath: String = "", destinationFile: File): LiveData<StorageDownloadResult>
    fun downloadInMemory(fileName: String, storagePath: String = "", maxDownloadByteSize: Long): LiveData<StorageDownloadResult>
    fun getDownloadUrl(fileName: String, storagePath: String = ""): LiveData<StorageDownloadResult>
}