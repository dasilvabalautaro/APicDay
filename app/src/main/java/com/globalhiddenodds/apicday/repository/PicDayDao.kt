package com.globalhiddenodds.apicday.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.globalhiddenodds.apicday.datasource.database.data.PicDay

@Dao
interface PicDayDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(picday: PicDay)

    @Query("SELECT * FROM picday WHERE date LIKE :date")
    fun getPicDay(date: String): PicDay

    @Query("DELETE FROM picday")
    suspend fun delete()
}