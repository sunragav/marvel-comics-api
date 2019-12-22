package com.sunragav.indiecampers.home.data.models

data class ComicsData(
    val id: String,
    val title: String,
    val description: String,
    val thumbNail: String,
    val imageUrls: List<String>,
    val flagged: Boolean
)