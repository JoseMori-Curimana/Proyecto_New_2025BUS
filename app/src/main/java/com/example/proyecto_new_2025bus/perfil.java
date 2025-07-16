package com.example.proyecto_new_2025bus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class perfil extends AppCompatActivity {

    private TextView tvNombre, tvDNI, tvCodigo, tvTelefono, tvRol;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Ajustes visuales para padding según barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            );
            return insets;
        });

        // Vinculación con los TextView (verifica que coincidan con el XML)
        tvNombre = findViewById(R.id.tvNombres);
        tvDNI = findViewById(R.id.tvDNIs);
        tvCodigo = findViewById(R.id.tvCodigos);
        tvTelefono = findViewById(R.id.tvTelefonos);
        tvRol = findViewById(R.id.tvRol);

        db = FirebaseFirestore.getInstance();

        // Obtener el código enviado desde el login
        String codigo = getIntent().getStringExtra("codigo");

        if (codigo != null && !codigo.isEmpty()) {
            cargarDatosPorCodigo(codigo);
        } else {
            Toast.makeText(this, "No se recibió el código del usuario", Toast.LENGTH_SHORT).show();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationChofer);
        bottomNavigationView.setSelectedItemId(R.id.nav_historial);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_inicio) {
                startActivity(new Intent(this, chofer.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_asientos) {
                startActivity(new Intent(this, AsistenciaChoferActivity.class));
                return true;
            } else if (id == R.id.nav_historial) {
                startActivity(new Intent(this, HistorialActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_perfil) {
                return true;
            }

            return false;
        });
        Button btnAgregarChofer = findViewById(R.id.btnAgregarChofer);
        btnAgregarChofer.setOnClickListener(v -> {
            Intent intent = new Intent(perfil.this, RegistrarChoferActivity.class);
            startActivity(intent);
        });

    }



    private void cargarDatosPorCodigo(String codigo) {
        db.collection("usuarios")
                .whereEqualTo("codigo", codigo)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);

                        tvNombre.setText("" + doc.getString("nombre"));
                        tvDNI.setText(" " + doc.getString("dni"));
                        tvCodigo.setText(" " + doc.getString("codigo"));
                        tvTelefono.setText("" + doc.getString("telefono"));
                        tvRol.setText("" + doc.getString("rol"));
                    } else {
                        Toast.makeText(this, "No se encontró el usuario con código: " + codigo, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al obtener datos: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }


}
