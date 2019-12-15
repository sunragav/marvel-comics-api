package com.sunragav.indiecampers.home.presentation.models

data class Comics(
    val id: String,
    val title: String,
    val description: String,
    val thumbNail: String,
    val imageUrls: List<String>,
    val flagged: Boolean
)