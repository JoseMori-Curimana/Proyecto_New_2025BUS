package com.example.proyecto_new_2025bus;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText editTextCodigo, editTextPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextCodigo = findViewById(R.id.etCodigo);
        editTextPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String codigo = editTextCodigo.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            boolean hasError = false;

            if (codigo.isEmpty()) {
                editTextCodigo.setError("Campo requerido");
                hasError = true;
            }

            if (password.isEmpty()) {
                editTextPassword.setError("Campo requerido");
                hasError = true;
            }

            if (hasError) {
                Toast.makeText(LoginActivity.this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Aquí puedes agregar la lógica de autenticación
            Toast.makeText(LoginActivity.this, "Login presionado", Toast.LENGTH_SHORT).show();
        });
    }
}
