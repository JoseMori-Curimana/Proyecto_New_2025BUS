package com.example.proyecto_new_2025bus;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class RegistrarChoferActivity extends AppCompatActivity {

    EditText etCodigo, etPassword, etNombre;
    Button btnRegistrar;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_chofer);

        db = FirebaseFirestore.getInstance();

        etCodigo = findViewById(R.id.etCodigo);
        etPassword = findViewById(R.id.etPassword);
        etNombre = findViewById(R.id.etNombre);
        btnRegistrar = findViewById(R.id.btnRegistrarChofer);

        btnRegistrar.setOnClickListener(v -> {
            String codigo = etCodigo.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String nombre = etNombre.getText().toString().trim();

            if (TextUtils.isEmpty(codigo) || TextUtils.isEmpty(password) || TextUtils.isEmpty(nombre)) {
                Toast.makeText(RegistrarChoferActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Crear un nuevo Usuario
            Usuario nuevoChofer = new Usuario(codigo, password, nombre, "chofer"); // Bus asignado es null por ahora

            // Guardar en Firestore
            db.collection("usuarios")
                    .add(nuevoChofer)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(RegistrarChoferActivity.this, "Chofer registrado con éxito", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(RegistrarChoferActivity.this, "Error al registrar el chofer", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
