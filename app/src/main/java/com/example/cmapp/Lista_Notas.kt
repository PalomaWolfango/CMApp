package com.example.cmapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cmapp.adapters.NotaAdapter
import com.example.cmapp.viewModel.NotaViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Lista_Notas : AppCompatActivity() {

    private lateinit var notaViewModel: NotaViewModel
    private val newNotaActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lista_notas)

        // recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = NotaAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // view model
        notaViewModel = ViewModelProvider(this).get(NotaViewModel::class.java)
        notaViewModel.allNotas.observe(this, Observer { notes ->
            notes?.let { adapter.setNotes(it) }
        })

        recyclerView.setOnClickListener{
            val intent = Intent(this@Lista_Notas, Nota::class.java)
            startActivityForResult(intent, newNotaActivityRequestCode)
        }

        //Fab
        val fab = findViewById<FloatingActionButton>(R.id.adicionar_nota)
        fab.setOnClickListener {
            val intent = Intent(this@Lista_Notas, Nota::class.java)
            startActivityForResult(intent, newNotaActivityRequestCode)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newNotaActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val ptitulo = data?.getStringExtra(Nota.EXTRA_REPLY_TITULO)
            val pdescricao = data?.getStringExtra(Nota.EXTRA_REPLY_DESCRICAO)

            if (ptitulo!= null && pdescricao != null) {
                val nota = com.example.cmapp.entities.Nota(titulo = ptitulo, descricao = pdescricao)
                notaViewModel.insert(nota)
            }

        } else {
            Toast.makeText(
                applicationContext,
                R.string.notSaved,
                Toast.LENGTH_LONG).show()
        }
    }

}
