package com.example.submissionandroidfundamental.ui.model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.submissionandroidfundamental.database.Favorite
import com.example.submissionandroidfundamental.repository.FavoriteRepository

class FavoriteViewModel (application: Application): ViewModel() {
    private val mFavoriteRepository:FavoriteRepository = FavoriteRepository(application)

    fun getAllFavorites():LiveData<List<Favorite>> = mFavoriteRepository.getAllFavorites()

}