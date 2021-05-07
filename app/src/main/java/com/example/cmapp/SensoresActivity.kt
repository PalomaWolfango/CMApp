package com.example.cmapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_sensores.*
import java.io.IOException

//Atividade Sensores (Acelerometro e Luminosidade)
class SensoresActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var txtAcelerometro: TextView
    private lateinit var sensorManager2: SensorManager

    //Sensor Luminosidade
    var sensor: Sensor? = null
    var sensorDistancia: Sensor? = null
    var sensorManager: SensorManager? = null

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensores)

        //Sensor da luminosidade
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)
        sensorDistancia = sensorManager!!.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        //Sensor Acelerómetro
        sensorManager2 = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        AppCompatDelegate.setDefaultNightMode((AppCompatDelegate.MODE_NIGHT_NO))
        txtAcelerometro = findViewById(R.id.txtAcelerometro)
        setUpSensorStuff()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun setUpSensorStuff(){
        sensorManager2 = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        sensorManager2.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also{

            sensorManager2.registerListener(this,
                it,
                SensorManager.SENSOR_DELAY_FASTEST,
                SensorManager.SENSOR_DELAY_FASTEST)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        //Sensor de Luminosidade
        try{
            if (event != null) {
                if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                    if (event!!.values[0] < 50) {
                        relativeLayoutSensor.setBackgroundColor(getResources().getColor(R.color.Secondary))
                    } else {
                        relativeLayoutSensor.setBackgroundColor(getResources().getColor(R.color.white))
                    }
                }

                else if(event.sensor.getType() == Sensor.TYPE_PROXIMITY){
                    if (event!!.values[0] < sensorDistancia!!.maximumRange) {
                        iconSensor.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_contactless_24_sensor2))
                    } else {
                        iconSensor.setImageDrawable(null)
                    }
                }
            }
        }catch(e : IOException){

        }

        //Sensor de Acelerómetro
        if(event?.sensor?.type == Sensor.TYPE_ACCELEROMETER){
            val sides = event.values[0]
            val upDown = event.values[1]

            txtAcelerometro.apply{
                rotationX = upDown * 3f
                rotationY = sides * 3f
                rotation = -sides
                translationX = sides * -10
                translationY = upDown * -10
            }

            val color = if (upDown.toInt() == 0 && sides.toInt() == 0) Color.GRAY else Color.DKGRAY
            txtAcelerometro.setBackgroundColor(color)

            txtAcelerometro.text = getString(R.string.caima_baixo) + " " + upDown.toInt() + "\n" + getString(R.string.esquerda_direita) + " " + sides.toInt()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this, sensor , SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager!!.registerListener(this, sensorDistancia , SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager


    }


    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }
}