package com.source.kotfirebase.data

import androidx.annotation.Keep

@Keep
data class StorageDownloadResult(
    var taskSnapshot: OnDownloadSuccess? = null,
    var exception: Exception? = null
)

@Keep
data class OnDownloadSuccess(
    val byteTransferred: Long,
    val totalByteCount: Long
)