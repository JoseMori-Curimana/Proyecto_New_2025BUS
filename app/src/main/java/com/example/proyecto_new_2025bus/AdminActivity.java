package com.example.proyecto_new_2025bus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminActivity extends AppCompatActivity {

    private TextView tvBienvenida;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);  // Aquí se usa el layout sin RecyclerView

        // Inicializa el TextView
        tvBienvenida = findViewById(R.id.tvBienvenida);

        // Recibir el nombre del usuario desde el Intent
        String nombreUsuario = getIntent().getStringExtra("nombre_usuario");
        if (nombreUsuario != null && !nombreUsuario.isEmpty()) {
            tvBienvenida.setText("Bienvenido, " + nombreUsuario);
        } else {
            tvBienvenida.setText("Bienvenido, Administrador");
        }

        // Botón de Registrar Chofer
        findViewById(R.id.btnRegistrarChofer).setOnClickListener(v -> {
            startActivity(new Intent(AdminActivity.this, RegistrarChoferActivity.class));
        });

        // Botón de Asignar Ruta
        findViewById(R.id.btnAsignarRuta).setOnClickListener(v -> {
            startActivity(new Intent(AdminActivity.this, RutaVaciaActivity.class));  // Redirigir a la vista vacía
        });
    }
}
