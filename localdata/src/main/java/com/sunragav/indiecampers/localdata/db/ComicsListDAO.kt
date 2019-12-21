package com.sunragav.indiecampers.localdata.db

import androidx.paging.DataSource
import androidx.room.*
import com.sunragav.indiecampers.localdata.models.ComicsLocal
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface ComicsListDAO {

    @Query(
        "SELECT * FROM comics where upper(title) like :searchKey"
    )
    fun getComicsList(searchKey: String): DataSource.Factory<Int, ComicsLocal>

    @Query("SELECT * FROM comics")
    fun getComicsList(): DataSource.Factory<Int, ComicsLocal>

    @Update
    fun update(comics: ComicsLocal)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(comicsList: List<ComicsLocal>): Completable

    @Query("SELECT * FROM comics WHERE id = :id")
    fun getComicsById(id: String): Observable<ComicsLocal>

    @Query("DELETE FROM comics")
    fun clearComicsFromTable()
}