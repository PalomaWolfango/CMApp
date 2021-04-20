package com.example.cmapp.api

//import com.google.android.gms.maps.model.LatLng
import java.sql.Blob

data class User(
    val id: Int,
    val username: String,
    val password: String
)

data class Markers(
    val id: Int,
    val titulo: String,
    val descricao: String,
    val lat: Double,
    val longi: Double,
    val foto: String,
    val login_id: Int
)