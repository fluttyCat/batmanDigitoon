package com.core.api

import com.core.dto.batman.BatmanDetailDto
import com.core.dto.batman.BatmanWrapperDto
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeApi {

    @GET(".")
    suspend fun getMoviesList(
        @Query("apikey") apikey: String,
        @Query("s") value: String,
    ): BatmanWrapperDto


    @GET(".")
    fun getMovieDetails(
        @Query("apikey") apikey: String,
        @Query("i") value: String,
    ): Observable<BatmanDetailDto>

}