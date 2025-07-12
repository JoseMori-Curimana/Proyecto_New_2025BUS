package com.example.proyecto_new_2025bus;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeAlumnoActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationAlumno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_alumno);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainAlumno), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNavigationAlumno = findViewById(R.id.bottomNavigationAlumno);

        bottomNavigationAlumno.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_rutas) {
//                startActivity(new Intent(this, VerRutasActivity.class)); // actividad para rutas
                return true;
            } else if (id == R.id.nav_ubicacion) {
                startActivity(new Intent(this, estudiante.class)); // actividad para ubicación
                return true;
            } else if (id == R.id.nav_perfil_alumno) {
//                startActivity(new Intent(this, PerfilAlumnoActivity.class)); // actividad para perfil
                return true;
            }

            return false;
        });
    }
}
