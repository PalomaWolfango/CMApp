package com.example.cmapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class MainActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            supportActionBar?.hide()

            val sessionAutomatica: SharedPreferences = getSharedPreferences(
                getString(R.string.SP),
                Context.MODE_PRIVATE
            )

            //Página inicial do Delay
            Handler(Looper.getMainLooper()).postDelayed({
                //Verificação se o user estiver logado, através do SP
                if (sessionAutomatica.getBoolean(getString(R.string.logado), false)) {
                    val intent = Intent(this@MainActivity, MapaActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    val intent = Intent(this@MainActivity, Login::class.java)
                    startActivity(intent)
                    finish()
                }
            },900)
        }
}

