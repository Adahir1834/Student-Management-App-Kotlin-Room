package com.example.aplicacion

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class AddStudentActivity : AppCompatActivity() {
    private lateinit var editTextNombre: EditText
    private lateinit var editTextEdad: EditText
    private lateinit var editTextGrupo: EditText
    private lateinit var editTextPromedio: EditText
    private lateinit var btnRegistrar: Button
    private lateinit var estudianteDao: EstudianteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.agregar_estudiante_activity)

        editTextNombre = findViewById(R.id.editTextNombre)
        editTextEdad = findViewById(R.id.editTextEdad)
        editTextGrupo = findViewById(R.id.editTextGrupo)
        editTextPromedio = findViewById(R.id.editTextPromedio)
        btnRegistrar = findViewById(R.id.btnRegistrar)

        // Inicializa Room DAO
        estudianteDao = DatabaseProvider.getDatabase(this).estudianteDao()

        // Listener para validar la edad mientras se escribe
        editTextEdad.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                when {
                    s.length == 3 -> Toast.makeText(
                        this@AgregarEstudianteActivity,
                        "Edad válida",
                        Toast.LENGTH_SHORT
                    ).show()

                    s.length >= 4 -> Toast.makeText(
                        this@AgregarEstudianteActivity,
                        "Error: La edad debe tener exactamente 2 cifras",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        btnRegistrar.setOnClickListener {
            agregarEstudiante()
        }
    }

    private fun agregarEstudiante() {
        val nombre = editTextNombre.text.toString()
        val edadStr = editTextEdad.text.toString()
        val grupo = editTextGrupo.text.toString()
        val promedioStr = editTextPromedio.text.toString()

        // Validar que la edad sea un número de 2 o 3 cifras
        if (edadStr.length !in 2..3) {
            Toast.makeText(this, "La edad debe tener entre 2 y 3 cifras", Toast.LENGTH_SHORT).show()
            return
        }

        val edad = edadStr.toIntOrNull() ?: run {
            Toast.makeText(this, "La edad debe ser un número válido", Toast.LENGTH_SHORT).show()
            return
        }

        // Validar que el promedio sea un número válido
        val promedio = promedioStr.toDoubleOrNull()?.let {
            // Convertir a decimal con dos decimales
            String.format("%.2f", it).toDouble()
        } ?: 0.0

        val estudiante = Estudiante(null, nombre, edad, grupo, promedio)

        lifecycleScope.launch {
            try {
                estudianteDao.insertar(estudiante)
                Toast.makeText(
                    this@AgregarEstudianteActivity,
                    "Estudiante agregado correctamente",
                    Toast.LENGTH_SHORT
                ).show()
                finish() // Regresar a MainActivity
            } catch (e: Exception) {
                Toast.makeText(
                    this@AgregarEstudianteActivity,
                    "Error al agregar estudiante: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
                // Opcional: Registra el error para depuración
                Log.e("AgregarEstudianteActivity", "Error en la inserción", e)
            }
        }
    }
}
