package com.source.kotfirebase.data

import android.net.Uri
import androidx.annotation.Keep
import com.google.firebase.storage.StorageMetadata

@Keep
data class StorageResult(
    var onUploadSuccess: OnUploadSuccess? = null,
    var exception: Exception? = null
)

@Keep
data class OnUploadSuccess(
    val byteTransferred: Long,
    val metadata: StorageMetadata?,
    val totalByteCount: Long,
    val uploadSessionUri: Uri?
)