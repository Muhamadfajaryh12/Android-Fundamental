package com.example.submissionandroidfundamental.data.response

import com.google.gson.annotations.SerializedName

data class ResponseSearchGithub(

	@field:SerializedName("items")
    var items: List<ItemsItem>
)

data class ItemsItem(

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

)
