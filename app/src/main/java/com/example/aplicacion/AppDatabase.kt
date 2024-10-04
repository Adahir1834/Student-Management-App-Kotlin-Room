package com.example.aplicacion

import androidx.room.Database
import androidx.room.RoomDatabase

// Anotación @Database que define las entidades y la versión de la base de datos
@Database(entities = [Estudiante::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    // Método abstracto para obtener una instancia del DAO
    abstract fun estudianteDao(): EstudianteDao
}
