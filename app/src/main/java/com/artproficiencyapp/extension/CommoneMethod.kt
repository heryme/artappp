package com.artproficiencyapp.extension

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

fun getRequestBody(value: String): RequestBody = RequestBody.create(MediaType.parse("text/plain"), value)

fun getImageRequestBody(path: String, key: String): MultipartBody.Part? {// create images file  in  MultipartBody.Part
    val file = File(path)
    val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
    return MultipartBody.Part.createFormData(key, file.name, requestFile)
}

fun getCommaSeparatorString(data: String): String {
    var tags: String = ""
    val items = data.split("#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    for (item in items) {
        tags = if (tags.isEmpty()) {
            item
        } else {
            tags + "#" + item
        }
    }
    return tags
}

fun isLastVisible(recyclerView: RecyclerView): Boolean {
    val layoutManger = recyclerView.layoutManager as LinearLayoutManager
    val pos = layoutManger.findLastCompletelyVisibleItemPosition()
    val numItems = recyclerView.adapter.itemCount
    return pos >= numItems - 1

}