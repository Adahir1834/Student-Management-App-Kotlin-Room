package com.example.aplicacion

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RemoveStudentActivity : AppCompatActivity() {
    private var estudianteId = 0

    private lateinit var db: AppDatabase
    private lateinit var estudianteDao: EstudianteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar la base de datos y DAO
        db = DatabaseProvider.getDatabase(this)
        estudianteDao = db.estudianteDao()

        estudianteId = intent.getIntExtra("estudianteId", -1)

        if (estudianteId != -1) {
            eliminarEstudiante(estudianteId)
        } else {
            Toast.makeText(this, "Error al obtener el ID del estudiante", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun eliminarEstudiante(id: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                estudianteDao.eliminar(id) // Usar el DAO para eliminar el estudiante
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@EliminarEstudianteActivity, "Estudiante eliminado", Toast.LENGTH_SHORT).show()
                    redirigirAMainActivity()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@EliminarEstudianteActivity, "Error al eliminar estudiante", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun redirigirAMainActivity() {
        val intent = Intent(this@EliminarEstudianteActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish() // Cierra la actividad actual para evitar que el usuario regrese aquí
    }
}
