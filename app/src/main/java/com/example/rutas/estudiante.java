package com.example.rutas;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class estudiante extends AppCompatActivity implements OnMapReadyCallback {

    private Spinner spinnerRuta;
    private TextView tvEstado;
    private GoogleMap mMap;
    private DatabaseReference refFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiante);

        spinnerRuta = findViewById(R.id.spinnerRuta);
        tvEstado = findViewById(R.id.tvEstado);

        ArrayAdapter<String> rutasAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Yarinacocha", "San Fernando"});
        rutasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRuta.setAdapter(rutasAdapter);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapEstudiante);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        refFirebase = FirebaseDatabase.getInstance().getReference("rutas");
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Mostrar ruta por defecto
        String rutaSeleccionada = spinnerRuta.getSelectedItem().toString();
        mostrarRutaSegunSeleccion(rutaSeleccionada);

        spinnerRuta.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                String seleccion = spinnerRuta.getSelectedItem().toString();
                mostrarRutaSegunSeleccion(seleccion);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    private void mostrarRutaSegunSeleccion(String rutaSeleccionada) {
        mMap.clear();

        List<LatLng> puntosRuta = rutaSeleccionada.equals("Yarinacocha")
                ? obtenerRutaYarinacocha()
                : obtenerRutaSanFernando();

        mostrarRutaEnMapa(mMap, puntosRuta);
        escucharUbicacionChofer(rutaSeleccionada);
    }

    private void mostrarRutaEnMapa(GoogleMap googleMap, List<LatLng> ruta) {
        PolylineOptions opciones = new PolylineOptions()
                .addAll(ruta)
                .color(android.graphics.Color.BLUE)
                .width(8);
        googleMap.addPolyline(opciones);

        if (!ruta.isEmpty()) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ruta.get(0), 15));
        }
    }

    private List<LatLng> obtenerRutaYarinacocha() {
        List<LatLng> puntos = new ArrayList<>();
        puntos.add(new LatLng(-8.3790, -74.5360));
        puntos.add(new LatLng(-8.3805, -74.5345));
        puntos.add(new LatLng(-8.3820, -74.5330));
        puntos.add(new LatLng(-8.3840, -74.5320));
        return puntos;
    }

    private List<LatLng> obtenerRutaSanFernando() {
        List<LatLng> puntos = new ArrayList<>();
        puntos.add(new LatLng(-8.3920, -74.5400));
        puntos.add(new LatLng(-8.3935, -74.5385));
        puntos.add(new LatLng(-8.3950, -74.5370));
        puntos.add(new LatLng(-8.3965, -74.5355));
        return puntos;
    }

    private void escucharUbicacionChofer(String ruta) {
        String rutaKey = ruta.toLowerCase().replace(" ", "_");
        refFirebase.child(rutaKey).child("ubicacion").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Double lat = snapshot.child("latitude").getValue(Double.class);
                    Double lng = snapshot.child("longitude").getValue(Double.class);
                    if (lat != null && lng != null) {
                        LatLng ubicacion = new LatLng(lat, lng);
                        mMap.addMarker(new MarkerOptions().position(ubicacion).title("Ubicación actual del chofer"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15));
                        tvEstado.setText("Chofer en ruta");
                    }
                } else {
                    tvEstado.setText("Esperando ubicación del chofer...");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                tvEstado.setText("Error al obtener la ubicación");
            }
        });
    }
}
