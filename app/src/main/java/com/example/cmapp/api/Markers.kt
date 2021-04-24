package com.example.cmapp.api

data class Markers(
    val id: Int,
    val titulo: String,
    val descricao: String,
    val lat: Double,
    val long: Double,
    val imagem: String,
    val login_id: Int
)