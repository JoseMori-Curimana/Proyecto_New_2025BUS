package com.example.proyecto_new_2025bus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeAlumnoActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationAlumno;
    private String dniAlumno;
    private String nombreAlumno;

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

        dniAlumno = getIntent().getStringExtra("dni-usuario");
        nombreAlumno = getIntent().getStringExtra("nombre_usuario");
        String codigo = getIntent().getStringExtra("codigo");
        if (dniAlumno == null) {
            dniAlumno = "";
            Toast.makeText(this, "DNI no recibido", Toast.LENGTH_SHORT).show();
        }
        bottomNavigationAlumno = findViewById(R.id.bottomNavigationAlumno);

        bottomNavigationAlumno.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_rutas) {
//
                return true;
            } else if (id == R.id.nav_ubicacion) {
                startActivity(new Intent(this, estudiante.class));
                return true;
            } else if (id == R.id.nav_asientos) {
                Intent intent = new Intent(this, AsistenciaAlumnoActivity.class);
                intent.putExtra("dni-alumno", dniAlumno);
                intent.putExtra("nombre-alumno", nombreAlumno);
                startActivity(intent);
                return true;
            }else if (id == R.id.nav_perfil_alumno) {
                Intent intent = new Intent(this, PerfilAlumnoActivity.class);
                intent.putExtra("codigo", codigo); //
                startActivity(intent);
                return true;
            }

            return false;
        });
    }
}
