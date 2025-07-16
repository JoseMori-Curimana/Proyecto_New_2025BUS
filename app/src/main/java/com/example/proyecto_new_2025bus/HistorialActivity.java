package com.example.proyecto_new_2025bus;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.*;
import android.widget.AdapterView;

public class HistorialActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistorialAdapter adapter;
    private List<HistorialItem> historialItems;
    private FirebaseFirestore db;
    private String filtroEstado = "TODOS";
    private String fechaSeleccionada = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    private Spinner spinnerEstado;
    private TextView txtFechaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerHistorial);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        historialItems = new ArrayList<>();
        adapter = new HistorialAdapter(historialItems);
        recyclerView.setAdapter(adapter);

        spinnerEstado = findViewById(R.id.spinnerEstado);
        txtFechaSeleccionada = findViewById(R.id.txtFechaSeleccionada);
        Button btnFecha = findViewById(R.id.btnFecha);

        // Spinner: Todos, ASISTIO, FALTA, RESERVADO
        ArrayAdapter<String> estadoAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                Arrays.asList("Todos", "ASISTIÓ", "AUSENTE", "RESERVADO"));
        estadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(estadoAdapter);

        // Selección de fecha
        btnFecha.setOnClickListener(v -> mostrarSelectorFecha());

        // Filtro por estado
        spinnerEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filtroEstado = parent.getItemAtPosition(position).toString().toUpperCase();
                if (!fechaSeleccionada.isEmpty()) {
                    cargarDesdeFirebase();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Opcional: cuando no hay selección
            }
        });
        String codigo = getIntent().getStringExtra("codigo");
        // Navegación inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationChofer);
        bottomNavigationView.setSelectedItemId(R.id.nav_historial);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_inicio) {
                startActivity(new Intent(this, Chofer.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_asientos) {
                startActivity(new Intent(this, AsistenciaChoferActivity.class));
                return true;
            } else if (id == R.id.nav_historial) {
                startActivity(new Intent(this, HistorialActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_perfil) {
                Intent intent = new Intent(this, perfil.class);
                intent.putExtra("codigo", codigo); //
                startActivity(intent);
                return true;
            }

            return false;
        });
        txtFechaSeleccionada.setText("Fecha: " + fechaSeleccionada);
        cargarDesdeFirebase();
    }

    private void mostrarSelectorFecha() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    fechaSeleccionada = formato.format(calendar.getTime());
                    txtFechaSeleccionada.setText("Fecha: " + fechaSeleccionada);
                    cargarDesdeFirebase();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void cargarDesdeFirebase() {
        db.collection("asistencia")
                .whereEqualTo("fecha", fechaSeleccionada)
                .get()
                .addOnSuccessListener(query -> {
                    historialItems.clear();
                    String filtro = spinnerEstado.getSelectedItem().toString();

                    for (DocumentSnapshot doc : query.getDocuments()) {
                        String dni = doc.getString("dni");
                        String nombre = doc.getString("nombre");
                        String fecha = doc.getString("fecha");
                        String estado = doc.getString("estado").toUpperCase();
                        String asiento = doc.getString("asiento");


                        if (filtro.equals("Todos") || filtro.equals(estado)) {
                            historialItems.add(new HistorialItem(dni, nombre, fecha, estado, asiento));
                        }
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
