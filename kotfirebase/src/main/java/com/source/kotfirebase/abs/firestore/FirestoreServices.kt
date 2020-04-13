package com.source.kotfirebase.abs.firestore

import androidx.lifecycle.LiveData
import com.source.kotfirebase.data.CollectionResult
import com.source.kotfirebase.data.DocumentResult

interface FirestoreServices {
    fun getFromDocument(document: String, syncRealtime: Boolean = false): LiveData<DocumentResult>
    fun getFromCollection(collection: String, syncRealtime: Boolean = false): LiveData<CollectionResult>
}