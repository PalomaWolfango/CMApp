package com.example.cmapp.api

import retrofit2.http.*

interface EndPoints {

    @FormUrlEncoded
    @POST("verificar_user")
    fun verificarUser(@Field("username")username:String, @Field("password")password:String): retrofit2.Call<User>

    //teste
    @GET("todas_anomalias")
    fun obterTodasAnomalias(): retrofit2.Call<List<Markers>>

}