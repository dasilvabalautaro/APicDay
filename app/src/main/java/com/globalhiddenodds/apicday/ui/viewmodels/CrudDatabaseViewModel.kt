package com.globalhiddenodds.apicday.ui.viewmodels

import androidx.lifecycle.*
import com.globalhiddenodds.apicday.datasource.database.data.PicDay
import com.globalhiddenodds.apicday.datasource.database.data.toPicDayView
import com.globalhiddenodds.apicday.domain.CrudDatabaseUseCase
import com.globalhiddenodds.apicday.ui.data.PicDayView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrudDatabaseViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val crudDatabaseUseCase: CrudDatabaseUseCase
): ViewModel() {
    private val taskResultMutableLive = MutableLiveData<String>()
    val taskResult: LiveData<String> = taskResultMutableLive
    val picDay: LiveData<PicDayView> by lazy {
        Transformations.map(
            crudDatabaseUseCase.picDay.distinctUntilChanged()
        ) {
            transformPicDay(it)
        }
    }
    fun insert(){
        viewModelScope.launch {
            val result = crudDatabaseUseCase.insertPicDay()
            taskResultMutableLive.value = "Insert Pic of Day: $result"
        }
    }

    private fun transformPicDay(picDay: PicDay): PicDayView? {
        val currentPicDay = MutableLiveData<PicDayView>()
        viewModelScope.launch(Dispatchers.IO) {
            currentPicDay.value = picDay.toPicDayView()
        }
        return currentPicDay.value
    }
}