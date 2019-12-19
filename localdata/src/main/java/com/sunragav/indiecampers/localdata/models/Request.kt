package com.sunragav.indiecampers.localdata.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "request")
data class Request(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "query") val searchKey: String,
    @ColumnInfo(name = "offset") val offset: Int,
    @ColumnInfo(name = "is_flagged") val flagged: Boolean
)
