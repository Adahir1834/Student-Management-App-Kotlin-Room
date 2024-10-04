package com.example.aplicacion

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "estudiante")
data class Student(
    @PrimaryKey(autoGenerate = true) val id: Int? = 0,
    val nombre: String,
    val edad: Int,
    val grupo: String,
    val promediogeneral: Double
): Serializable