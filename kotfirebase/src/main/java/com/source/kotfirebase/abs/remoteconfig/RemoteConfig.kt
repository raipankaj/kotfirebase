package com.source.kotfirebase.abs.remoteconfig

import androidx.annotation.XmlRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.source.kotfirebase.data.RemoteConfigResult

object RemoteConfig : RemoteConfigService {

    private val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    /**
     * Update the remote config settings by changing the timeout and
     * set the minimum interval between successive fetch calls.
     */
    fun initRemoteConfig(
        @XmlRes defaultConfigValue: Int,
        fetchTimeoutInSeconds: Long? = null,
        durationInFetchIntervalInSeconds: Long? = null
    ) {
        val builder = FirebaseRemoteConfigSettings.Builder()

        fetchTimeoutInSeconds?.let {
            builder.setFetchTimeoutInSeconds(it)
        }

        durationInFetchIntervalInSeconds?.let {
            builder.setMinimumFetchIntervalInSeconds(it)
        }

        firebaseRemoteConfig.setConfigSettingsAsync(builder.build())

        firebaseRemoteConfig.setDefaultsAsync(defaultConfigValue)
    }

    override fun fetchAndShow(): LiveData<RemoteConfigResult> {

        val mutableLiveData = MutableLiveData<RemoteConfigResult>()
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener {
            updateResult(it, mutableLiveData)
        }

        return mutableLiveData
    }

    private fun updateResult(
        it: Task<Boolean>,
        mutableLiveData: MutableLiveData<RemoteConfigResult>
    ) {
        if (it.isSuccessful) {
            mutableLiveData.postValue(RemoteConfigResult(true))
        } else {
            mutableLiveData.postValue(RemoteConfigResult(false, it.exception))
        }
    }

    override fun justFetch() {
        firebaseRemoteConfig.fetch()
    }

    override fun activateFetchedResults(): LiveData<RemoteConfigResult> {

        val mutableLiveData = MutableLiveData<RemoteConfigResult>()
        firebaseRemoteConfig.activate().addOnCompleteListener {
            updateResult(it, mutableLiveData)
        }

        return mutableLiveData
    }

    fun getRemoteBoolean(key: String): Boolean = firebaseRemoteConfig.getBoolean(key)
    fun getRemoteDouble(key: String): Double = firebaseRemoteConfig.getDouble(key)
    fun getRemoteLong(key: String): Long = firebaseRemoteConfig.getLong(key)
    fun getRemoteString(key: String): String = firebaseRemoteConfig.getString(key)
}