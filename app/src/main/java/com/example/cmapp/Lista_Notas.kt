package com.example.cmapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cmapp.adapter.LineAdapter
import com.example.cmapp.dataclasses.Notes
import kotlinx.android.synthetic.main.lista_notas.*

class Lista_Notas : AppCompatActivity() {

    private lateinit var myList: ArrayList<Notes>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lista_notas)

        myList = ArrayList<Notes>()

        //Virá p.e. de WS
        for(i in 0 until 500) {
            myList.add(Notes("Título $i", i*500,  "Descrição $i"))
        }
        recycler_view.adapter = LineAdapter(myList)
        recycler_view.layoutManager = LinearLayoutManager(this)

    }
}
