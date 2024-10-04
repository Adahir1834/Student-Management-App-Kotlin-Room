package com.example.aplicacion

import androidx.lifecycle.lifecycleScope
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var listViewAlumnos: ListView
    private lateinit var btnAgregarAlumno: Button
    private lateinit var btnActualizar: Button
    private lateinit var db: AppDatabase
    private lateinit var estudianteDao: EstudianteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicialización de vistas
        listViewAlumnos = findViewById(R.id.listViewAlumnos)
        btnAgregarAlumno = findViewById(R.id.btnAgregarAlumno)
        btnActualizar = findViewById(R.id.btnConsultar)

        // Inicializar la base de datos y DAO
        db = DatabaseProvider.getDatabase(this)
        estudianteDao = db.estudianteDao()

        // Cargar estudiantes al iniciar
        cargarEstudiantes()

        // Configurar el botón de agregar alumno
        btnAgregarAlumno.setOnClickListener {
            val intent = Intent(this, AgregarEstudianteActivity::class.java)
            startActivity(intent)
        }

        // Configurar el botón de actualizar
        btnActualizar.setOnClickListener {
            cargarEstudiantes()
        }

        // Listener para la lista
        listViewAlumnos.setOnItemClickListener { parent, view, position, id ->
            val estudianteSeleccionado = parent.getItemAtPosition(position) as Estudiante
            val intent = Intent(this, DetalleEstudianteActivity::class.java)
            intent.putExtra("estudiante", estudianteSeleccionado)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarEstudiantes() // Actualizar la lista al regresar a la actividad
    }

    private fun cargarEstudiantes() {
        lifecycleScope.launch(Dispatchers.IO) {
            val estudiantes = estudianteDao.obtenerTodos()
            withContext(Dispatchers.Main) {
                val adapter = EstudianteAdapter(this@MainActivity, estudiantes)
                listViewAlumnos.adapter = adapter
            }
        }
    }
}
