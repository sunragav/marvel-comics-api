package com.sunragav.indiecampers.localdata.db

import androidx.paging.DataSource
import androidx.room.*
import com.sunragav.indiecampers.localdata.models.ComicsLocal
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface ComicsListDAO {

    @Query(
        "SELECT * FROM comics where (title like :searchKey) or (id LIKE :searchKey) " +
                "or (description LIKE :searchKey) ORDER BY id DESC LIMIT :limit"
    )
    fun getComicsList(searchKey: String, limit: Int): DataSource.Factory<Int, ComicsLocal>

    @Query("SELECT * FROM comics LIMIT :limit")
    fun getComicsList(limit: Int): DataSource.Factory<Int, ComicsLocal>

    @Update
    fun update(comics: ComicsLocal): Completable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(comicsList: List<ComicsLocal>): Completable

    @Query("SELECT * FROM comics WHERE id = :id")
    fun getComicsById(id: String): Observable<ComicsLocal>

    @Query("DELETE FROM comics")
    fun clearComicsTable(): Completable
}