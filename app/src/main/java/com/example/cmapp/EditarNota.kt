package com.example.cmapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.cmapp.adapters.Descricao
import com.example.cmapp.adapters.ID
import com.example.cmapp.adapters.Titulo
import com.example.cmapp.entities.Nota
import com.example.cmapp.viewModel.NotaViewModel

class EditarNota : AppCompatActivity() {

    private lateinit var description: EditText
    private lateinit var title: EditText
    private lateinit var notaViewModel: NotaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editarnota)

        val Titulo = intent.getStringExtra(Titulo)
        val Descricao = intent.getStringExtra(Descricao)

        findViewById<EditText>(R.id.edtxtTituloAlterar).setText(Titulo)
        findViewById<EditText>(R.id.edtxtDescriptionAlterar).setText(Descricao)

        notaViewModel = ViewModelProvider(this).get(NotaViewModel::class.java)
    }

    fun updateNota(view: View) {
        title = findViewById(R.id.edtxtTituloAlterar)
        description = findViewById(R.id.edtxtDescriptionAlterar)

        var message = intent.getIntExtra(ID, 0)
        val replyIntent = Intent()

        if (TextUtils.isEmpty((title.text)) || TextUtils.isEmpty((description.text))) {

            if (TextUtils.isEmpty((title.text)) && !TextUtils.isEmpty((description.text))) {
                title.error = getString(R.string.tituloMessage)
            }

            if (!TextUtils.isEmpty((title.text)) && TextUtils.isEmpty((description.text))) {
                description.error = getString(R.string.DescMessage)
            }

            if (TextUtils.isEmpty((title.text)) && TextUtils.isEmpty((description.text))) {
                title.error = getString(R.string.tituloMessage)
                description.error = getString(R.string.DescMessage)
            }

        } else {
            val nota = Nota (
                id = message,
                titulo = title.text.toString(),
                descricao = description.text.toString()
            )
            notaViewModel.updateNota(nota)
            finish()
        }


    }
}