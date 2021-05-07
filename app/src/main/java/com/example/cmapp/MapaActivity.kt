package com.example.cmapp


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import android.util.Log
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.Toast
import com.example.cmapp.api.EndPoints
import com.example.cmapp.api.Markers
import com.example.cmapp.api.ServiceBuilder
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt


class MapaActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private var results = FloatArray(1)

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)
        supportActionBar?.hide()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //FILTROS POR TIPO DE ANOMALIA (RADIOGROUP)
        val relativeLayout = findViewById<RelativeLayout>(R.id.relativeLayout)
        val posicao0 = RadioButton(this)
        posicao0.layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        posicao0.setText(getString(R.string.buraco))
        posicao0.id = 0

        val posicao1 = RadioButton(this)
        posicao1.layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        posicao1.setText(getString(R.string.obras))
        posicao1.id = 1

        val posicao2 = RadioButton(this)
        posicao2.layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        posicao2.setText(getString(R.string.transito))
        posicao2.id = 2

        val posicao3 = RadioButton(this)
        posicao3.layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        posicao3.setText(getString(R.string.todos))
        posicao3.id = 3

        val radioGroup = RadioGroup(this)
        val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(20, 0, 0, 0)
        radioGroup.layoutParams = params

        radioGroup.addView(posicao0)
        radioGroup.addView(posicao1)
        radioGroup.addView(posicao2)
        radioGroup.addView(posicao3)
        relativeLayout.addView(radioGroup)

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                0 -> getBuraco()
                1 -> getObras()
                2 -> getTransito()
                3 -> onMapReady(map)
            }
        }

        //FILTROS POR DISTÂNCIA
        val relativeLayout2 = findViewById<RelativeLayout>(R.id.relativeLayout2)
        val distancia500 = RadioButton(this)
        distancia500.layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        distancia500.setText("500m")
        distancia500.id = 0

        val distancia1000 = RadioButton(this)
        distancia1000.layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        distancia1000.setText("1000m")
        distancia1000.id = 1

        val distancia1500 = RadioButton(this)
        distancia1500.layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        distancia1500.setText("1500m")
        distancia1500.id = 2

        val nenhuma = RadioButton(this)
        nenhuma.layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        nenhuma.setText(getString(R.string.nenhuma))
        nenhuma.id = 3

        val radioGroup2 = RadioGroup(this)
        val params2 = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params2.setMargins(20, 0, 0, 0)
        radioGroup.layoutParams = params2

        radioGroup2.addView(distancia500)
        radioGroup2.addView(distancia1000)
        radioGroup2.addView(distancia1500)
        radioGroup2.addView(nenhuma)
        relativeLayout2.addView(radioGroup2)

        radioGroup2.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                0 -> getFiltroDistancia500()
                1 -> getFiltroDistancia1000()
                2 -> getFiltroDistancia1500()
                3 -> onMapReady(map)
            }
        }

        //Botão de terminar sessão
        val terminarSessao = findViewById<FloatingActionButton>(R.id.terminarSessao)
        terminarSessao.setOnClickListener {
            val sessionAutomatica: SharedPreferences = getSharedPreferences(
                getString(R.string.SP),
                Context.MODE_PRIVATE
            )
            with(sessionAutomatica.edit()) {
                clear()
                apply()
            }

            //Toast que aparece após término da sessão
            Toast.makeText(this@MapaActivity, getString(R.string.sessaoTerminada), Toast.LENGTH_SHORT).show()

            val intent = Intent(this@MapaActivity, Login::class.java)
            startActivity(intent)
            finish()
        }

        //Fab
        val fab = findViewById<FloatingActionButton>(R.id.adicionarAnomalia)
        fab.setOnClickListener {
            val intent = Intent(this@MapaActivity, AdicionarAnomalia::class.java)
            startActivity(intent)
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.getUiSettings().setZoomControlsEnabled(true)
        map.setOnMarkerClickListener(this)

        map.mapType = GoogleMap.MAP_TYPE_TERRAIN

        setUpMap()

        // 1
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        map.isMyLocationEnabled = true

        // 2
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            // 3
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                Log.i("local",currentLatLng.toString())
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            }
        }

        val sessionAutomatica: SharedPreferences = getSharedPreferences(
            getString(R.string.SP),
            Context.MODE_PRIVATE
        )
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.obterTodasAnomalias()

        //Apresentação dos marcadores conforme o user que está logado
        call.enqueue(object : Callback<List<Markers>> {
            override fun onResponse(call: Call<List<Markers>>, response: Response<List<Markers>>) {
                if (response.isSuccessful) {
                    val anomalia = response.body()!!

                    for(i in anomalia){
                        val latlong = LatLng(i.latitude,i.longitude)

                        if(i.login_id.equals(sessionAutomatica.all[getString(R.string.id)])) {
                            map.addMarker(MarkerOptions()
                                .position(latlong)
                                .title(i.titulo)
                                .snippet(i.descricao)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)))
                        }else{
                            map.addMarker(MarkerOptions()
                                .position(latlong)
                                .title(i.titulo)
                                .snippet(i.descricao))
                        }
                    }
                }
            }
            override fun onFailure(call: Call<List<Markers>>, t: Throwable) {
            }
        })

    }

    override fun onMarkerClick(p0: Marker?) = false

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        map.isMyLocationEnabled = true
    }

    private fun placeMarkerOnMap(location: LatLng) {
        // 1
        val markerOptions = MarkerOptions().position(location)
        // 2
        map.addMarker(markerOptions)
    }

    //Função para obter as anomalias com o tipo "Buraco"
    fun getBuraco() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location

                    map.clear()

                    val request = ServiceBuilder.buildService(EndPoints::class.java)
                    val call = request.obterAnomaliasTipo(tipo = "Buraco")
                    var position: LatLng

                    val sessionAutomatica: SharedPreferences = getSharedPreferences(
                        getString(R.string.SP), Context.MODE_PRIVATE
                    )

                    call.enqueue(object : Callback<List<Markers>> {
                        override fun onResponse(call: Call<List<Markers>>, response: Response<List<Markers>>) {

                            if (response.isSuccessful) {

                                val anomalias = response.body()!!
                                for (anomalia in anomalias) {
                                    val latlong = LatLng(anomalia.latitude, anomalia.longitude)

                                    if (anomalia.login_id.equals(sessionAutomatica.all[getString(R.string.id)])) {
                                        map.addMarker(MarkerOptions()
                                            .position(latlong)
                                            .title(anomalia.titulo)
                                            .snippet(anomalia.descricao)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)))

                                    } else {
                                        map.addMarker(MarkerOptions().position(latlong).title(anomalia.titulo).snippet(anomalia.descricao))
                                    }
                                }
                            }
                        }
                        override fun onFailure(call: Call<List<Markers>>, t: Throwable) {
                            Toast.makeText(this@MapaActivity, getString(R.string.app_name), Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }

    //Função para obter as anomalias com o tipo "Obras"
    fun getObras() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location

                    map.clear()

                    val request = ServiceBuilder.buildService(EndPoints::class.java)
                    val call = request.obterAnomaliasTipo(tipo = "Obras")
                    var position: LatLng

                    val sessionAutomatica: SharedPreferences = getSharedPreferences(
                        getString(R.string.SP), Context.MODE_PRIVATE
                    )

                    call.enqueue(object : Callback<List<Markers>> {
                        override fun onResponse(call: Call<List<Markers>>, response: Response<List<Markers>>) {

                            if (response.isSuccessful) {

                                val anomalias = response.body()!!

                                for (anomalia in anomalias) {
                                    val latlong = LatLng(anomalia.latitude, anomalia.longitude)

                                    if (anomalia.login_id.equals(sessionAutomatica.all[getString(R.string.id)])) {

                                        map.addMarker(MarkerOptions()
                                            .position(latlong)
                                            .title(anomalia.titulo)
                                            .snippet(anomalia.descricao)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))

                                    } else {
                                        map.addMarker(MarkerOptions().position(latlong).title(anomalia.titulo).snippet(anomalia.descricao))
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Markers>>, t: Throwable) {
                            Toast.makeText(this@MapaActivity, getString(R.string.app_name), Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }

    //Função para obter as anomalias com o tipo "Trânsito"
    fun getTransito() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location

                    map.clear()

                    val request = ServiceBuilder.buildService(EndPoints::class.java)
                    val call = request.obterAnomaliasTipo(tipo = "Trânsito")
                    var position: LatLng

                    val sessionAutomatica: SharedPreferences = getSharedPreferences(
                        getString(R.string.SP), Context.MODE_PRIVATE
                    )

                    call.enqueue(object : Callback<List<Markers>> {
                        override fun onResponse(call: Call<List<Markers>>, response: Response<List<Markers>>) {

                            if (response.isSuccessful) {

                                val anomalias = response.body()!!

                                for (anomalia in anomalias) {
                                    val latlong = LatLng(anomalia.latitude, anomalia.longitude)

                                    if (anomalia.login_id.equals(sessionAutomatica.all[getString(R.string.id)])) {

                                        map.addMarker(MarkerOptions()
                                            .position(latlong)
                                            .title(anomalia.titulo)
                                            .snippet(anomalia.descricao)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)))

                                    } else {
                                        map.addMarker(MarkerOptions()
                                            .position(latlong)
                                            .title(anomalia.titulo)
                                            .snippet(anomalia.descricao))
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Markers>>, t: Throwable) {
                            Toast.makeText(this@MapaActivity, getString(R.string.app_name), Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }

    //Cálculo da distância
    fun calcularDistancia(latitude: Double, longitude: Double, latitude2: Double, longitude2: Double): Float {
        Location.distanceBetween(latitude, longitude, latitude2, longitude2, results)
        return results[0]
    }

    //Distância até 500m
    fun getFiltroDistancia500() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location
                    map.clear()

                    val request = ServiceBuilder.buildService(EndPoints::class.java)
                    val call = request.obterTodasAnomalias()
                    var position: LatLng

                    call.enqueue(object : Callback<List<Markers>> {
                        override fun onResponse(call: Call<List<Markers>>, response: Response<List<Markers>>) {
                            if (response.isSuccessful) {
                                val anomalias = response.body()!!
                                for (anomalia in anomalias) {
                                    position = LatLng(anomalia.latitude, anomalia.longitude)

                                    if (calcularDistancia(location.latitude, location.longitude, anomalia.latitude, anomalia.longitude) <= 500) {
                                        map.addMarker(MarkerOptions()
                                            .position(position)
                                            .title(anomalia.titulo)
                                            .snippet(getString(R.string.distancia)+ ": " + results[0].roundToInt() + getString(R.string.metros))
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)))
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Markers>>, t: Throwable) {
                            Toast.makeText(this@MapaActivity, getString(R.string.erro), Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }

    //Distância até 1000m
    fun getFiltroDistancia1000() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location
                    map.clear()

                    val request = ServiceBuilder.buildService(EndPoints::class.java)
                    val call = request.obterTodasAnomalias()
                    var position: LatLng

                    call.enqueue(object : Callback<List<Markers>> {
                        override fun onResponse(call: Call<List<Markers>>, response: Response<List<Markers>>) {

                            if (response.isSuccessful) {
                                val anomalias = response.body()!!
                                for (anomalia in anomalias) {
                                    position = LatLng(anomalia.latitude, anomalia.longitude)

                                    if (calcularDistancia(location.latitude, location.longitude, anomalia.latitude, anomalia.longitude) <= 1000) {
                                        map.addMarker(MarkerOptions()
                                            .position(position)
                                            .title(anomalia.titulo)
                                            .snippet(getString(R.string.distancia)+ ": " + results[0].roundToInt() + getString(R.string.metros))
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Markers>>, t: Throwable) {
                            Toast.makeText(this@MapaActivity, getString(R.string.erro), Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }

    //Distância até 1000m
    fun getFiltroDistancia1500() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location
                    map.clear()

                    val request = ServiceBuilder.buildService(EndPoints::class.java)
                    val call = request.obterTodasAnomalias()
                    var position: LatLng

                    call.enqueue(object : Callback<List<Markers>> {
                        override fun onResponse(call: Call<List<Markers>>, response: Response<List<Markers>>) {

                            if (response.isSuccessful) {
                                val anomalias = response.body()!!
                                for (anomalia in anomalias) {
                                    position = LatLng(anomalia.latitude, anomalia.longitude)

                                    if (calcularDistancia(location.latitude, location.longitude, anomalia.latitude, anomalia.longitude) <= 1500) {
                                        map.addMarker(MarkerOptions()
                                            .position(position)
                                            .title(anomalia.titulo)
                                            .snippet(getString(R.string.distancia)+ ": " + results[0].roundToInt() + getString(R.string.metros))
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)))
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Markers>>, t: Throwable) {
                            Toast.makeText(this@MapaActivity, getString(R.string.erro), Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }

}