package com.globalhiddenodds.apicday.overview

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globalhiddenodds.apicday.database.Likes
import com.globalhiddenodds.apicday.database.toLikeView
import com.globalhiddenodds.apicday.network.ImageCloud
import com.globalhiddenodds.apicday.network.toLikes
import com.globalhiddenodds.apicday.repository.LikesDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PersistenceViewModel @Inject constructor(
    private val likesDao: LikesDao
) : ViewModel() {
    private val likesState = mutableStateOf(0)
    private var listBestLikes = MutableLiveData<List<LikeView>>()
    private val observerBestLikes = Observer<List<Likes>> {
        it?.let {
            viewModelScope.launch {
                listBestLikes.value = transformLikes(it)
            }
        }
    }
    val listLikes: List<LikeView>?
        get() = listBestLikes.value?.toMutableStateList()
    val likesCurrent: Int
        get() = likesState.value

    init {
        likesDao.getBestLikes().observeForever(observerBestLikes)
    }

    fun getLikesOfId(id: Int) {
        viewModelScope.launch(
            viewModelScope.coroutineContext + Dispatchers.IO
        ) {
            try {
                val result = likesDao.getLikesOfId(id).first()
                likesState.value = result.like
            } catch (ex: Exception) {
                likesState.value = 0
            }
        }
    }

    fun updateLikes(imageCloud: ImageCloud, likes: Int) {
        viewModelScope.launch(
            viewModelScope.coroutineContext + Dispatchers.IO
        ) {
            val newLike = likes + 1
            imageCloud.like = newLike
            if (likes == 0) {
                likesDao.insertLike(imageCloud.toLikes())
            } else {
                likesDao.updateLikes(imageCloud.toLikes())
            }

            getLikesOfId(imageCloud.id)
        }

    }

    private suspend fun transformLikes(list: List<Likes>): List<LikeView> {
        val listImage = withContext(
            viewModelScope.coroutineContext + Dispatchers.IO
        ) {
            val listMutableImage = mutableListOf<LikeView>()
            list.map { listMutableImage.add(it.toLikeView()) }
            return@withContext listMutableImage
        }

        return listImage
    }

}