package com.sunragav.indiecampers.localdata.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comics")
data class ComicsLocal(
    @PrimaryKey(autoGenerate = true) val i: Int = 0,
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "thumbNail") val thumbNail: String,
    @ColumnInfo(name = "imageUrls") val imageUrls: List<String>,
    @ColumnInfo(name = "is_flagged") val flagged: Boolean
)
