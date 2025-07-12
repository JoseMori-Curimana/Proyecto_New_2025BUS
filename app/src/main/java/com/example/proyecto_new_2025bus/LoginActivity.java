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

            db.collection("usuarios")
                    .whereEqualTo("codigo", codigo)
                    .whereEqualTo("password", password)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        if (!querySnapshot.isEmpty()) {
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                            String rol = document.getString("rol");
                            String nombre = document.getString("nombre");

                            Intent intent;

                            if ("chofer".equalsIgnoreCase(rol)) {
                                intent = new Intent(this, HomeChoferActivity.class);
                            } else if ("alumno".equalsIgnoreCase(rol)) {
                                intent = new Intent(this, HomeAlumnoActivity.class);
                            } else {
                                Toast.makeText(this, "Usuario no existe o no autorizado", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            intent.putExtra("nombre_usuario", nombre);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(this, "Código o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al iniciar sesión: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
