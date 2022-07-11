package com.globalhiddenodds.apicday.injection

import android.content.Context
import androidx.room.Room
import com.globalhiddenodds.apicday.datasource.database.AppRoomDatabase
import com.globalhiddenodds.apicday.repository.PicDayDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    fun providePicDayDao(database: AppRoomDatabase): PicDayDao {
        return database.picDayDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context):
            AppRoomDatabase {
        return Room.databaseBuilder(appContext,
        AppRoomDatabase::class.java,
        "apic_db")
            .fallbackToDestructiveMigration()
            .build()
    }
}