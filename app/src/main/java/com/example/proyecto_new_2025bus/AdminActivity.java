package com.example.proyecto_new_2025bus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class AdminActivity extends AppCompatActivity {

    private TextView tvBienvenida;
    private Button btnRegistrarChofer, btnAsignarRuta;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Referenciar vistas
        tvBienvenida = findViewById(R.id.tvBienvenida);
        btnRegistrarChofer = findViewById(R.id.btnRegistrarChofer);
        btnAsignarRuta = findViewById(R.id.btnAsignarRuta);

        // Mostrar saludo personalizado si hay nombre
        String nombreUsuario = getIntent().getStringExtra("nombre_usuario");
        if (nombreUsuario != null && !nombreUsuario.isEmpty()) {
            tvBienvenida.setText("Bienvenido, " + nombreUsuario);
        } else {
            tvBienvenida.setText("Bienvenido, Administrador");
        }

        // Ir a formulario de registro de chofer
        btnRegistrarChofer.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, RegistrarChoferActivity.class);
            startActivity(intent);
        });

        // Ir a asignar ruta (temporal: RutaVaciaActivity)
        btnAsignarRuta.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, RutaVaciaActivity.class); // luego lo cambias por AsignarRutaActivity
            startActivity(intent);
        });
    }
}
