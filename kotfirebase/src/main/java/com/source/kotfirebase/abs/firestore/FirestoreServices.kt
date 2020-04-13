package com.source.kotfirebase.abs.firestore

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.SetOptions
import com.source.kotfirebase.data.CollectionResult
import com.source.kotfirebase.data.CollectionWrite
import com.source.kotfirebase.data.DocumentResult
import com.source.kotfirebase.data.DocumentWrite

interface FirestoreServices {
    fun getFromDocument(document: String, syncRealtime: Boolean = false): LiveData<DocumentResult>
    fun getFromCollection(collection: String, syncRealtime: Boolean = false): LiveData<CollectionResult>

    fun addToCollection(collection: String, any: Any): LiveData<CollectionWrite>
    fun addToDocument(document: String, any: Any, setOptions: SetOptions? = null): LiveData<DocumentWrite>
}