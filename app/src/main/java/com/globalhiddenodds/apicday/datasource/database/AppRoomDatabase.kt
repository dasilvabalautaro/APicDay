package com.globalhiddenodds.apicday.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.globalhiddenodds.apicday.datasource.database.data.PicDay
import com.globalhiddenodds.apicday.repository.PicDayDao

@Database(entities = [PicDay::class], version = 1, exportSchema = false)
abstract class AppRoomDatabase: RoomDatabase() {
    abstract fun picDayDao(): PicDayDao
}