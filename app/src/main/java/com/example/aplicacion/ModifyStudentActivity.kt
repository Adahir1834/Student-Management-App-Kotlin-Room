package com.example.aplicacion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ModifyStudentActivity : AppCompatActivity() {
    private lateinit var editTextNombre: EditText
    private lateinit var editTextEdad: EditText
    private lateinit var editTextGrupo: EditText
    private lateinit var editTextPromedio: EditText
    private lateinit var buttonGuardar: Button
    private var estudiante: Estudiante? = null

    private lateinit var db: AppDatabase
    private lateinit var estudianteDao: EstudianteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modificar_estudiante)

        editTextNombre = findViewById(R.id.editTextNombre)
        editTextEdad = findViewById(R.id.editTextEdad)
        editTextGrupo = findViewById(R.id.editTextGrupo)
        editTextPromedio = findViewById(R.id.editTextPromedio)
        buttonGuardar = findViewById(R.id.buttonGuardar)

        // Inicializar la base de datos y DAO
        db = DatabaseProvider.getDatabase(this)
        estudianteDao = db.estudianteDao()

        estudiante = intent.getSerializableExtra("estudiante") as? Estudiante

        estudiante?.let {
            editTextNombre.setText(it.nombre)
            editTextEdad.setText(it.edad.toString())
            editTextGrupo.setText(it.grupo)
            editTextPromedio.setText(it.promediogeneral.toString())
        }

        buttonGuardar.setOnClickListener {
            guardarCambios()
        }
    }

    private fun guardarCambios() {
        val nombre = editTextNombre.text.toString().trim()
        val grupo = editTextGrupo.text.toString().trim()
        val promedioStr = editTextPromedio.text.toString().trim()

        if (nombre.isEmpty() || grupo.isEmpty() || promedioStr.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val edadStr = editTextEdad.text.toString().trim()
        val edad = edadStr.toIntOrNull() ?: run {
            Toast.makeText(this, "La edad debe ser un número válido", Toast.LENGTH_SHORT).show()
            return
        }

        val promedio = promedioStr.toDoubleOrNull() ?: run {
            Toast.makeText(this, "El promedio debe ser un número válido", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedEstudiante = estudiante?.copy(
            nombre = nombre,
            edad = edad,
            grupo = grupo,
            promediogeneral = promedio
        )

        updatedEstudiante?.let {
            lifecycleScope.launch(Dispatchers.IO) {
                estudianteDao.actualizar(it)  // Usar el DAO para actualizar el estudiante en la base de datos local
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ModificarEstudianteActivity, "Estudiante modificado correctamente", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@ModificarEstudianteActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
            }
        } ?: run {
            Toast.makeText(this, "Error al cargar datos del estudiante", Toast.LENGTH_SHORT).show()
        }
    }
}
