package com.source.extensions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import com.source.kotfirebase.*
import com.source.kotfirebase.abs.firestore.Firestore
import com.source.kotfirebase.abs.remoteconfig.RemoteConfig
import com.source.kotfirebase.abs.storage.Storage
import com.source.kotfirebase.data.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*Cloud firestore APIs usage*/
        cloudFirestore()

        /*Cloud storage*/
        cloudStorage()

        /*Firebase Analytics*/
        firebaseAnalytics()

        /*Remote config*/
        remoteConfig()
    }

    private fun firebaseAnalytics() {
        //Log event to firebase by providing bundle
        logBundledEvent("eventName") {
            putString("name", "xyz")
            putString("info", "test")
        }

        //log event to firebase without bundle
        logEvent("eventName")
    }

    private fun cloudStorage() {
        //Upload file by providing cloud storage path and file path
        val uploadFileLiveData: LiveData<StorageResult> =
            Storage.uploadFile("myimage.jpg","storagePath", "filePath")

        //Upload file stream by providing cloud storage path and file path
        val uploadFileStreamLiveData: LiveData<StorageResult> =
            Storage.uploadFileStream("myimage.jpg","storagePath", "filePath")

        //Upload bitmap by providing cloud storage path and bitmap
        val uploadBitmapLiveData: LiveData<StorageResult> =
            Storage.uploadBitmap("myimage.jpg","storagePath", bitmap)

        val getDownloadUrlLiveData: LiveData<StorageDownloadResult> =
            Storage.getDownloadUrl("myimage.jpg", "storagePath")

        val downloadFileLiveData: LiveData<StorageDownloadResult> =
            Storage.downloadFile("myimage.jpg", "storagePath", file)

        val ONE_MEGABYTE: Long = 1024 * 1024
        val downloadTempStorageLiveData: LiveData<StorageDownloadResult> =
            Storage.downloadInMemory("myimage.jpg", "storagePath", ONE_MEGABYTE)
    }

    private fun remoteConfig() {
        //Initiate the remote config to provide the default value and to change other params
        RemoteConfig.initRemoteConfig(R.xml.remote_config_defaults)

        //Fetch and activate to show the update the result in realtime
        val isFetchedLiveData: LiveData<RemoteConfigResult> = RemoteConfig.fetchAndShow()

        //Just fetch but do not update the values
        RemoteConfig.justFetch()

        //Activate the fetched result so as to get updated values
        val isFetchedResultActivated: LiveData<RemoteConfigResult> = RemoteConfig.activateFetchedResults()

        //Get the value from the remote config using these predefined methods
        RemoteConfig.getRemoteBoolean("is_update_available")
        RemoteConfig.getRemoteDouble("current_version")
        RemoteConfig.getRemoteLong("last_updated_timestamp")
        RemoteConfig.getRemoteString("app_theme_name")
    }

    private fun cloudFirestore() {
        //Get single time result
        val documentsLiveData: LiveData<DocumentResult> =
            Firestore.getFromDocument("collectionId/documentId")
        //or
        //Get realtime result
        val documentsRealtimeLiveData: LiveData<DocumentResult> =
            Firestore.getFromDocument("collectionId/documentId", true)

        //Get the document data to the model class
        val modelFormedLiveData: LiveData<Any> =
            Firestore.getDocumentResultsIn<Any>("collectionId/documentId")
        //or
        //Get the document data to the model class in realtime
        val modelFormedRealtimeLiveData: LiveData<Any> =
            Firestore.getDocumentResultsIn<Any>("collectionId/documentId", true)

        Firestore.addToCollection("sa","ss").observe(this) {
            it.documentReference
        }

        //Get the documents in collection
        val collectionLiveData: LiveData<CollectionResult> =
            Firestore.getFromCollection("collectionId")
        //or
        //Get the collection with realtime listener attached to it
        val collectionRealtimeLiveData: LiveData<CollectionResult> =
            Firestore.getFromCollection("collectionId", true)

        //Write data to collection
        val addToCollection: LiveData<CollectionWrite> =
            Firestore.addToCollection("collectionId", mapOf("one" to 1, "two" to 2))

        //Write data to document
        val addToDocument: LiveData<DocumentWrite> =
            Firestore.addToDocument(
                "collectionId/documentId",
                mapOf("one" to 1, "two" to 2)
            )

        //Using kotlin extension on string for single time result
        val documentsKtxLiveData: LiveData<DocumentResult> =
            "collectionId/documentId".getFirebaseDocuments()

        //Using kotlin extension on string for realtime result
        val documentsKtxRealtimeLiveData: LiveData<DocumentResult> =
            "collectionId/documentId".getFirebaseDocuments(true)
        //or
        //Get the document data to the model class
        val modelFormedKtxLiveData: LiveData<Any> =
            "collectionId/documentId".getFirebaseDocumentsIn<Any>()

        //Get the document data to the model class in realtime
        val modelFormedKtxRealtimeLiveData: LiveData<Any> =
            "collectionId/documentId".getFirebaseDocumentsIn<Any>(true)

        //Get documents in the collection using ktx
        val collectionKtxLiveData: LiveData<CollectionResult> =
            "collectionId".getFirebaseCollection()
        //or
        //Listen for the realtime update
        val collectionRealtimeKtxLiveData: LiveData<CollectionResult> =
            "collectionId".getFirebaseCollection(true)
    }
}
