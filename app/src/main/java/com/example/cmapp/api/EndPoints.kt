package com.example.cmapp.api

import retrofit2.http.*

interface EndPoints {

    @FormUrlEncoded
    @POST("verificar_user")
    fun verificarUser(@Field("username")username:String, @Field("password")password:String): retrofit2.Call<User>

    //Obter todas as anomalias
    @GET("todas_anomalias")
    fun obterTodasAnomalias(): retrofit2.Call<List<Markers>>

    //Obter anomalias pelo tipo de anomalia
    @GET("anomalia/{tipo}")
    fun obterAnomaliasTipo(@Path("tipo")tipo:String): retrofit2.Call<List<Markers>>

}