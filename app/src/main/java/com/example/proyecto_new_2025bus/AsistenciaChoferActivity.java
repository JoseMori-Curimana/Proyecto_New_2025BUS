package com.example.proyecto_new_2025bus;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AsistenciaChoferActivity extends AppCompatActivity {

    private GridLayout gridLayout;
    private FirebaseFirestore db;
    private final int totalAsientos = 20;
    private final String fechaActual = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistencia_chofer);
        String codigo = getIntent().getStringExtra("codigo");
        db = FirebaseFirestore.getInstance();
        gridLayout = findViewById(R.id.gridAsientos);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationChofer);
        bottomNavigationView.setSelectedItemId(R.id.nav_asientos);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_inicio) {
                startActivity(new Intent(this, chofer.class));
                return true;
            } else if (id == R.id.nav_asientos) {
                return true;
            } else if (id == R.id.nav_historial) {
                startActivity(new Intent(this, HistorialActivity.class));
                return true;
            } else if (id == R.id.nav_perfil) {
                Intent intent = new Intent(this, perfil.class);
                intent.putExtra("codigo", codigo); //
                startActivity(intent);
                return true;
            }
            return false;
        });
        Log.d("FIREBASE_FECHA", "Fecha:  " + fechaActual);
        cargarAsientos();
    }

    private void cargarAsientos() {
        gridLayout.removeAllViews();

        db.collection("asistencia")
                .whereEqualTo("fecha", fechaActual)
                .get()
                .addOnSuccessListener(snapshot -> {
                    Map<String, DocumentSnapshot> mapaAsistencia = new HashMap<>();

                    for (DocumentSnapshot doc : snapshot) {
                        String asiento = doc.getString("asiento");
                        if (asiento != null) {
                            mapaAsistencia.put(asiento, doc);
                        }
                    }

                    int totalFilas = 10;
                    int totalColumnas = 6;
                    int contador = 1;

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

                            if (contador > totalAsientos) break outer;

                            String nombreAsiento = "A" + contador;
                            Button btn = new Button(this);
                            btn.setText(nombreAsiento);
                            btn.setTextColor(Color.BLACK);
                            btn.setAllCaps(false);
                            btn.setBackgroundResource(R.drawable.bg_asiento);

                            DocumentSnapshot doc = mapaAsistencia.get(nombreAsiento);
                            if (doc != null) {
                                String estado = doc.getString("estado");
                                if ("ASISTIÓ".equalsIgnoreCase(estado)) {
                                    btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50"))); // Verde
                                } else if ("AUSENTE".equalsIgnoreCase(estado)) {
                                    btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F44336"))); // Rojo
                                } else {
                                    btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFEB3B"))); // Amarillo
                                }

                                btn.setOnClickListener(v -> mostrarDialogoEstado(btn, doc.getId()));
                            } else {
                                btn.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
                                btn.setEnabled(false);
                            }

                            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                            params.width = 0;
                            params.height = 160;
                            params.columnSpec = GridLayout.spec(columna, 1f);
                            params.rowSpec = GridLayout.spec(fila, 1);
                            btn.setLayoutParams(params);
                            gridLayout.addView(btn);
                            contador++;
                        }
                    }

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void mostrarDialogoEstado(Button asientoBtn, String docId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar asistencia");
        builder.setMessage("¿El alumno asistió al asiento " + asientoBtn.getText().toString() + "?");

        builder.setPositiveButton("Sí (Asistió)", (dialog, which) -> {
            actualizarEstado(docId, "ASISTIÓ");
        });

        builder.setNegativeButton("No (Ausente)", (dialog, which) -> {
            actualizarEstado(docId, "AUSENTE");
        });

        builder.setNeutralButton("Cancelar", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void actualizarEstado(String docId, String nuevoEstado) {
        db.collection("asistencia").document(docId)
                .update("estado", nuevoEstado)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Estado actualizado a " + nuevoEstado, Toast.LENGTH_SHORT).show();
                    cargarAsientos(); // refrescar colores
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al actualizar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
