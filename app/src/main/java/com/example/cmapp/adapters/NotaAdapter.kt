package com.example.cmapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cmapp.Lista_Notas
import com.example.cmapp.R
import com.example.cmapp.entities.Nota

class NotaAdapter internal constructor(
    context: Context, private val callbackInterface: Lista_Notas
) : RecyclerView.Adapter<NotaAdapter.NotaViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notes = emptyList<Nota>()

    interface CallbackInterface {
        fun passResultCallback(id: Int?)
    }

    class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notaItemView1: TextView = itemView.findViewById(R.id.titulo)
        val notaItemView2: TextView = itemView.findViewById(R.id.descricaoNota)
        val eliminarNota : ImageButton = itemView.findViewById(R.id.delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return NotaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val current = notes[position]
        holder.notaItemView1.text = current.titulo
        holder.notaItemView2.text = current.descricao
        val id: Int? = current.id

        holder.eliminarNota.setOnClickListener {
            callbackInterface.delete(current.id)
        }
    }

    internal fun setNotes(notes: List<Nota>) {
        this.notes = notes
        notifyDataSetChanged()
    }

    override fun getItemCount() = notes.size
}