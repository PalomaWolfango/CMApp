package com.example.cmapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cmapp.entities.Nota

@Dao
interface NotaDao {

    @Query("SELECT * FROM nota_table")
    fun getAllNotas(): LiveData<List<Nota>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(nota: Nota)

    @Query("UPDATE nota_table SET titulo=:titulo AND descricao=:descricao WHERE id == :id")
    suspend fun updateNota(titulo: String, descricao: String, id: Int)

    @Query("DELETE FROM nota_table WHERE id == :id")
    suspend fun deleteByNotaId(id: Int)

    @Query("DELETE FROM nota_table")
    suspend fun deleteAll()
}