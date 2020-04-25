package com.source.kotfirebase.abs.storage

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.source.kotfirebase.data.OnUploadSuccess
import com.source.kotfirebase.data.StorageResult
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

object Storage : StorageServices {

    /**
     * Upload bitmap by providing the name of the file and storage bucket.
     * In case of storage bucket is empty than it will store image directly in
     * the root bucket.
     */
    override fun uploadBitmap(fileName: String, storagePath: String, bitmap: Bitmap): LiveData<StorageResult> {

        val storageMutableLiveData = MutableLiveData<StorageResult>()

        performNetworkCall {

            reference.child(storagePath.plus("/").plus(fileName)).putBytes(bitmap.getBytes())
                .addOnSuccessListener {
                    formData(it, storageMutableLiveData)
                }.addOnFailureListener {
                    storageMutableLiveData.postValue(StorageResult(exception = it))
                }
        }

        return storageMutableLiveData
    }

    /**
     * Upload filestream by providing the name of the file and storage bucket.
     * In case of storage bucket is empty than it will store image directly in
     * the root bucket.
     */
    override fun uploadFileStream(fileName: String, storagePath: String, filePath: String): LiveData<StorageResult> {

        val stream: InputStream = FileInputStream(File(filePath))
        val storageMutableLiveData = MutableLiveData<StorageResult>()

        performNetworkCall {
            reference.child(storagePath.plus("/").plus(fileName)).putStream(stream)
                .addOnSuccessListener {
                    formData(it, storageMutableLiveData)
                }.addOnFailureListener {
                    storageMutableLiveData.postValue(StorageResult(exception = it))
                }
        }

        return storageMutableLiveData
    }

    /**
     * Upload file by providing the name of the file and storage bucket.
     * In case of storage bucket is empty than it will store image directly in
     * the root bucket.
     */
    override fun uploadFile(fileName: String, storagePath: String, filePath: String): LiveData<StorageResult> {

        val file: Uri = Uri.fromFile(File(filePath))
        val storageMutableLiveData = MutableLiveData<StorageResult>()

        performNetworkCall {
            reference.child(storagePath.plus("/").plus(fileName)).putFile(file)
                .addOnSuccessListener {
                    formData(it, storageMutableLiveData)
                }.addOnFailureListener {
                    storageMutableLiveData.postValue(StorageResult(exception = it))
                }
        }

        return storageMutableLiveData
    }

    private fun formData(
        it: UploadTask.TaskSnapshot,
        storageMutableLiveData: MutableLiveData<StorageResult>
    ) {
        val upload = OnUploadSuccess(
            it.bytesTransferred,
            it.metadata,
            it.totalByteCount,
            it.uploadSessionUri
        )
        storageMutableLiveData.postValue(StorageResult(upload))
    }

    private fun Bitmap.getBytes(): ByteArray {
        val baos = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return baos.toByteArray()
    }

    private inline fun performNetworkCall(storageFun: FirebaseStorage.() -> Unit) {
        val firebaseStorage = FirebaseStorage.getInstance()
        storageFun(firebaseStorage)
    }
}