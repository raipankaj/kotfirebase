@file:JvmName("FirebaseExt")
package com.source.kotfirebase

import android.content.Context
import android.os.Bundle
import com.source.kotfirebase.abs.analytics.Analytics
import com.source.kotfirebase.abs.firestore.KotFirestore

fun String.getFirebaseCollection(sync: Boolean = false) = KotFirestore.getFromCollection(this, sync)

fun String.getFirebaseDocuments(sync: Boolean = false) = KotFirestore.getFromDocument(this, sync)

inline fun <reified T> String.getFirebaseDocumentsIn(sync: Boolean = false) =
    KotFirestore.getFromDocumentInto<T>(this, sync)

fun Context.logBundledEvent(name: String, analyticsFunc: Bundle.() -> Unit) {
    val bundle = Bundle()
    analyticsFunc(bundle)
    Analytics.logBundledEvent(this, name, bundle)
}

fun Context.logEvent(name: String) {
    Analytics.logEvent(this, name)
}