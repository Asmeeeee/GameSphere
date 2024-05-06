package com.example.soundwave.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.soundwave.model.Album

@Database(entities = [Album::class], version = 1, exportSchema = false)
abstract class AlbumDatabase : RoomDatabase() {

    abstract  fun albumDao(): AlbumDao

    companion object{
        @Volatile
        private var Instance: AlbumDatabase? = null

        fun getDatabase(context: Context): AlbumDatabase {
            return Instance ?: synchronized(this){
                Room.databaseBuilder(context, AlbumDatabase::class.java, "movie_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance= it }
            }
        }
    }
}