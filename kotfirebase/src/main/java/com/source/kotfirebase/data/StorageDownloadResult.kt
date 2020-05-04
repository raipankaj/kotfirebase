package com.source.kotfirebase.data

import android.net.Uri
import androidx.annotation.Keep

@Keep
data class StorageDownloadResult(
    var taskSnapshot: OnDownloadSuccess? = null,
    //This contains data only when download is stored in local memory
    var byteArray: ByteArray? = null,
    //In case user request to get only the download uri than it contains data
    var downloadUrl: Uri? = null,
    var exception: Exception? = null
)

@Keep
data class OnDownloadSuccess(
    val byteTransferred: Long,
    val totalByteCount: Long
)