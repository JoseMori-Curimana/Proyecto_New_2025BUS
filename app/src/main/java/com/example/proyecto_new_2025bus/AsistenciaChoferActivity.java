package com.example.proyecto_new_2025bus;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AsistenciaChoferActivity extends AppCompatActivity {

    private GridLayout gridLayout;
    private final int totalAsientos = 20;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistencia_chofer);
        db = FirebaseFirestore.getInstance();

        gridLayout = findViewById(R.id.gridAsientos);

        int totalFilas = 10;
        int totalColumnas = 6;
        int contadorAsiento = 1;

        outer:
        for (int fila = 0; fila < totalFilas; fila++) {
            for (int columna = 0; columna < totalColumnas; columna++) {

                // Columnas 2 y 3 son el pasillo vacío
                if (columna == 2 || columna == 3) {
                    View espacio = new View(this);
                    GridLayout.LayoutParams espacioParams = new GridLayout.LayoutParams();
                    espacioParams.width = 0;
                    espacioParams.height = 160;
                    espacioParams.columnSpec = GridLayout.spec(columna, 1f);
                    espacioParams.rowSpec = GridLayout.spec(fila, 1);
                    espacio.setLayoutParams(espacioParams);
                    gridLayout.addView(espacio);
                    continue;
                }

                // Limita a 20 asientos
                if (contadorAsiento > totalAsientos) break outer;

                // Crear botón de asiento
                View asiento = crearAsiento(contadorAsiento++);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = 160;
                params.columnSpec = GridLayout.spec(columna, 1f);
                params.rowSpec = GridLayout.spec(fila, 1);
                asiento.setLayoutParams(params);
                gridLayout.addView(asiento);
            }
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationChofer);
        bottomNavigationView.setSelectedItemId(R.id.nav_asientos); // marca el ítem actual

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_inicio) {
                startActivity(new Intent(this, chofer.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_asientos) {
                return true; // Ya estamos aquí
            } else if (id == R.id.nav_historial) {
                startActivity(new Intent(this, HistorialActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_perfil) {
                // Reemplaza con tu actividad de perfil si tienes una distinta
                return true;
            }

            return false;
        });

        db.collection("usuarios")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                        String rol = document.getString("rol");
                        String nombre = document.getString("nombre");

                        Intent intent;

                        if ("chofer".equalsIgnoreCase(rol)) {

                        } else if ("alumno".equalsIgnoreCase(rol)) {

                        } else {
                            Toast.makeText(this, "Usuario no existe o no autorizado", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    } else {
                        Toast.makeText(this, "Código o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar los datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });


    }

    private View crearAsiento(int numero) {
        Button btn = new Button(this);
        btn.setText("A" + numero);
        btn.setTextColor(Color.BLACK);
        btn.setAllCaps(false);
        btn.setBackgroundColor(Color.LTGRAY);  // Color inicial

        // Si tienes un fondo personalizado en drawable, descomenta:
        btn.setBackgroundResource(R.drawable.bg_asiento);

        // Evento al hacer clic en el asiento
        btn.setOnClickListener(v -> mostrarDialogo(btn));

        return btn;
    }

    private void mostrarDialogo(Button asiento) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar asistencia");
        builder.setMessage("¿El estudiante asistió?");

        builder.setPositiveButton("Sí", (dialog, which) -> {
            asiento.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50"))); // Verde
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            asiento.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F44336"))); // Rojo
        });

        builder.setNeutralButton("Cancelar", (dialog, which) -> dialog.dismiss());

        builder.show();
    }


}
