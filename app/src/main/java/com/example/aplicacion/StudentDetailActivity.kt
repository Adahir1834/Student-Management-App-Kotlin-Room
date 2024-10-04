package com.example.aplicacion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class StudentDetailActivity : AppCompatActivity() {
    private lateinit var textViewNombre: TextView
    private lateinit var textViewEdad: TextView
    private lateinit var textViewGrupo: TextView
    private lateinit var textViewPromedio: TextView
    private lateinit var buttonModificar: Button
    private lateinit var buttonEliminar: Button
    private var estudiante: Estudiante? = null
    private lateinit var estudianteDao: EstudianteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_estudiante)

        // Inicialización de vistas
        textViewNombre = findViewById(R.id.textViewNombre)
        textViewEdad = findViewById(R.id.textViewEdad)
        textViewGrupo = findViewById(R.id.textViewGrupo)
        textViewPromedio = findViewById(R.id.textViewPromedio)
        buttonModificar = findViewById(R.id.buttonModificar)
        buttonEliminar = findViewById(R.id.buttonEliminar)

        // Inicializa Room DAO
        estudianteDao = DatabaseProvider.getDatabase(this).estudianteDao()

        // Recuperar el estudiante de la Intent
        estudiante = intent.getSerializableExtra("estudiante") as? Estudiante

        // Actualizar vistas con los datos del estudiante
        estudiante?.let {
            textViewNombre.text = it.nombre
            textViewEdad.text = it.edad.toString()
            textViewGrupo.text = it.grupo
            textViewPromedio.text = it.promediogeneral.toString()
        }

        // Configuración de click listeners
        buttonEliminar.setOnClickListener {
            mostrarDialogoConfirmacion()
        }

        buttonModificar.setOnClickListener {
            val intent = Intent(this, ModificarEstudianteActivity::class.java)
            intent.putExtra("estudiante", estudiante)
            startActivity(intent)
        }
    }

    private fun mostrarDialogoConfirmacion() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar este estudiante?")
            .setPositiveButton("Sí") { _, _ ->
                estudiante?.let {
                    eliminarEstudiante(it.id ?: return@let)
                }
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun eliminarEstudiante(id: Int) {
        lifecycleScope.launch {
            try {
                estudianteDao.eliminar(id)
                Toast.makeText(
                    this@DetalleEstudianteActivity,
                    "Estudiante eliminado",
                    Toast.LENGTH_SHORT
                ).show()
                redirigirAMainActivity()
            } catch (e: Exception) {
                Toast.makeText(
                    this@DetalleEstudianteActivity,
                    "Error al eliminar estudiante: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun redirigirAMainActivity() {
        val intent = Intent(this@DetalleEstudianteActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish() // Cierra la actividad actual para evitar que el usuario regrese aquí
    }
}
