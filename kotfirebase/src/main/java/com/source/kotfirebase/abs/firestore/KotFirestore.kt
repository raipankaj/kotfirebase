package com.source.kotfirebase.abs.firestore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.*
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
     * Return a well formed model based on the provided model type.
     */
    inline fun <reified T> getFromCollectionInto(
        collectionID: String,
        syncRealtime: Boolean = false,
        noinline queryFunc: (Query.() -> Query)? = null
    ): LiveData<List<T>> {

        val mutableLiveData = MutableLiveData<List<T>>()

        if (syncRealtime) {
            getCollection(collectionID, queryFunc).addSnapshotListener { value, error ->
                mutableLiveData.postValue(value?.toObjects(T::class.java))
            }
        } else {
            getCollection(collectionID, queryFunc).get().addOnSuccessListener {
                mutableLiveData.postValue(it?.toObjects(T::class.java))
            }
        }

        return mutableLiveData
    }

    fun getCollection(
        collectionID: String,
        queryFunc: (Query.() -> Query)?
    ): Query {
        return if (queryFunc != null) {
            firestore.collection(collectionID).queryFunc()
        } else {
            firestore.collection(collectionID)
        }
    }

    fun getDocument(documentID: String): DocumentReference {
        return firestore.document(documentID)
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
        collectionID: String,
        syncRealtime: Boolean = false,
        queryFunc: (Query.() -> Query)? = null
    ): LiveData<CollectionResult> {

        val collectionLiveData = MutableLiveData<CollectionResult>()

        if (syncRealtime) {
            getCollection(collectionID, queryFunc)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    val collectionResult = if (firebaseFirestoreException == null) {
                        CollectionResult(querySnapshot = querySnapshot)
                    } else {
                        CollectionResult(firestoreException = firebaseFirestoreException)
                    }
                    collectionLiveData.postValue(collectionResult)
                }
        } else {
            getCollection(collectionID, queryFunc).get()
                .addOnSuccessListener {
                    collectionLiveData.postValue(CollectionResult(querySnapshot = it))
                }.addOnFailureListener {
                    collectionLiveData.postValue(CollectionResult(exception = it))
                }
        }

        return collectionLiveData
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
        documentID: String,
        syncRealtime: Boolean = false
    ): LiveData<DocumentResult> {

        val documentLiveData = MutableLiveData<DocumentResult>()

        if (syncRealtime) {
            getDocument(documentID)
                .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                    val documentResult = if (firebaseFirestoreException == null) {
                        DocumentResult(documentSnapshot = documentSnapshot)
                    } else {
                        DocumentResult(firestoreException = firebaseFirestoreException)
                    }

                    documentLiveData.postValue(documentResult)
                }
        } else {
            getDocument(documentID)
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
     * Get the document from the collection based on pre-defined model.
     * Pass the data class type while calling getDocumentResultsIn()
     */
    inline fun <reified T> getFromDocumentInto(
        documentID: String,
        syncRealtime: Boolean = false
    ): LiveData<T> {
        val mutableLiveData = MutableLiveData<T>()

        if (syncRealtime) {
            getDocument(documentID)
                .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                    mutableLiveData.postValue(documentSnapshot?.toObject(T::class.java))
                }
        } else {
            getDocument(documentID)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    mutableLiveData.postValue(documentSnapshot?.toObject(T::class.java))
                }
        }

        return mutableLiveData
    }

    fun addDocumentToCollection(collectionID: String, any: Any): LiveData<CollectionWrite> {
        val mutableLiveData = MutableLiveData<CollectionWrite>()

        firestore.collection(collectionID)
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
    fun addDocumentWithID(
        documentID: String,
        any: Any,
        setOptions: SetOptions? = null
    ): LiveData<DocumentWrite> {

        val mutableLiveData = MutableLiveData<DocumentWrite>()

        if (setOptions == null) {
            firestore.document(documentID).set(any)
        } else {
            firestore.document(documentID).set(any, setOptions)
        }.addOnSuccessListener {
            mutableLiveData.postValue(DocumentWrite(true))
        }.addOnFailureListener {
            mutableLiveData.postValue(DocumentWrite(exception = it))
        }

        return mutableLiveData
    }

    /**
     * Update the individual field of the document by passing them as
     * a map of key, value pair.
     */
    fun updateDocumentByID(documentID: String, map: Map<String, Any>): LiveData<DocumentWrite> {
        val mutableLiveData = MutableLiveData<DocumentWrite>()

        firestore.document(documentID)
            .update(map)
            .addOnSuccessListener {
                mutableLiveData.postValue(DocumentWrite(true))
            }.addOnFailureListener {
                mutableLiveData.postValue(DocumentWrite(exception = it))
            }

        return mutableLiveData
    }

    /**
     * Delete the document in the collection by passing document ID
     */
    fun deleteDocumentByID(documentID: String) {
        firestore.document(documentID)
            .delete()
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