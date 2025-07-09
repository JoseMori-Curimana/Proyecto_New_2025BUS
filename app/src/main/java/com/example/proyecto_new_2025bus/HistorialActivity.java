package com.example.proyecto_new_2025bus;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistorialActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistorialAdapter adapter;
    private List<HistorialItem> historialItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        recyclerView = findViewById(R.id.recyclerHistorial);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        historialItems = new ArrayList<>();
        historialItems.add(new HistorialItem("Usuario #123", "01 de julio de 2025", "ASISTIO"));
        historialItems.add(new HistorialItem("Usuario #124", "02 de julio de 2025", "FALTA"));
        historialItems.add(new HistorialItem("Usuario #125", "03 de julio de 2025", "ASISTIO"));
        historialItems.add(new HistorialItem("Usuario #123", "01 de julio de 2025", "ASISTIO"));
        historialItems.add(new HistorialItem("Usuario #124", "02 de julio de 2025", "FALTA"));
        historialItems.add(new HistorialItem("Usuario #125", "03 de julio de 2025", "ASISTIO"));
        historialItems.add(new HistorialItem("Usuario #123", "01 de julio de 2025", "ASISTIO"));
        historialItems.add(new HistorialItem("Usuario #124", "02 de julio de 2025", "FALTA"));
        historialItems.add(new HistorialItem("Usuario #125", "03 de julio de 2025", "ASISTIO"));
        historialItems.add(new HistorialItem("Usuario #123", "01 de julio de 2025", "ASISTIO"));
        historialItems.add(new HistorialItem("Usuario #124", "02 de julio de 2025", "FALTA"));
        historialItems.add(new HistorialItem("Usuario #125", "03 de julio de 2025", "ASISTIO"));

        adapter = new HistorialAdapter(historialItems);
        recyclerView.setAdapter(adapter);
    }
}