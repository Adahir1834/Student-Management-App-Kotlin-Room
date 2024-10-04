package com.example.aplicacion

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete

@Dao
interface EstudianteDao {

    // Obtener todos los estudiantes
    @Query("SELECT * FROM estudiante")
    suspend fun obtenerTodos(): List<Estudiante>

    // Insertar uno o más estudiantes
    @Insert
    suspend fun insertar(vararg estudiantes: Estudiante)

    // Actualizar un estudiante
    @Update
    suspend fun actualizar(estudiante: Estudiante)

    // Eliminar un estudiante
    @Query("DELETE FROM estudiante WHERE id = :id")
    suspend fun eliminar(id: Int)
}
