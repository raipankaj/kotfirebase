package com.source.kotfirebase.abs.firestore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.source.kotfirebase.data.CollectionResult
import com.source.kotfirebase.data.CollectionWrite
import com.source.kotfirebase.data.DocumentResult
import com.source.kotfirebase.data.DocumentWrite

object KotFirestore {

    val firestore = FirebaseFirestore.getInstance()

    /**
     * Based on the collection id get all the documents in that
     * also if syncRealtime is enabled then listen for the real-
     * time changes.
     * Return the well formed model based on the provided model type.
     */
    inline fun <reified T> getFromCollectionInto(
        collectionPath: String,
        syncRealtime: Boolean = false,
        noinline queryFunc: (Query.() -> Query)? = null
    ): LiveData<List<T>> {

        val mutableLiveData = MutableLiveData<List<T>>()

        if (syncRealtime) {
            if (queryFunc != null) {
                firestore.collection(collectionPath).queryFunc()
                    .addSnapshotListener { value, error ->
                        mutableLiveData.postValue(value?.toObjects(T::class.java))
                    }
            } else {
                firestore.collection(collectionPath)
                    .addSnapshotListener { value, error ->
                        mutableLiveData.postValue(value?.toObjects(T::class.java))
                    }
            }
        } else {
            if (queryFunc != null) {
                firestore.collection(collectionPath).queryFunc()
                    .get()
                    .addOnSuccessListener {
                        mutableLiveData.postValue(it.toObjects(T::class.java))
                    }
            } else {
                firestore.collection(collectionPath)
                    .get()
                    .addOnSuccessListener {
                        mutableLiveData.postValue(it.toObjects(T::class.java))
                    }
            }
        }

        return mutableLiveData
    }

    /**
     * Perform the network using the firebase existing API to fetch result
     * from the document and return the result as a live data.
     * In case syncRealtime is true than set the realtime listener and post
     * the result through live data. In case activity or fragment is not in
     * onResume state then live data will take care of storing result but not
     * delivering it until they are in onResume state once again.
     */
    fun getFromDocument(
        document: String,
        syncRealtime: Boolean = false
    ): LiveData<DocumentResult> {

        val documentLiveData = MutableLiveData<DocumentResult>()

        if (syncRealtime) {
            firestore.document(document)
                .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->

                    val documentResult = if (firebaseFirestoreException == null) {
                        DocumentResult(documentSnapshot = documentSnapshot)
                    } else {
                        DocumentResult(firestoreException = firebaseFirestoreException)
                    }

                    documentLiveData.postValue(documentResult)
                }
        } else {
            firestore.document(document)
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
    fun getFromCollection(
        collection: String,
        syncRealtime: Boolean = false
    ): LiveData<CollectionResult> {

        val collectionLiveData = MutableLiveData<CollectionResult>()
        if (syncRealtime) {
            firestore.collection(collection)
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
            firestore.collection(collection)
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

        return collectionLiveData
    }


    fun addToCollection(collection: String, any: Any): LiveData<CollectionWrite> {
        val mutableLiveData = MutableLiveData<CollectionWrite>()

        firestore.collection(collection)
            .add(any)
            .addOnSuccessListener {
                mutableLiveData.postValue(CollectionWrite(it))
            }.addOnFailureListener {
                mutableLiveData.postValue(CollectionWrite(exception = it))
            }

        return mutableLiveData
    }

    /**
     * Add the document based on the document id, SetOption is by default set to
     * null, that means new values are written in the document every time.
     */
    fun addToDocument(
        document: String,
        any: Any,
        setOptions: SetOptions?
    ): LiveData<DocumentWrite> {

        val mutableLiveData = MutableLiveData<DocumentWrite>()

        if (setOptions == null) {
            firestore.document(document)
                .set(any)
        } else {
            firestore.document(document)
                .set(any, setOptions)
        }.addOnSuccessListener {
            mutableLiveData.postValue(DocumentWrite(true))
        }.addOnFailureListener {
            mutableLiveData.postValue(DocumentWrite(exception = it))
        }

        return mutableLiveData
    }

    /**
     * Get the document from the collection based on pre-defined model.
     * Pass the data class type while calling getDocumentResultsIn()
     */
    inline fun <reified T> getDocumentResultsIn(
        document: String,
        sync: Boolean = false
    ): LiveData<T> {
        val mutableLiveData = MutableLiveData<T>()

        if (sync) {
            firestore.document(document)
                .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                    val t = documentSnapshot?.toObject(T::class.java)
                    mutableLiveData.postValue(t)
                }
        } else {
            firestore.document(document)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val t = documentSnapshot?.toObject(T::class.java)
                    mutableLiveData.postValue(t)
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

}