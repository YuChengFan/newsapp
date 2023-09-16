package com.androiddevs.mvvmnewsapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.androiddevs.mvvmnewsapp.model.Article

@Dao
interface ArticleDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long //return ids that inserted

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>   //因為和livedata結合，所以不能寫成suspend function

    @Delete
    suspend fun delete(article: Article)
}