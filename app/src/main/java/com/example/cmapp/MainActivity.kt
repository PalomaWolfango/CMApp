package com.example.cmapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun btnIrNotas(view: View) {
        val intent = Intent(this, Lista_Notas::class.java).apply {}
        startActivity(intent)
    }

    fun semRegisto(view: View) {
        val intent = Intent(this, Registo::class.java).apply {}
        startActivity(intent)
    }

}