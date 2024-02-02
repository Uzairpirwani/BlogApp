package com.example.blogapplication.network

import com.example.blogapplication.data.ApiRequest
import com.example.blogapplication.data.ApiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface IRequestContact {
    @POST("service.php")
    fun makeApiCall(@Body request: ApiRequest): Call<ApiResponse>
}