package com.codechallenge.doctorscatalog.ui.home

import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.codechallenge.doctorscatalog.data.model.presentation.Doctor
import com.codechallenge.doctorscatalog.domain.Repository
import kotlinx.coroutines.flow.Flow
import java.util.*

class MainViewModel @ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {

    val visitedDoctorLiveData = MutableLiveData<List<Doctor>>()
    val showSearchView = MutableLiveData<Int>()
    private val lastVisitedDoctors = LinkedList<Doctor>()
    private var isSearchEnabled = false

    fun addLastVisitedDoctor(doctor: Doctor) {
        if (lastVisitedDoctors.contains(doctor)) return
        lastVisitedDoctors.addLast(doctor)
        if (lastVisitedDoctors.size > 3) lastVisitedDoctors.removeFirst()
        visitedDoctorLiveData.postValue(lastVisitedDoctors)
    }

    suspend fun startLoading(): Flow<PagingData<Doctor>> {
        return repository.loadFirstPage().cachedIn(viewModelScope)
    }

    fun searchDoctorPressed() {
        isSearchEnabled = !isSearchEnabled
        if (isSearchEnabled) {
            showSearchView.postValue(View.VISIBLE)
        } else {
            showSearchView.postValue(View.INVISIBLE)
        }
    }
}