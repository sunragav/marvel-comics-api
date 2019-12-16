package com.sunragav.indiecampers.localdata.db

import android.content.Context
import androidx.annotation.NonNull
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sunragav.indiecampers.localdata.models.ComicsLocal
import com.sunragav.indiecampers.localdata.models.Favorites
import com.sunragav.indiecampers.localdata.utils.StringConverter

@Database(
    entities = [ComicsLocal::class, Favorites::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(StringConverter::class)
abstract class ComicsDB : RoomDatabase() {

    companion object {
        private val LOCK = Any()
        private const val DATABASE_NAME = "comics.db"
        @Volatile
        private var INSTANCE: ComicsDB? = null

        fun getInstance(@NonNull context: Context): ComicsDB {
            if (INSTANCE == null) {
                synchronized(LOCK) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context,
                            ComicsDB::class.java,
                            DATABASE_NAME
                        ).build()
                    }
                }
            }
            return INSTANCE!!
        }
    }

    abstract fun getComisListDao(): ComicsListDAO

    abstract fun getFavoritesDao(): FavoriteComicsDAO

}