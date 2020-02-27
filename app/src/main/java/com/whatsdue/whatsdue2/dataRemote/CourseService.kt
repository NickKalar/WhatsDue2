package com.whatsdue.whatsdue2.dataRemote

import com.whatsdue.whatsdue2.restmodels.responseModel.Wrapper
import retrofit2.Call
import retrofit2.http.GET

interface CourseService {
    @GET("courses")
    fun getCoursesList(): Call<Wrapper>
}