package com.example.cmapp.db

import androidx.lifecycle.LiveData
import com.example.cmapp.dao.NotaDao
import com.example.cmapp.entities.Nota

class NotaRepository(private val notaDao: NotaDao) {

    val allNotas: LiveData<List<Nota>> = notaDao.getAllNotas()

    suspend fun insert(nota: Nota) {
        notaDao.insert(nota)
    }

    suspend fun updateNota(nota: Nota) {
        notaDao.updateNota(nota)
    }

    suspend fun deleteByNotaId(id: Int?){
        notaDao.deleteByNotaId(id)
    }

    suspend fun deleteAll(){
        notaDao.deleteAll()
    }
}