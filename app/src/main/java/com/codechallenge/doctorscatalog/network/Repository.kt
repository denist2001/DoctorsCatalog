package com.codechallenge.doctorscatalog.network

import androidx.paging.PagingData
import com.codechallenge.doctorscatalog.data.model.presentation.Doctor
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun loadFirstPage(): Flow<PagingData<Doctor>>

}