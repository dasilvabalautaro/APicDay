package com.globalhiddenodds.apicday.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import com.globalhiddenodds.apicday.datasource.database.data.PicDay
import com.globalhiddenodds.apicday.repository.PicDayDao
import com.globalhiddenodds.apicday.workers.DownloadPicDayWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CrudDatabaseUseCase @Inject constructor(
    private val picDayDao: PicDayDao
) {
    private val picDayMutableLive = MutableLiveData<PicDay>()
    val picDay: LiveData<PicDay> by lazy {
        picDayMutableLive.distinctUntilChanged()
    }

    fun getPicDay(date: String) {
        picDayMutableLive.value = picDayDao
            .getPicDay(date).asLiveData().value
    }

    suspend fun insertPicDay(): Result<Boolean>{
        return withContext(Dispatchers.IO) {
            val pic = DownloadPicDayWorker.picDayCloud
            picDayDao.insert(pic.toPicDay())

            return@withContext Result.success(true)
        }
    }

    suspend fun cleanDatabase(): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            picDayDao.delete()
            return@withContext Result.success(true)
        }
    }
}