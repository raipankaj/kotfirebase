package com.source.kotfirebase.abs.firestore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.SetOptions
import com.source.kotfirebase.data.CollectionResult
import com.source.kotfirebase.data.CollectionWrite
import com.source.kotfirebase.data.DocumentResult
import com.source.kotfirebase.data.DocumentWrite

object Firestore : FirestoreServices {

    val firestore = FirebaseFirestore.getInstance()

    /**
     * Perform the network using the firebase existing API to fetch result
     * from the document and return the result as a live data.
     * In case syncRealtime is true than set the realtime listener and post
     * the result through live data. In case activity or fragment is not in
     * onResume state then live data will take care of storing result but not
     * delivering it until they are in onResume state once again.
     */
    override fun getFromDocument(
        document: String,
        syncRealtime: Boolean
    ): LiveData<DocumentResult> {

        val documentLiveData = MutableLiveData<DocumentResult>()

        performNetworkCall {
            if (syncRealtime) {
                document(document)
                    .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->

                        val documentResult = if (firebaseFirestoreException == null) {
                            DocumentResult(documentSnapshot = documentSnapshot)
                        } else {
                            DocumentResult(firestoreException = firebaseFirestoreException)
                        }

                        documentLiveData.postValue(documentResult)
                    }
            } else {
                document(document)
                    .get()
                    .addOnSuccessListener {
                        val documentResult = DocumentResult(documentSnapshot = it)
                        documentLiveData.postValue(documentResult)
                    }
                    .addOnFailureListener {
                        val documentResult = DocumentResult(exception = it)
                        documentLiveData.postValue(documentResult)
                    }
            }

        }

        return documentLiveData
    }


    /**
     * Perform the network using the firebase existing API to fetch result
     * from the collection and return the result as a live data.
     * In case syncRealtime is true than set the realtime listener and post
     * the result through live data. In case activity or fragment is not in
     * onResume state then live data will take care of storing result but not
     * delivering it until they are in onResume state once again.
     */
    override fun getFromCollection(
        collection: String,
        syncRealtime: Boolean
    ): LiveData<CollectionResult> {

        val collectionLiveData = MutableLiveData<CollectionResult>()

        performNetworkCall {
            if (syncRealtime) {
                collection(collection)
                    .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                        val collectionResult = if (firebaseFirestoreException == null) {
                            CollectionResult(querySnapshot = querySnapshot)
                        } else {
                            CollectionResult(firestoreException = firebaseFirestoreException)
                        }

                        /*Post the collection result which either have querySnapshot
                         or firestore exception*/
                        collectionLiveData.postValue(collectionResult)
                    }
            } else {
                collection(collection)
                    .get()
                    .addOnSuccessListener {
                        val collectionResult = CollectionResult(querySnapshot = it)
                        //Post the collection result which will have querySnapshot
                        collectionLiveData.postValue(collectionResult)
                    }
                    .addOnFailureListener {
                        //Post the collection result which will have exception
                        val collectionResult = CollectionResult(exception = it)
                        collectionLiveData.postValue(collectionResult)
                    }
            }
        }

        return collectionLiveData
    }

    override fun addToCollection(collection: String, any: Any): LiveData<CollectionWrite> {
        val mutableLiveData = MutableLiveData<CollectionWrite>()

        performNetworkCall {
            collection(collection)
                .add(any)
                .addOnSuccessListener {
                    mutableLiveData.postValue(CollectionWrite(it))
                }.addOnFailureListener {
                    mutableLiveData.postValue(CollectionWrite(exception = it))
                }
        }

        return mutableLiveData
    }

    override fun addToDocument(
        document: String,
        any: Any,
        setOptions: SetOptions?
    ): LiveData<DocumentWrite> {

        val mutableLiveData = MutableLiveData<DocumentWrite>()

        performNetworkCall {
            if (setOptions == null) {
                document(document)
                    .set(any)
            } else {
                document(document)
                    .set(any, setOptions)
            }.addOnSuccessListener {
                mutableLiveData.postValue(DocumentWrite(true))
            }.addOnFailureListener {
                mutableLiveData.postValue(DocumentWrite(exception = it))
            }
        }

        return mutableLiveData
    }

    inline fun <reified T> getDocumentResultsIn(
        document: String,
        sync: Boolean = false
    ): LiveData<T> {
        val mutableLiveData = MutableLiveData<T>()

        performNetworkCall {
            if (sync) {
                document(document)
                    .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                        val t = documentSnapshot?.toObject(T::class.java)
                        mutableLiveData.postValue(t)
                    }
            } else {
                document(document)
                    .get()
                    .addOnSuccessListener { documentSnapshot ->
                        val t = documentSnapshot?.toObject(T::class.java)
                        mutableLiveData.postValue(t)
                    }
            }
        }

        return mutableLiveData
    }

    /**
     * Update the firestore settings based before using cloud firestore
     * where isPersistenceEnabled and isSLLEnabled is set to true by default.
     */
    fun updateFirestoreSettings(
        host: String? = null,
        cacheSizeInBytes: Long? = null,
        isPersistenceEnabled: Boolean = true,
        isSLLEnabled: Boolean = true
    ) {
        val builder = FirebaseFirestoreSettings.Builder()

        host?.let {
            builder.setHost(it)
        }

        cacheSizeInBytes?.let {
            builder.setCacheSizeBytes(it)
        }

        builder.setPersistenceEnabled(isPersistenceEnabled)
        builder.setSslEnabled(isSLLEnabled)
    }


    /**
     * A utility method to perform firestore APIs call without need to write
     * getInstance() over and over with an added advantage of perform certain
     * activity based on if-else condition.
     */
    inline fun performNetworkCall(firebaseFunc: FirebaseFirestore.() -> Unit) {
        firebaseFunc(firestore)
    }
}