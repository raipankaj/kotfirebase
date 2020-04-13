package com.source.kotfirebase.data

import androidx.annotation.Keep
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

@Keep
data class CollectionResult(
    val querySnapshot: QuerySnapshot? = null,
    val firestoreException: FirebaseFirestoreException? = null,
    val exception: Exception? = null
)