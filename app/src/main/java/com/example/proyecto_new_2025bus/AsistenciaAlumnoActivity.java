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

import java.util.HashMap;
import java.util.Map;

public class AsistenciaAlumnoActivity extends AppCompatActivity {

    private GridLayout gridLayout;
    private final int totalAsientos = 20;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistencia_alumno);

        db = FirebaseFirestore.getInstance();
        gridLayout = findViewById(R.id.gridAsientos);

        // Obtener DNI del alumno actual desde Intent
        final String dniActual = getIntent().getStringExtra("dni-alumno");
        if (dniActual == null) {
            Toast.makeText(this, "DNI del alumno no recibido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fecha fija para demo, idealmente pasar o calcular la fecha actual
        final String fechaActual = "2025-07-15";

        cargarAsientos(dniActual, fechaActual);
    }

    private void cargarAsientos(String dniActual, String fechaActual) {
        gridLayout.removeAllViews();

        // Consultar reservas en Firestore para la fecha actual
        db.collection("asistencia")
                .whereEqualTo("fecha", fechaActual)
                .get()
                .addOnSuccessListener(snapshot -> {
                    Map<String, String> mapaAsientos = new HashMap<>();
                    String dniAlumnoActual = dniActual; // dni recibido en onCreate
                    String asientoReservadoPorAlumno = null;

                    // Construir mapa asiento->dni y detectar el asiento del alumno actual
                    for (DocumentSnapshot doc : snapshot) {
                        String asiento = doc.getString("asiento");
                        String dniDoc = doc.getString("dni");
                        if (asiento != null && dniDoc != null) {
                            mapaAsientos.put(asiento, dniDoc);
                            if (dniAlumnoActual.equals(dniDoc)) {
                                asientoReservadoPorAlumno = asiento;
                            }
                        }
                    }

                    int totalFilas = 10;
                    int totalColumnas = 6;
                    int contadorAsiento = 1;

                    gridLayout.removeAllViews(); // limpiar

                    outer:
                    for (int fila = 0; fila < totalFilas; fila++) {
                        for (int columna = 0; columna < totalColumnas; columna++) {
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
                            if (contadorAsiento > totalAsientos) break outer;

                            String nombreAsiento = "A" + contadorAsiento;
                            Button btn = new Button(this);
                            btn.setText(nombreAsiento);
                            btn.setTextColor(Color.BLACK);
                            btn.setAllCaps(false);
                            btn.setBackgroundResource(R.drawable.bg_asiento);

                            // Pintar según estado
                            if (mapaAsientos.containsKey(nombreAsiento)) {
                                String dniReserva = mapaAsientos.get(nombreAsiento);
                                if (dniAlumnoActual.equals(dniReserva)) {
                                    // Es el asiento del alumno actual: verde
                                    btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
                                } else {
                                    // Asiento reservado por otro: amarillo y deshabilitado
                                    btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFEB3B")));
                                    btn.setEnabled(false);
                                }
                            } else {
                                // Asiento libre: color gris claro (por defecto)
                                btn.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
                            }

                            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                            params.width = 0;
                            params.height = 160;
                            params.columnSpec = GridLayout.spec(columna, 1f);
                            params.rowSpec = GridLayout.spec(fila, 1);
                            btn.setLayoutParams(params);

                            if (asientoReservadoPorAlumno == null) {
                                btn.setOnClickListener(v -> mostrarDialogoReserva(btn, dniAlumnoActual, fechaActual));
                            } else {
                                String finalAsientoReservadoPorAlumno = asientoReservadoPorAlumno;
                                btn.setOnClickListener(v ->
                                        Toast.makeText(this, "Usted ya reservó el asiento " + finalAsientoReservadoPorAlumno, Toast.LENGTH_SHORT).show()
                                );
                            }

                            gridLayout.addView(btn);
                            contadorAsiento++;
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar los asientos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void mostrarDialogoReserva(Button asientoBtn, String dni, String fecha) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reservar asiento");
        builder.setMessage("¿Desea reservar el asiento " + asientoBtn.getText().toString() + "?");

        builder.setPositiveButton("Sí", (dialog, which) -> {
            Map<String, Object> reserva = new HashMap<>();
            reserva.put("dni", dni);
            reserva.put("nombre", "Nombre del alumno"); // Opcional, reemplaza o pasa el nombre real
            reserva.put("fecha", fecha);
            reserva.put("estado", "RESERVADO");
            reserva.put("asiento", asientoBtn.getText().toString());

            db.collection("asistencia").add(reserva)
                    .addOnSuccessListener(docRef -> {
                        Toast.makeText(this, "Asiento reservado exitosamente", Toast.LENGTH_SHORT).show();
                        cargarAsientos(dni, fecha); // refresca la grilla con el nuevo estado
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al reservar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
