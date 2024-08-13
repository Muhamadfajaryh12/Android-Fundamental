package com.example.submissionandroidfundamental.ui.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submissionandroidfundamental.data.response.ItemsItem
import com.example.submissionandroidfundamental.data.response.ResponseSearchGithub
import com.example.submissionandroidfundamental.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _users = MutableLiveData<List<ItemsItem>>()
    val users: LiveData<List<ItemsItem>> = _users

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage : LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    var ApiService = ApiConfig.getApiService()

    companion object{
        fun findSearch(query: String) {

        }

        private const val TAG = "MainViewModel"
        private const val query = "fajar"
    }
    init {
        findSearch(query)
    }
    fun findSearch(query:String){
        _isLoading.value=true
        val client = ApiService.searchUser(query)
        client.enqueue(object : Callback<ResponseSearchGithub> {
            override fun onResponse(
                call: Call<ResponseSearchGithub>,
                response: Response<ResponseSearchGithub>
            ){
                _isLoading.value=false
                if(response.isSuccessful){
                    _users.value = response.body()?.items
                }else {
                    Log.e(TAG, "onSucces: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<ResponseSearchGithub>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value= "Terjadi Kesalahan: ${t.message}"
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}