package com.example.cmapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.app.ActivityCompat
import com.example.cmapp.api.EndPoints
import com.example.cmapp.api.Markers
import com.example.cmapp.api.ServiceBuilder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdicionarAnomalia : AppCompatActivity() {

    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var spinner_anomalia : Spinner
    private lateinit var edTxtDescricao : EditText
    private lateinit var edTxtTitulo : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_anomalia)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val spinnerAnomalia: Spinner = findViewById(R.id.spinnerItems)
        ArrayAdapter.createFromResource(
            this,
            R.array.array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerAnomalia.adapter = adapter
        }

        //Botão de adicionar uma nova anomalia
        val btnGuardarAnomalia = findViewById<Button>(R.id.btnGuardarAnomalia)
        btnGuardarAnomalia.setOnClickListener {

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1
                )

                return@setOnClickListener

            }
            else {
                fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                    if (location != null) {
                        lastLocation = location

                        val atualLatLng = LatLng(location.latitude, location.longitude)

                        val sessionAutomatica: SharedPreferences = getSharedPreferences(
                            getString(R.string.SP),
                            Context.MODE_PRIVATE)

                        var id: Int? = 0
                        id = sessionAutomatica.all[getString(R.string.id)] as Int?

                        edTxtTitulo = findViewById(R.id.edtxtTituloAnomalia)
                        edTxtDescricao = findViewById(R.id.edtxtDescriçãoAnomalia)
                        spinner_anomalia = findViewById(R.id.spinnerItems)

                        val titulo = edTxtTitulo.text.toString()
                        val descricao = edTxtDescricao.text.toString()
                        val imagem = ""
                        val login_id = id
                        val latitude = lastLocation.latitude
                        val longitude = lastLocation.longitude
                        val spinnerVal = spinnerAnomalia.selectedItem.toString()

                        val request = ServiceBuilder.buildService(EndPoints::class.java)
                        val call = request.inserirAnomalia(
                            titulo,
                            descricao,
                            latitude.toString(),
                            longitude.toString(),
                            imagem,
                            login_id.toString().toInt(),
                            spinnerVal
                        )

                        //Verificação se os campos são preenchidos
                        if(edTxtTitulo.text.isNullOrEmpty() || edTxtDescricao.text.isNullOrEmpty()){

                            if(edTxtTitulo.text.isNullOrEmpty() && !edTxtDescricao.text.isNullOrEmpty()){
                                edTxtTitulo.error = getString(R.string.erroUsername)
                            }
                            if(!edTxtTitulo.text.isNullOrEmpty() && edTxtDescricao.text.isNullOrEmpty()){
                                edTxtDescricao.error = getString(R.string.erroPassword)
                            }
                            if(edTxtTitulo.text.isNullOrEmpty() && edTxtDescricao.text.isNullOrEmpty()){
                                edTxtTitulo.error = getString(R.string.erroUsername)
                                edTxtDescricao.error = getString(R.string.erroPassword)
                            }
                        }
                        else {
                            call.enqueue(object : Callback<Markers> {
                                override fun onResponse(call: Call<Markers>, response: Response<Markers>) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(this@AdicionarAnomalia, getString(R.string.adcionadoSucesso), Toast.LENGTH_SHORT).show()
                                    }
                                        val intent = Intent(this@AdicionarAnomalia, MapaActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                override fun onFailure(call: Call<Markers>?, t: Throwable?) {
                                    Toast.makeText(applicationContext, t!!.message, Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                    }
                }
            }
        }
    }
}