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
        val intent = Intent(this, Nota::class.java).apply {
        }
        startActivity(intent)
    }
}