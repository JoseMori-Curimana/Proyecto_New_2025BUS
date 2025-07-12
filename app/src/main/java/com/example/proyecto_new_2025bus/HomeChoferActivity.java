package com.example.proyecto_new_2025bus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeChoferActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_choferprincipal);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Mostrar el nombre del chofer si fue enviado desde LoginActivity
        String nombre = getIntent().getStringExtra("nombre_usuario");
        TextView tvBienvenida = findViewById(R.id.tvBienvenidaChofer);
        if (nombre != null && !nombre.isEmpty()) {
            tvBienvenida.setText("¡Bienvenido, " + nombre + "!");
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationChofer);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_inicio) {
                startActivity(new Intent(this, chofer.class));
                return true;
            } else if (id == R.id.nav_asientos) {
                startActivity(new Intent(this, AsistenciaActivity.class));
                return true;
            } else if (id == R.id.nav_perfil) {
                startActivity(new Intent(this, HistorialActivity.class));
                return true;
            }

            return false;
        });
    }
}
