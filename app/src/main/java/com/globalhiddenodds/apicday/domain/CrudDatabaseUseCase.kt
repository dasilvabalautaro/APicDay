package com.globalhiddenodds.apicday.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.globalhiddenodds.apicday.datasource.database.data.PicDay
import com.globalhiddenodds.apicday.repository.PicDayDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CrudDatabaseUseCase @Inject constructor(
    private val picDayDao: PicDayDao
) {
    var lisPicDay: LiveData<List<PicDay>>? = null

    fun setDate(date: String) {
        lisPicDay = picDayDao
            .getPicDay(date).asLiveData()
    }

    suspend fun cleanDatabase(): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            picDayDao.delete()
            return@withContext Result.success(true)
        }
    }
}