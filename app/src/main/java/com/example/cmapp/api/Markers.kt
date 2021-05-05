package com.example.cmapp.api

//data class dos markers
data class Markers(
    val id: Int,
    val titulo: String,
    val descricao: String,
    val latitude: Double,
    val longitude: Double,
    val imagem: String,
    val login_id: Int,
    val tipo: String
)