package com.source.kotfirebase.data

import androidx.annotation.Keep
import com.google.firebase.storage.FileDownloadTask

@Keep
data class StorageDownloadResult(
    var taskSnapshot: FileDownloadTask.TaskSnapshot? = null,
    var exception: Exception? = null
)