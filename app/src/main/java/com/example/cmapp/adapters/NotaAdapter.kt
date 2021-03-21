package com.example.cmapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cmapp.EditarNota
import com.example.cmapp.Lista_Notas
import com.example.cmapp.R
import com.example.cmapp.entities.Nota

const val Titulo = "TITULO"
const val Descricao = "DESCRICAO"
const val ID = "ID"

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
        val editarNota : RelativeLayout = itemView.findViewById(R.id.AlterarNota)
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
            callbackInterface.delete(id)
        }

        holder.editarNota.setOnClickListener {
            val context = holder.notaItemView1.context
            val title = holder.notaItemView1.text.toString()
            val description = holder.notaItemView2.text.toString()

            val intent = Intent(context, EditarNota::class.java).apply {
                putExtra(Titulo, title)
                putExtra(Descricao, description )
                putExtra(ID,id)
            }
            context.startActivity(intent)
        }
    }

    internal fun setNotes(notes: List<Nota>) {
        this.notes = notes
        notifyDataSetChanged()
    }

    override fun getItemCount() = notes.size
}