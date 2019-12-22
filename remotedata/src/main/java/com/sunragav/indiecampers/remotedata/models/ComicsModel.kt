package com.sunragav.indiecampers.remotedata.models

data class Comic(
    val id: Int,
    val title: String,
    val description: String?,
    val thumbnail: Image,
    val images: List<Image>
)

data class DataContainer<out T>(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: T
)

data class DataWrapper<out T>(
    val code: Int,
    val status: String,
    val copyRight: String,
    val attributionText: String,
    val attributionHTML: String,
    val etag: String,
    val data: DataContainer<T>
)

data class Image(
    val path: String,
    val extension: String
)
