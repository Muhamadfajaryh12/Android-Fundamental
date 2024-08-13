package com.example.submissionandroidfundamental.data.retrofit
import com.example.submissionandroidfundamental.data.response.ItemsItem
import com.example.submissionandroidfundamental.data.response.ResponseDetailGithub
import retrofit2.Call
import retrofit2.http.*
import com.example.submissionandroidfundamental.data.response.ResponseSearchGithub

interface ApiService {
    @GET("search/users")
    fun searchUser(@Query("q") q:String):Call <ResponseSearchGithub>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") query:String):Call<ResponseDetailGithub>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String):Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String):Call<List<ItemsItem>>
}