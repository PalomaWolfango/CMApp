package com.example.cmapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.cmapp.entities.Nota

@Dao
interface NotaDao {

    @Query("SELECT * FROM nota_table")
    fun getAllNotas(): LiveData<List<Nota>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(nota: Nota)

    @Update
    suspend fun updateNota(nota: Nota)

    @Query("DELETE FROM nota_table WHERE id == :id")
    suspend fun deleteByNotaId(id: Int?)

    @Query("DELETE FROM nota_table")
    suspend fun deleteAll()
}