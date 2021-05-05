package com.example.cmapp.api

import retrofit2.http.*

interface EndPoints {

    //Verificação do User
    @FormUrlEncoded
    @POST("verificar_user")
    fun verificarUser(@Field("username") username:String, @Field("password") password:String): retrofit2.Call<User>

    //Obter todas as anomalias
    @GET("todas_anomalias")
    fun obterTodasAnomalias(): retrofit2.Call<List<Markers>>

    //Obter anomalias pelo tipo de anomalia
    @GET("anomalia/{tipo}")
    fun obterAnomaliasTipo(@Path("tipo") tipo:String): retrofit2.Call<List<Markers>>

    //Inserir Anomalia
    @FormUrlEncoded
    @POST("inserir_anomalia")
    fun inserirAnomalia(@Field("titulo") titulo: String?,
                       @Field("descricao") descricao: String?,
                       @Field("latitude") latitude: String?,
                       @Field("longitude") longitude: String?,
                       @Field("imagem") imagem: String?,
                       @Field("login_id") login_id: Int?,
                       @Field("tipo") tipo: String? ): retrofit2.Call<Markers>

    //Update da anomalia
    @FormUrlEncoded
    @POST("update_anomalia/{id}")
    fun editarOcorrencia(@Field("id") id: Int? ): retrofit2.Call<Markers>

    //Delete da anomalia
    @FormUrlEncoded
    @POST("delete_anomalia/{id}")
    fun eliminarOcorrencia(@Field("id") id: Int? ): retrofit2.Call<Markers>

}