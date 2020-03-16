package com.whatsdue.whatsdue2.dataRemote

import com.whatsdue.whatsdue2.restmodels.Course
import com.whatsdue.whatsdue2.restmodels.responseModel.GenericWrapper
import retrofit2.Call
import retrofit2.http.GET

interface GenericService<T> {

    @GET("courses")
    fun getCourses(): Call<GenericWrapper<Course>>
}