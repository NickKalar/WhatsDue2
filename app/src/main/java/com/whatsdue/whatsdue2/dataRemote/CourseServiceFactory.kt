package com.whatsdue.whatsdue2.dataRemote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CourseServiceFactory {

    private var API = "https://www.whatsdue.net/api/"


    fun makeService(): CourseService {
        val logging = HttpLoggingInterceptor()
        logging.apply { logging.level = HttpLoggingInterceptor.Level.BODY }
        val httpClient = OkHttpClient.Builder().addInterceptor(logging)

        val builder = Retrofit.Builder()
            .baseUrl(API)
            .addConverterFactory(GsonConverterFactory.create())

        return builder
            .client(httpClient.build())
            .build().create(CourseService::class.java)
    }
}