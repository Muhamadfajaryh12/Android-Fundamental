package com.example.submissionandroidfundamental.ui.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.submissionandroidfundamental.data.response.ItemsItem
import com.example.submissionandroidfundamental.data.response.ResponseDetailGithub
import com.example.submissionandroidfundamental.data.retrofit.ApiConfig
import com.example.submissionandroidfundamental.database.Favorite
import com.example.submissionandroidfundamental.repository.FavoriteRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class DetailViewModel(application: Application) : AndroidViewModel(application){
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    private  val _username = MutableLiveData<ResponseDetailGithub>()
    val username : LiveData<ResponseDetailGithub> = _username

    private val _followers = MutableLiveData<List<ItemsItem>>()
    val followers : LiveData<List<ItemsItem>> = _followers

    private val _following = MutableLiveData<List<ItemsItem>>()
    val following : LiveData<List<ItemsItem>> = _following

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage : LiveData<String> =_errorMessage

    companion object{
        private const val TAG="DetailViewModel"
    }

    fun detailUser(username:String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<ResponseDetailGithub>{
            override fun onResponse(
                call: Call<ResponseDetailGithub>,
                response: Response<ResponseDetailGithub>
            ){
                _isLoading.value=false
                if(response.isSuccessful){
                    _username.value = response.body()
                }else {
                    Log.e(TAG, "onSucces: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<ResponseDetailGithub>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value= "Terjadi Kesalahan: ${t.message}"
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun followerUser(username:String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object:Callback<List<ItemsItem>>{
            override  fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ){
                _isLoading.value=false
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _followers.value = responseBody
                    }
                } else {
                    Log.e(TAG,"onSuccess:${response.message()}")
                }
            }
            override fun onFailure(call:Call<List<ItemsItem>>, t:Throwable){
                _isLoading.value=false
                _errorMessage.value= "Terjadi Kesalahan: ${t.message}"
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun followingUser(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object:Callback<List<ItemsItem>>{
            override  fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ){
                _isLoading.value=false
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _following.value = responseBody
                    }
                }else{
                    Log.e(TAG,"onSuccess:${response.message()}")
                }
            }

            override fun onFailure(call:Call<List<ItemsItem>>, t:Throwable){
                _isLoading.value=false
                _errorMessage.value= "Terjadi Kesalahan: ${t.message}"
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun setFavorite(favorite: Favorite){
        viewModelScope.launch {
            mFavoriteRepository.insert(favorite)
        }
    }

    fun deleteFavorite(favorite: Favorite){
        viewModelScope.launch {
            mFavoriteRepository.delete(favorite)
        }
    }

    fun checkFavorited(username: String): LiveData<Favorite> {
        return mFavoriteRepository.getFavoriteByUsername(username)
    }
}