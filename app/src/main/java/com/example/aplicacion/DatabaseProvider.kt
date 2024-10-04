package com.example.aplicacion

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

object DatabaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "estudiantes_db"
            )
                .enableMultiInstanceInvalidation() // Habilitar invalidación de múltiples instancias (opcional)
                .build()
            INSTANCE = instance
            instance
        }
    }
}
