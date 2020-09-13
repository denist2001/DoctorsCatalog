package com.codechallenge.doctorscatalog.ui.main

import androidx.hilt.lifecycle.ViewModelInject
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

    private val lastVisitedDoctors = LinkedList<Doctor>()

    fun getLastVisitedDoctors(): List<Doctor> {
        return lastVisitedDoctors
    }

    fun addLastVisitedDoctor(doctor: Doctor) {
        if (lastVisitedDoctors.contains(doctor)) return
        lastVisitedDoctors.addLast(doctor)
        if (lastVisitedDoctors.size > 3) lastVisitedDoctors.removeFirst()
    }

    suspend fun startLoading(): Flow<PagingData<Doctor>> {
        return repository.loadFirstPage().cachedIn(viewModelScope)
    }
}