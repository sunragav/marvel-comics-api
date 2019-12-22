package com.sunragav.indiecampers.localdata.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sunragav.indiecampers.localdata.models.Request

@Dao
interface RequestTrackerDao {
    @Query("SELECT * FROM request")
    fun getRequest(): Request

    @Query("DELETE FROM request")
    fun delete()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(request: Request)
}
