package com.example.proyecto_new_2025bus;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

public class AsistenciaActivity extends AppCompatActivity {

    private GridLayout gridLayout;
    private final int totalAsientos = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistencia);

        gridLayout = findViewById(R.id.gridAsientos);

        int totalFilas = 10;
        int totalColumnas = 6;
        int contadorAsiento = 1;

        for (int fila = 0; fila < totalFilas; fila++) {
            for (int columna = 0; columna < totalColumnas; columna++) {

                // Columnas 2 y 3 serán el pasillo (vacías)
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

                // Crear asiento y colocarlo en la grilla
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
    }

    private View crearAsiento(int numero) {
        Button btn = new Button(this);
        btn.setText("A" + numero);
        btn.setBackgroundColor(Color.LTGRAY);
        btn.setTextColor(Color.BLACK);
        btn.setAllCaps(false);
        btn.setBackgroundResource(R.drawable.bg_asiento);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = 160;
        params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1);
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        btn.setLayoutParams(params);

        btn.setOnClickListener(v -> mostrarDialogo(btn));

        return btn;
    }



    private void mostrarDialogo(Button asiento) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar asistencia");
        builder.setMessage("¿El estudiante asistió?");

        builder.setPositiveButton("Sí", (dialog, which) -> {
            asiento.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50"))); // verde
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            asiento.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F44336"))); // rojo
        });

        builder.setNeutralButton("Cancelar", (dialog, which) -> dialog.dismiss());

        builder.show();
    }
}
