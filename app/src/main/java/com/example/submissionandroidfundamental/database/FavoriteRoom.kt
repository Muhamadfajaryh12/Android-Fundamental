package com.example.submissionandroidfundamental.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Favorite::class], version = 1)
abstract class FavoriteRoom : RoomDatabase() {
    abstract fun favoriteDao() : FavoriteDao
    companion object{
        @Volatile
        private var INSTANCE: FavoriteRoom? = null
        @JvmStatic
        fun getDatabase(context:Context):FavoriteRoom{
            if(INSTANCE == null){
                synchronized(FavoriteRoom::class.java){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        FavoriteRoom::class.java, "FavDatabase")
                        .build()
                }
            }
            return INSTANCE as FavoriteRoom
        }
    }
}