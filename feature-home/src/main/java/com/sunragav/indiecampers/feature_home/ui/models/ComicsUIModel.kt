package com.sunragav.indiecampers.feature_home.ui.models

data class ComicsUIModel(
    var id: String,
    var title: String,
    var description: String,
    var thumbNail: String,
    var imageUrls: List<String>,
    var flagged: Boolean
)