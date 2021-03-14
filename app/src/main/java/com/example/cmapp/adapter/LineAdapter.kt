package com.example.cmapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cmapp.R
import com.example.cmapp.dataclasses.Notes
import kotlinx.android.synthetic.main.recyclerline.view.*

class LineAdapter(val list: ArrayList<Notes>): RecyclerView.Adapter<LineViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineViewHolder {

        val itemView = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.recyclerline, parent, false);
        return LineViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: LineViewHolder, position: Int) {
        val currentPlace = list[position]

        holder.titulo.text = currentPlace.titulo
        holder.descricaoNota.text = currentPlace.descricaoNota
        holder.dataCriacao.text = currentPlace.dataCriacao.toString()
    }

}

class LineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val titulo = itemView.titulo
    val descricaoNota = itemView.descricaoNota
    var dataCriacao = itemView.data
}