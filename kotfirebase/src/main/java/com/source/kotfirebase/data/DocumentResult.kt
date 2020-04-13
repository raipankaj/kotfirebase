package com.source.kotfirebase.data

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException

@Keep
data class DocumentResult(
    val documentSnapshot: DocumentSnapshot? = null,
    val firestoreException: FirebaseFirestoreException? = null,
    val exception: Exception? = null
)