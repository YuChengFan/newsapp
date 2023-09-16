package com.androiddevs.mvvmnewsapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

//tells android this is a table in our class
@Entity(
    tableName = "articles"
)

data class Article(
    @PrimaryKey(autoGenerate = true)  //會自動建立這個id
    var id: Int? = 0, //不一定每一個文章都會存在database
    val author: String = "",
//    val content: String = "",
    val description: String = "",
    val publishedAt: String = "",
    val source: Source,
    val title: String = "",
    val url: String = "",
    val urlToImage: String = ""
) : Serializable