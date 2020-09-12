package com.codechallenge.doctorscatalog.network

import androidx.paging.PagingSource
import com.codechallenge.doctorscatalog.data.model.api.ApiResponse
import com.codechallenge.doctorscatalog.data.model.presentation.Doctor
import com.codechallenge.doctorscatalog.utils.converter.ResponsesMapper
import java.lang.Exception
import javax.inject.Inject

class DoctorsFlowPagingSource @Inject constructor(
    private val networkService: RepositoryService,
    private val mapper: ResponsesMapper
): PagingSource<String, Doctor>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, Doctor> {
        return try {
            if (params.key.isNullOrEmpty()) {
                networkService.loadFirstPage().run {
                    transformResult(this)
                }
            } else {
                networkService.loadNextPage(params.key.toString()).run {
                    transformResult(this)
                }
            }
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    private fun transformResult(apiResponse: ApiResponse): LoadResult<String, Doctor> {
        val doctors = mapper.transform(apiResponse)
        if (doctors.isEmpty()) return LoadResult.Error(Throwable("Result can not be parsed"))
        return LoadResult.Page(
            data = doctors,
            prevKey = null,//starts from the first page
            nextKey = apiResponse.lastKey
        )
    }
}
