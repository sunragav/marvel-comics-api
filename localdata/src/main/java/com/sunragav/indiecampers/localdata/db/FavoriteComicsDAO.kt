package com.sunragav.indiecampers.localdata.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sunragav.indiecampers.localdata.models.Favorites

@Dao
interface FavoriteComicsDAO {

    @Query("SELECT * FROM favorites")
    fun getFavoriteComicsList(): DataSource.Factory<Int, Favorites>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(comicsList: List<Favorites>)

    @Query("DELETE FROM favorites where id=:id")
    fun deleteFavorite(id: String)

    @Query("DELETE FROM favorites")
    fun clearFavorites()
}