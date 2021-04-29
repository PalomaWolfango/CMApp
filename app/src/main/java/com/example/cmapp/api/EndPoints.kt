package com.example.cmapp.api

import retrofit2.http.*

interface EndPoints {

    @FormUrlEncoded
    @POST("verificar_user")
    fun verificarUser(@Field("username")username:String, @Field("password")password:String): retrofit2.Call<User>

    //teste
    @GET("users/anomalias")
    fun getAllAnomalias(): retrofit2.Call<List<Markers>>

    //filtro
    @GET("myuser/{id}/anomalias")
    fun getAllUserAnomalias(@Path("id")id_username:Int): retrofit2.Call<List<Markers>>


}