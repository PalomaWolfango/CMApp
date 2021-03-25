package com.example.cmapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText

class AdicionarNota : AppCompatActivity() {

    private lateinit var tituloText: EditText
    private lateinit var descricaoText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionarnota)

        tituloText = findViewById(R.id.edtxtTitulo)
        descricaoText = findViewById(R.id.edtxtDescription)

        if(intent.getStringExtra(EXTRA_REPLY_TITULO).isNullOrEmpty() && intent.getStringExtra(EXTRA_REPLY_DESCRICAO).isNullOrEmpty()){

            val buttonGuardar = findViewById<Button>(R.id.btnGuardarNota)
            buttonGuardar.setOnClickListener {

                if (TextUtils.isEmpty((tituloText.text)) || TextUtils.isEmpty((descricaoText.text))) {

                    if (TextUtils.isEmpty(tituloText.text) && !TextUtils.isEmpty(descricaoText.text)) {
                        tituloText.error = getString(R.string.tituloMessage)
                    }

                    if (!TextUtils.isEmpty(descricaoText.text) && TextUtils.isEmpty(descricaoText.text)) {
                        descricaoText.error = getString(R.string.DescMessage)
                    }

                    if (TextUtils.isEmpty(tituloText.text) && TextUtils.isEmpty(descricaoText.text)) {
                        tituloText.error = getString(R.string.tituloMessage)
                        descricaoText.error = getString(R.string.DescMessage)
                    }

                } else {
                    val replyIntent = Intent()

                    replyIntent.putExtra(EXTRA_REPLY_TITULO, tituloText.text.toString())
                    replyIntent.putExtra(EXTRA_REPLY_DESCRICAO, descricaoText.text.toString())
                    setResult(Activity.RESULT_OK, replyIntent)
                    finish()
                }
            }
        }


    }
    companion object {
        const val EXTRA_REPLY_TITULO = "com.example.android.titulo"
        const val EXTRA_REPLY_DESCRICAO = "com.example.android.descricao"
    }

}