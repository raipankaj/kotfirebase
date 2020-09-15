package com.source.kotfirebase.abs.remoteconfig

import androidx.annotation.XmlRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.source.kotfirebase.data.RemoteConfigResult

object KotRemoteConfig : RemoteConfigService {

    private val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    /**
     * Add the default remote config values and also update the
     * remote config settings by changing the timeout and
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

    /**
     * Fetch the update value from server and update the remote config
     * parameters to return the updated value upon subsequent call.
     * Note: New values will only be fetched based on the interval
     * provided while initiating remote config.
     */
    override fun fetchAndShow(): LiveData<RemoteConfigResult> {

        val mutableLiveData = MutableLiveData<RemoteConfigResult>()
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener {
            updateResult(it, mutableLiveData)
        }

        return mutableLiveData
    }

    /**
     * Just fetch the value from the server and do not update the remote config
     * parameters to return the updated value upon subsequent call.
     * Note: New values will only be fetched based on the interval
     * provided while initiating remote config.
     */
    override fun justFetch() {
        firebaseRemoteConfig.fetch()
    }

    /**
     * Update the remote config values based on earlier fetched result, the
     * remote config parameters will return the updated value upon subsequent call.
     * Note: New values will only be fetched based on the interval
     * provided while initiating remote config.
     */
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
}