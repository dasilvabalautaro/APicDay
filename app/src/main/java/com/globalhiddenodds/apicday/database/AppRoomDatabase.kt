package com.globalhiddenodds.apicday.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.globalhiddenodds.apicday.repository.LikesDao

@Database(entities = [Likes::class], version = 1, exportSchema = false)
abstract class AppRoomDatabase: RoomDatabase() {
    abstract fun likeDao(): LikesDao
}