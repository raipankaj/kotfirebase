# kotfirebase
A wrapper over existing firebase APIs to reduce the developer effort in using them, currently it supports APIs for cloud firestore, cloud storage and firebase analytics

The objective of this library is to bring down APIs to a single line of code by using the kotlin features, under the hood everything remains same it's just a matter of fact that developer need not have to worry about them.

<h3>Cloud Firestore</h3>
Here are the ways to use cloud firestore APIs

```kotlin
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

        //Get the document data to the model class in realtime
        val modelFormedRealtimeLiveData: LiveData<Any> =
            Firestore.getDocumentResultsIn<Any>("collectionId/documentId", true)
        //or
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
 
```

<h3>Cloud Storage</h3>
Here are the ways to use cloud storage APIs

```kotlin
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

```

<h3>Firebase Analytics</h3>
Here are the ways to add events

```kotlin
        //Log event to firebase by providing bundle
        logBundledEvent("eventName") {
            putString("name", "xyz")
            putString("info", "test")
        }

        //log event to firebase without bundle
        logEvent("eventName")

```
