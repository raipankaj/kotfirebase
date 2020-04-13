package com.source.extensions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import com.source.kotfirebase.abs.firestore.Firestore
import com.source.kotfirebase.abs.storage.Storage
import com.source.kotfirebase.data.CollectionWrite
import com.source.kotfirebase.data.DocumentResult
import com.source.kotfirebase.data.DocumentWrite
import com.source.kotfirebase.data.StorageResult
import com.source.kotfirebase.getFirebaseDocuments
import com.source.kotfirebase.getFirebaseDocumentsIn
import com.source.kotfirebase.logBundledEvent
import com.source.kotfirebase.logEvent

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
            Storage.uploadFile("storagePath", "filePath")

        //Upload file stream by providing cloud storage path and file path
        val uploadFileStreamLiveData: LiveData<StorageResult> =
            Storage.uploadFileStream("storagePath", "filePath")

        //Upload bitmap by providing cloud storage path and bitmap
        val uploadBitmapLiveData: LiveData<StorageResult> =
            Storage.uploadBitmap("storagePath", bitmap)
        //or
        //Ktx version of uploading bitmap is
        val uploadBitmapKtxLiveData: LiveData<StorageResult> =
            bitmap.uploadToCloudStorageAt("storagePath")
    }

    private fun cloudFirestore() {
        //Get single time result
        val documentsLiveData: LiveData<DocumentResult> =
            Firestore.getFromDocument("collectionId/documentId")
        //or
        //Get realtime result
        val documentsRealtimeLiveData: LiveData<DocumentResult> =
            Firestore.getFromDocument("collectionId/documentId", true)
        //or
        //Get the document data to the model class
        val modelFormedLiveData: LiveData<Any> =
            Firestore.getDocumentResultsIn<Any>("collectionId/documentId")
        //or
        //Get the document data to the model class in realtime
        val modelFormedRealtimeLiveData: LiveData<Any> =
            Firestore.getDocumentResultsIn<Any>("collectionId/documentId", true)

        //Write data to collection
        val addToCollection: LiveData<CollectionWrite> =
            Firestore.addToCollection("collectionId", mapOf("one" to 1, "two" to 2))

        //Write data to document
        val addToDocument: LiveData<DocumentWrite> =
            Firestore.addToDocument("collectionId/documentId",
                mapOf("one" to 1, "two" to 2))

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
    }
}
