package com.source.kotfirebase.abs.remoteconfig

import androidx.lifecycle.LiveData
import com.source.kotfirebase.data.RemoteConfigResult

interface RemoteConfigService {
    fun fetchAndShow(): LiveData<RemoteConfigResult>
    fun justFetch()
    fun activateFetchedResults(): LiveData<RemoteConfigResult>
}