package com.example.cmapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Registo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registo)
    }

    //IR para o login
    fun irLogin(view: View) {
        val intent = Intent(this, MainActivity::class.java).apply {}
        startActivity(intent)
    }
}