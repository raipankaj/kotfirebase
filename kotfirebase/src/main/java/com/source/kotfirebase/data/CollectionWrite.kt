package com.source.kotfirebase.data

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

@Keep
data class CollectionWrite(
    val documentReference: DocumentReference? = null,
    val exception: Exception? = null
)