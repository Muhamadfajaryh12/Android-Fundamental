package com.example.submissionandroidfundamental.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.submissionandroidfundamental.database.Favorite
import com.example.submissionandroidfundamental.database.FavoriteDao
import com.example.submissionandroidfundamental.database.FavoriteRoom
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoriteDao : FavoriteDao
    private val executorService:ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteRoom.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }

    fun getAllFavorites(): LiveData<List<Favorite>> = mFavoriteDao.getAllFavorites()

    fun getFavoriteByUsername(username: String): LiveData<Favorite> =
        mFavoriteDao.getFavoriteByUsername(username)

    fun insert(favorite: Favorite){
        executorService.execute { mFavoriteDao.insert(favorite) }
    }
    fun delete(favorite: Favorite){
        executorService.execute { mFavoriteDao.delete(favorite) }
    }

}