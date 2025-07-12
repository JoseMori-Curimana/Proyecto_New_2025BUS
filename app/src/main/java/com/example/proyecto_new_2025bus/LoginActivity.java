package com.example.proyecto_new_2025bus;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    EditText editTextCodigo, editTextPassword;
    Button btnLogin;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = FirebaseFirestore.getInstance();

        editTextCodigo = findViewById(R.id.etCodigo);
        editTextPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String codigo = editTextCodigo.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (TextUtils.isEmpty(codigo)) {
                editTextCodigo.setError("Ingrese su código");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                editTextPassword.setError("Ingrese su contraseña");
                return;
            }

            // Consultar Firestore
            db.collection("usuarios")
                    .whereEqualTo("codigo", codigo)
                    .whereEqualTo("password", password)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                            String rol = document.getString("rol");

                            Class<?> destino;

                            // Redirige a AdminActivity si el rol es admin
                            if ("admin".equals(rol)) {
                                destino = AdminActivity.class;
                            } else {
                                // Redirige a la nueva actividad vacía para cualquier otro rol
                                destino = AsignarRutaActivity.class;
                            }

                            // Pasa el nombre del usuario al Intent
                            Intent intent = new Intent(LoginActivity.this, destino);
                            intent.putExtra("nombre_usuario", document.getString("nombre"));  // Pasa el nombre al Intent
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Código o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(LoginActivity.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
