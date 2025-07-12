package com.example.proyecto_new_2025bus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int DURACION_SPLASH = 3000; // 3 segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); // Cargar el layout con el mensaje

        // Uso de Handler con método postDelayed en el estilo moderno
        new Handler(getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();  // Termina esta actividad para evitar que el usuario regrese a ella
        }, DURACION_SPLASH);
    }
}
