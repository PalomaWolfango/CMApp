package com.example.cmapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.cmapp.api.EndPoints
import com.example.cmapp.api.ServiceBuilder
import com.example.cmapp.api.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Botão após o login, direciona-nos para o mapa
        val entrar = findViewById<Button>(R.id.btnLogin)
        entrar.setOnClickListener{
            val edtxtUsername = findViewById<EditText>(R.id.edtxtUsername)
            val edtxtPassword = findViewById<EditText>(R.id.edtxtPassword)

            //Retirar espaços que poderão existir
            val username = edtxtUsername.text.toString().trim()
            val password = edtxtPassword.text.toString().trim()

            //Verificação se os campos do login são preenchidos corretamente
            if(edtxtUsername.text.isNullOrEmpty() || edtxtPassword.text.isNullOrEmpty()){

                if(edtxtUsername.text.isNullOrEmpty() && !edtxtPassword.text.isNullOrEmpty()){
                    edtxtUsername.error = getString(R.string.erroUsername)
                }
                if(!edtxtUsername.text.isNullOrEmpty() && edtxtPassword.text.isNullOrEmpty()){
                    edtxtPassword.error = getString(R.string.erroPassword)
                }
                if(edtxtUsername.text.isNullOrEmpty() && edtxtPassword.text.isNullOrEmpty()){
                    edtxtUsername.error = getString(R.string.erroUsername)
                    edtxtPassword.error = getString(R.string.erroPassword)
                }
            }

            //Associação dos endpoints ao web server
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.verificarUser(username = username, password = password)

            call.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val utilizador = response.body()!!

                        val intent = Intent(this@Login, MapaActivity::class.java)
                        startActivity(intent)
                        finish()

                        //Guardar a informação no SharedPreferences
                        val sessionAutomatica: SharedPreferences = getSharedPreferences(
                            getString(R.string.SP),
                            Context.MODE_PRIVATE
                        )
                        with(sessionAutomatica.edit()) {
                            putBoolean(getString(R.string.logado), true)
                            putString(getString(R.string.username), username)
                            putInt(getString(R.string.id), utilizador.id)
                            apply()
                        }

                        //Toast após fazer login
                        Toast.makeText(this@Login, "Olá " + sessionAutomatica.getString("username", null) + "!", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<User>, t: Throwable) {
                    //Toast se o login estiver incorreto
                    Toast.makeText(this@Login, getString(R.string.loginIncorreto), Toast.LENGTH_SHORT).show()
                }
            })
        }

    }

    //Botão que nos direciona para a listagem das notas pessoais
    fun btnIrNotas(view: View) {
        val intent = Intent(this, Lista_Notas::class.java).apply {}
        startActivity(intent)
    }

    //Link que nos direciona para o registo
    fun semRegisto(view: View) {
        val intent = Intent(this, Registo::class.java).apply {}
        startActivity(intent)
    }

    fun btnIrSensores(view: View) {
        val intent = Intent(this, SensoresActivity::class.java).apply {}
        startActivity(intent)
    }
}