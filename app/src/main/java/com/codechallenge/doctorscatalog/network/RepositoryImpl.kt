package com.codechallenge.doctorscatalog.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.codechallenge.doctorscatalog.data.model.presentation.Doctor
import com.codechallenge.doctorscatalog.utils.converter.ResponsesMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor() : Repository {

    @Inject
    lateinit var networkService: RepositoryService

    @Inject
    lateinit var mapper: ResponsesMapper

    override suspend fun loadFirstPage(): Flow<PagingData<Doctor>> {
        return withContext(Dispatchers.IO) {
            val pagingSource = DoctorsFlowPagingSource(networkService, mapper)
            return@withContext Pager(
                config = PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = true,
                    prefetchDistance = 10,
                    initialLoadSize = 1
                ),
                pagingSourceFactory = { pagingSource }
            ).flow
        }
    }
}