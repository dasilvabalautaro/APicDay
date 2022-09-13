package com.globalhiddenodds.apicday.injection

import android.content.Context
import androidx.room.Room
import com.globalhiddenodds.apicday.database.AppRoomDatabase
import com.globalhiddenodds.apicday.repository.LikesDao
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
    fun provideLikeDao(database: AppRoomDatabase): LikesDao {
        return database.likeDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context):
            AppRoomDatabase {
        return Room.databaseBuilder(
            appContext,
            AppRoomDatabase::class.java,
            "apic_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}