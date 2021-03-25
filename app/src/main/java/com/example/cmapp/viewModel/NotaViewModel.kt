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

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    fun deleteByNotaId(id: Int?) = viewModelScope.launch {
        repository.deleteByNotaId(id)
    }

    fun updateNota(nota : Nota) = viewModelScope.launch {
        repository.updateNota(nota)
    }
}