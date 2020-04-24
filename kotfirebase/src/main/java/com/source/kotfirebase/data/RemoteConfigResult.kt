package com.source.kotfirebase.data

import androidx.annotation.Keep
import java.lang.Exception

@Keep
data class RemoteConfigResult(
    val isSuccessful: Boolean = false,
    val exception: Exception? = null
)