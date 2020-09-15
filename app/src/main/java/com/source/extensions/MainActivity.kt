package com.source.extensions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import com.source.kotfirebase.*
import com.source.kotfirebase.abs.firestore.KotFirestore
import com.source.kotfirebase.abs.remoteconfig.KotRemoteConfig
import com.source.kotfirebase.abs.storage.KotStorage
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
            KotStorage.uploadFile("myimage.jpg", "storagePath", "filePath")

        //Upload file stream by providing cloud storage path and file path
        val uploadFileStreamLiveData: LiveData<StorageResult> =
            KotStorage.uploadFileStream("myimage.jpg", "storagePath", "filePath")

        //Upload bitmap by providing cloud storage path and bitmap
        val uploadBitmapLiveData: LiveData<StorageResult> =
            KotStorage.uploadBitmap("myimage.jpg", "storagePath", bitmap)

        val getDownloadUrlLiveData: LiveData<StorageDownloadResult> =
            KotStorage.getDownloadUrl("myimage.jpg", "storagePath")

        val downloadFileLiveData: LiveData<StorageDownloadResult> =
            KotStorage.downloadFile("myimage.jpg", "storagePath", file)

        val ONE_MEGABYTE: Long = 1024 * 1024
        val downloadTempStorageLiveData: LiveData<StorageDownloadResult> =
            KotStorage.downloadInMemory("myimage.jpg", "storagePath", ONE_MEGABYTE)
    }

    private fun remoteConfig() {
        //Initiate the remote config to provide the default value and to change other params
        KotRemoteConfig.initRemoteConfig(R.xml.remote_config_defaults)

        //Fetch and activate to show the update the result in realtime
        val isFetchedLiveData: LiveData<RemoteConfigResult> = KotRemoteConfig.fetchAndShow()

        //Just fetch but do not update the values
        KotRemoteConfig.justFetch()

        //Activate the fetched result so as to get updated values
        val isFetchedResultActivated: LiveData<RemoteConfigResult> =
            KotRemoteConfig.activateFetchedResults()

        //Get the value from the remote config using these predefined methods
        KotRemoteConfig.getRemoteBoolean("is_update_available")
        KotRemoteConfig.getRemoteDouble("current_version")
        KotRemoteConfig.getRemoteLong("last_updated_timestamp")
        KotRemoteConfig.getRemoteString("app_theme_name")
    }

    private fun cloudFirestore() {
        //Get single time result
        val documentsLiveData: LiveData<DocumentResult> =
            KotFirestore.getFromDocument("collectionId/documentId")
        //or get realtime update using following method
        val documentsRealtimeLiveData: LiveData<DocumentResult> =
            KotFirestore.getFromDocument("collectionId/documentId", true)

        //Get the document data to the model class
        val modelFormedLiveData: LiveData<Any> =
            KotFirestore.getFromDocumentInto<Any>("collectionId/documentId")
        //or get the document data to the model class in realtime
        val modelFormedRealtimeLiveData: LiveData<Any> =
            KotFirestore.getFromDocumentInto<Any>("collectionId/documentId", true)

        //Get the documents in collection
        val collectionLiveData: LiveData<CollectionResult> =
            KotFirestore.getFromCollection("collectionId")
        //or get the collection with realtime listener attached to it
        val collectionRealtimeLiveData: LiveData<CollectionResult> =
            KotFirestore.getFromCollection("collectionId", true)

        //Get the documents from collection into list of model
        val modelFormedCollectionLiveData: LiveData<List<Any>> =
            KotFirestore.getFromCollectionInto<Any>("collectionId")
        //or get the documents from collection into list of model with realtime listener
        val modelFormedCollectionRealtimeLiveData: LiveData<List<Any>> =
            KotFirestore.getFromCollectionInto("collectionId", true)

        //Add document to collection
        val addToCollection: LiveData<CollectionWrite> =
            KotFirestore.addDocumentToCollection("collectionId", mapOf("one" to 1, "two" to 2))

        //Write data to individual document based on document ID
        val addToDocument: LiveData<DocumentWrite> =
            KotFirestore.addDocumentWithID(
                "collectionId/documentId", mapOf("one" to 1, "two" to 2)
            )

        //Update document based on document ID
        val updateDocument: LiveData<DocumentWrite> =
            KotFirestore.updateDocumentByID(
                "collectionId/documentId", mapOf("one" to 1, "two" to 2)
            )

        //Delete document based on document ID
        KotFirestore.deleteDocumentByID("collectionId/documentID")

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
