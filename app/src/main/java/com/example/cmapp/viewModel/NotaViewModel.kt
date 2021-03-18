package com.example.cmapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.cmapp.db.NotaDB
import com.example.cmapp.db.NotaRepository
import com.example.cmapp.entities.Nota
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotaViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NotaRepository

    val allNotas: LiveData<List<Nota>>

    init {
        val notasDao = NotaDB.getDatabase(application, viewModelScope).notaDao()
        repository = NotaRepository(notasDao)
        allNotas = repository.allNotas
    }

    fun insert(nota: Nota) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(nota)
    }

    // delete all
    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    // delete by Id
    fun deleteNotaId(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteByNotaId(id)
    }

    fun updateCity(titulo: String, descricao: String, id: Int) = viewModelScope.launch {
        repository.updateNota(titulo, descricao, id)
    }
}