package com.example.proyecto_new_2025bus;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrarChoferActivity extends AppCompatActivity {

    EditText etNombre, etUsername, etPassword, etDni, etTelefono, etCorreo, etLicencia, etGenero;
    Button btnRegistrar;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_chofer);

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Referencias a los campos
        etNombre = findViewById(R.id.etNombre);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etDni = findViewById(R.id.etDni);
        etTelefono = findViewById(R.id.etTelefono);
        etCorreo = findViewById(R.id.etCorreo);
        etLicencia = findViewById(R.id.etLicencia);
        etGenero = findViewById(R.id.etGenero);
        btnRegistrar = findViewById(R.id.btnRegistrarChofer);

        // Evento clic
        btnRegistrar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String dni = etDni.getText().toString().trim();
            String telefono = etTelefono.getText().toString().trim();
            String correo = etCorreo.getText().toString().trim();
            String licencia = etLicencia.getText().toString().trim();
            String genero = etGenero.getText().toString().trim();

            if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password) ||
                    TextUtils.isEmpty(dni) || TextUtils.isEmpty(telefono) || TextUtils.isEmpty(correo) ||
                    TextUtils.isEmpty(licencia) || TextUtils.isEmpty(genero)) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Crear chofer como mapa
            Map<String, Object> chofer = new HashMap<>();
            chofer.put("nombre", nombre);
            chofer.put("username", username);
            chofer.put("password", password);
            chofer.put("dni", dni);
            chofer.put("telefono", telefono);
            chofer.put("correo", correo);
            chofer.put("licencia", licencia);
            chofer.put("genero", genero);
            chofer.put("rol", "chofer");
            chofer.put("bus_asignado", null);

            // Guardar en Firestore
            db.collection("usuarios")
                    .add(chofer)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Chofer registrado con éxito", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al registrar el chofer", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
