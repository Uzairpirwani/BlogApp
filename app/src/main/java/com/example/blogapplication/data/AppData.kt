package com.example.blogapplication.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class ApiRequest(
    val action: String,
    val userName: String? = null,
    val password: String? = null,
    val email: String? = null,
    val userId: Int? = null,
    val blogId: Int? = null,
    val blogName: String? = null,
    val blogDescription: String? = null,
)

data class ApiResponse(
    val status: Boolean,
    val responseCode: Int,
    val message: String,
    val userId: Int,
    val userName: String,
    val userEmail: String,
    val userPassword: String,
    val blogId: Int,
    val blogName: String,
    val blogDescription: String,
    val blogs: ArrayList<Blogs>? = null
)

@Parcelize
data class Blogs(
    val blogId: Int,
    val blogName: String,
    val blogDescription: String
) : Parcelable
