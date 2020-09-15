# kotfirebase
A wrapper over existing firebase APIs to reduce the developer effort in using them, currently it supports APIs for cloud firestore, cloud storage and firebase analytics

The objective of this library is to bring down APIs to a single line of code by using the kotlin features, under the hood everything remains same it's just a matter of fact that developer need not have to worry about them.

Following are the steps to add kotfirebase to your android project
1. Add it in your root build.gradle at the end of repositories
```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
2. Add the dependency
```groovy
dependencies {
	implementation 'com.github.raipankaj:kotfirebase:1.0.0'
}
```


<h3>Cloud Firestore</h3>
Here are the ways to use cloud firestore APIs

```kotlin
//Get single time result
val documentsLiveData: LiveData<DocumentResult> = KotFirestore.getFromDocument("collectionId/documentId")
//or get realtime update using following method
val documentsRealtimeLiveData: LiveData<DocumentResult> = KotFirestore.getFromDocument("collectionId/documentId", true)

//Get the document data to the model class
val modelFormedLiveData: LiveData<Any> = KotFirestore.getFromDocumentInto<Any>("collectionId/documentId")
//or get the document data to the model class in realtime
val modelFormedRealtimeLiveData: LiveData<Any> = KotFirestore.getFromDocumentInto<Any>("collectionId/documentId", true)

//Get the documents in collection
val collectionLiveData: LiveData<CollectionResult> = KotFirestore.getFromCollection("collectionId")
//or get the collection with realtime listener attached to it
val collectionRealtimeLiveData: LiveData<CollectionResult> = KotFirestore.getFromCollection("collectionId", true)

//Get the documents from collection into list of model
val modelFormedCollectionLiveData: LiveData<List<Any>> = KotFirestore.getFromCollectionInto<Any>("collectionId")
//or get the documents from collection into list of model with realtime listener
val modelFormedCollectionRealtimeLiveData: LiveData<List<Any>> = KotFirestore.getFromCollectionInto("collectionId", true)

//Add document to collection
val addToCollection: LiveData<CollectionWrite> = KotFirestore.addDocumentToCollection("collectionId", mapOf("one" to 1, "two" to 2))

//Write data to individual document based on document ID
val addToDocument: LiveData<DocumentWrite> = KotFirestore.addDocumentWithID("collectionId/documentId", mapOf("one" to 1, "two" to 2))

//Update document based on document ID
val updateDocument: LiveData<DocumentWrite> = KotFirestore.updateDocumentByID("collectionId/documentId", mapOf("one" to 1, "two" to 2))

//Delete document based on document ID
KotFirestore.deleteDocumentByID("collectionId/documentID")

//Using kotlin extension on string for single time result
val documentsKtxLiveData: LiveData<DocumentResult> = "collectionId/documentId".getFirebaseDocuments()

//Using kotlin extension on string for realtime result
val documentsKtxRealtimeLiveData: LiveData<DocumentResult> = "collectionId/documentId".getFirebaseDocuments(true)
//or get the document data to the model class
val modelFormedKtxLiveData: LiveData<Any> = "collectionId/documentId".getFirebaseDocumentsIn<Any>()

//Get the document data to the model class in realtime
val modelFormedKtxRealtimeLiveData: LiveData<Any> = "collectionId/documentId".getFirebaseDocumentsIn<Any>(true)

//Get documents in the collection using ktx
val collectionKtxLiveData: LiveData<CollectionResult> = "collectionId".getFirebaseCollection()
//or listen for the realtime update
val collectionRealtimeKtxLiveData: LiveData<CollectionResult> = "collectionId".getFirebaseCollection(true)
```

<h3>Cloud Storage</h3>
Here are the ways to use cloud storage APIs

```kotlin
//Upload file by providing file name, storage path and file path
val uploadFileLiveData: LiveData<StorageResult> = Storage.uploadFile("myimage.jpg","storagePath", "filePath")

//Upload file stream by providing file name, storage path and file path
val uploadFileStreamLiveData: LiveData<StorageResult> = Storage.uploadFileStream("myimage.jpg","storagePath", "filePath")

//Upload bitmap by providing file name, storage path and bitmap
val uploadBitmapLiveData: LiveData<StorageResult> = Storage.uploadBitmap("myimage.jpg","storagePath", bitmap)

//Get the download url for the multimedia
val getDownloadUrlLiveData: LiveData<StorageDownloadResult> = Storage.getDownloadUrl("myimage.jpg", "storagePath")

//Download into another file
val downloadFileLiveData: LiveData<StorageDownloadResult> = Storage.downloadFile("myimage.jpg", "storagePath", file)

//Download file into local memory
val ONE_MEGABYTE: Long = 1024 * 1024
val downloadTempStorageLiveData: LiveData<StorageDownloadResult> = Storage.downloadInMemory("myimage.jpg", "storagePath", ONE_MEGABYTE)

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

<h3>Remote Config</h3>
Update app behaviour without updating app on playstore using the remote config

```kotlin
//Initiate the remote config to provide the default value and to change other params
RemoteConfig.initRemoteConfig(R.xml.remote_config_defaults)

//Fetch and activate to update the remote config values in realtime
val isFetchedLiveData: LiveData<RemoteConfigResult> = RemoteConfig.fetchAndShow()

//Just fetch but do not update the remote config values
RemoteConfig.justFetch()

//Activate the fetched result so as to get latest remote config values
val isFetchedResultActivated: LiveData<RemoteConfigResult> = RemoteConfig.activateFetchedResults()

//Get the value from remote config using these predefined methods
RemoteConfig.getRemoteBoolean("is_update_available")
RemoteConfig.getRemoteDouble("current_version")
RemoteConfig.getRemoteLong("last_updated_timestamp")
RemoteConfig.getRemoteString("app_theme_name")
```
