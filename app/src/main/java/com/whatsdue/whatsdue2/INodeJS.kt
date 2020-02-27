package com.whatsdue.whatsdue2

import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
interface INodeJS {
    @POST("register")
    @FormUrlEncoded
    fun registerUser(@Field("email") email:String,
                     @Field("name") name:String,
                     @Field("password") password:String): Observable<String>

    @POST("applogin")
    @FormUrlEncoded
    fun loginUser(@Field("username") username:String,
                  @Field("password") password:String):Observable<String>
}