package com.example.submissionandroidfundamental.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName="favorite")
data class Favorite(
    @PrimaryKey(autoGenerate = false)

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "avatarUrl")
    val avatarUrl: String? = null

)
