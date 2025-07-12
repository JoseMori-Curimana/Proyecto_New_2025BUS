package com.example.proyecto_new_2025bus;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class EditarChoferActivity extends AppCompatActivity {

    private EditText etNombre, etBusAsignado;
    private Button btnGuardarCambios;
    private FirebaseFirestore db;
    private String codigoChofer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_chofer);

        db = FirebaseFirestore.getInstance();

        // Obtén el código del chofer desde el Intent
        codigoChofer = getIntent().getStringExtra("codigo");

        // Inicializa las vistas
        etNombre = findViewById(R.id.etNombre);
        etBusAsignado = findViewById(R.id.etBusAsignado);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios);

        // Cargar los datos actuales del chofer
        cargarDatosChofer();

        // Configurar el botón de guardar cambios
        btnGuardarCambios.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String busAsignado = etBusAsignado.getText().toString().trim();

            if (!nombre.isEmpty()) {
                // Actualiza los datos del chofer en Firestore
                db.collection("usuarios").document(codigoChofer)
                        .update("nombre", nombre, "asignadoABus", busAsignado)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(EditarChoferActivity.this, "Chofer actualizado", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(EditarChoferActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(this, "Por favor ingresa el nombre del chofer", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarDatosChofer() {
        db.collection("usuarios").document(codigoChofer)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String nombre = documentSnapshot.getString("nombre");
                        String busAsignado = documentSnapshot.getString("asignadoABus");

                        etNombre.setText(nombre);
                        etBusAsignado.setText(busAsignado);
                    }
                });
    }
}
