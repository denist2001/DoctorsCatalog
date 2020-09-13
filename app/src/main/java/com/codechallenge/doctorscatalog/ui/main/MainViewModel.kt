package com.codechallenge.doctorscatalog.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.codechallenge.doctorscatalog.data.model.presentation.Doctor
import com.codechallenge.doctorscatalog.network.RepositoryImpl
import kotlinx.coroutines.flow.Flow
import java.util.*

class MainViewModel @ViewModelInject constructor(
    private val repository: RepositoryImpl
) : ViewModel() {

    val visitedDoctorLiveData = MutableLiveData<List<Doctor>>()
    private val lastVisitedDoctors = LinkedList<Doctor>()

    fun addLastVisitedDoctor(doctor: Doctor) {
        if (lastVisitedDoctors.contains(doctor)) return
        lastVisitedDoctors.addLast(doctor)
        if (lastVisitedDoctors.size > 3) lastVisitedDoctors.removeFirst()
        visitedDoctorLiveData.postValue(lastVisitedDoctors)
    }

    suspend fun startLoading(): Flow<PagingData<Doctor>> {
        return repository.loadFirstPage().cachedIn(viewModelScope)
    }
}