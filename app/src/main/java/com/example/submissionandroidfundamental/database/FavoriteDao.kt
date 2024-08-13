package com.example.submissionandroidfundamental.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favorite:Favorite)

    @Delete
    fun delete(favorite:Favorite)

    @Query("SELECT * FROM Favorite")
    fun getAllFavorites():LiveData<List<Favorite>>

    @Query("SELECT * FROM Favorite WHERE username = :username" )
    fun getFavoriteByUsername(username:String):LiveData<Favorite>
}