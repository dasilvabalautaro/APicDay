package com.globalhiddenodds.apicday.repository

import androidx.lifecycle.LiveData
import androidx.room.*
import com.globalhiddenodds.apicday.database.Likes

@Dao
interface LikesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLike(likes: Likes)

    @Query("SELECT * FROM likes WHERE id = :id")
    suspend fun getLikesOfId(id: Int): List<Likes>

    @Update
    suspend fun updateLikes(likes: Likes)

    @Query("SELECT * FROM likes ORDER BY favorite DESC")
    fun getBestLikes(): LiveData<List<Likes>>
}