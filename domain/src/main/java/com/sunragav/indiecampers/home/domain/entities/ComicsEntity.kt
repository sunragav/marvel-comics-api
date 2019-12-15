package com.sunragav.indiecampers.home.domain.entities

data class ComicsEntity(
    val id: String,
    val title: String,
    val description: String,
    val thumbNail: String,
    val imageUrls: List<String>,
    val flagged: Boolean
)