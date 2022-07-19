package com.globalhiddenodds.apicday.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import com.globalhiddenodds.apicday.datasource.database.data.PicDay
import com.globalhiddenodds.apicday.repository.PicDayDao
import com.globalhiddenodds.apicday.ui.activities.MainActivity.Companion.dateSearch
import com.globalhiddenodds.apicday.workers.DownloadPicDayWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CrudDatabaseUseCase @Inject constructor(
    private val picDayDao: PicDayDao
) {
    private val picDayMutableLive = MutableLiveData<List<PicDay>>()
    var lisPicDay: LiveData<List<PicDay>>? = null

    //val lisPicDay: LiveData<List<PicDay>> = picDayMutableLive.distinctUntilChanged()

    fun setDate(date: String) {
        lisPicDay = picDayDao
            .getPicDay(date).asLiveData()
    }

    suspend fun insertPicDay(): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            val pic = DownloadPicDayWorker.picDayCloud
            if (pic != null) {
                picDayDao.insert(pic.toPicDay())
                return@withContext Result.success(true)
            } else {
                return@withContext Result.failure(Throwable("Pic Day null"))
            }
        }
    }

    suspend fun cleanDatabase(): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            picDayDao.delete()
            return@withContext Result.success(true)
        }
    }
}