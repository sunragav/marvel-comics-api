package com.sunragav.indiecampers.localdata.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sunragav.indiecampers.localdata.models.Favorites
import io.reactivex.Completable

@Dao
interface FavoriteComicsDAO {

    @Query("SELECT * FROM favorites LIMIT :limit")
    fun getFavoriteComicsList(limit: Int): DataSource.Factory<Int, Favorites>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(comicsList: List<Favorites>): Completable

    @Query("DELETE FROM favorites where id=:id")
    fun deleteFavorite(id: String): Completable

    @Query("DELETE FROM favorites")
    fun clearFavorites(): Completable
}