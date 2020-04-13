package com.source.kotfirebase.data

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException

@Keep
data class DocumentWrite(
    val isSuccessful: Boolean = false,
    val exception: Exception? = null
)