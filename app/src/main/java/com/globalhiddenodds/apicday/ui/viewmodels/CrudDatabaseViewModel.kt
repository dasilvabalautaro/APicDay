package com.globalhiddenodds.apicday.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.globalhiddenodds.apicday.datasource.database.data.PicDay
import com.globalhiddenodds.apicday.datasource.database.data.toPicDayView
import com.globalhiddenodds.apicday.domain.CrudDatabaseUseCase
import com.globalhiddenodds.apicday.ui.data.PicDayView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CrudDatabaseViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val crudDatabaseUseCase: CrudDatabaseUseCase
) : ViewModel() {
    private val taskResultMutableLive = MutableLiveData<String>()
    val taskResult: LiveData<String> = taskResultMutableLive
    val loading = mutableStateOf(false)

    val lisPicDay: LiveData<List<PicDayView>> by lazy {
        crudDatabaseUseCase.lisPicDay!!.switchMap {
            liveData { emit(transformPicDay(it)) }
        }
    }

    fun insert() {
        viewModelScope.launch {
            val result = crudDatabaseUseCase.insertPicDay()
            taskResultMutableLive.value = "Insert Pic of Day: $result"
        }
    }

    private suspend fun transformPicDay(list: List<PicDay>):
            List<PicDayView> {
        val listPic = withContext(
            viewModelScope
                .coroutineContext + Dispatchers.IO
        ) {
            val listPicDayView = mutableListOf<PicDayView>()
            list.map { listPicDayView.add(it.toPicDayView()) }
            return@withContext listPicDayView
        }

        return listPic
    }

    fun setSearchDate(date: String) {
        loading.value = true
        crudDatabaseUseCase.setDate(date)
    }
}