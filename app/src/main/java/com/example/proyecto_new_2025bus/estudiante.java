package com.example.proyecto_new_2025bus;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.graphics.Color;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.data.kml.KmlLayer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class estudiante extends AppCompatActivity implements OnMapReadyCallback {
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng ubicacionEstudiante;

    private Spinner spinnerRuta, spinnerViaje;
    private TextView tvEstado;
    private GoogleMap mMap;
    private DatabaseReference refFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        obtenerUbicacionEstudiante();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiante);

        spinnerRuta = findViewById(R.id.spinnerRuta);
        spinnerViaje = findViewById(R.id.spinnerViaje);
        tvEstado = findViewById(R.id.tvEstado);

        ArrayAdapter<String> rutasAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Yarinacocha", "San Fernando"});
        rutasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRuta.setAdapter(rutasAdapter);

        ArrayAdapter<String> viajesAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"1er viaje", "2do viaje", "3er viaje", "4to viaje"});
        viajesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerViaje.setAdapter(viajesAdapter);

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
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mostrarRutaYUbicacion();

        spinnerRuta.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                mostrarRutaYUbicacion();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        spinnerViaje.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                mostrarRutaYUbicacion();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    private void mostrarRutaYUbicacion() {
        if (mMap == null) {
            Log.e("MAP_ERROR", "Mapa aún no inicializado");
            return;
        }

        mMap.clear();

        String rutaSeleccionada = spinnerRuta.getSelectedItem().toString();

        if (rutaSeleccionada.equals("Yarinacocha")) {
            cargarRutaDesdeKML(R.raw.ruta_yarinacocha);
        } else if (rutaSeleccionada.equals("San Fernando")) {
            cargarRutaDesdeKML(R.raw.ruta_sanfernando);
        }

        escucharUbicacionChofer();
    }


    private void mostrarRutaEnMapa(GoogleMap googleMap, List<LatLng> ruta) {
        if (ruta == null || ruta.isEmpty()) {
            Log.e("DEBUG_RUTA", "Ruta vacía");
            return;
        }

        PolylineOptions opciones = new PolylineOptions()
                .addAll(ruta)
                .color(Color.RED)       // 🔴 Cambia el color aquí (ej. RED, GREEN, YELLOW, etc.)
                .width(25);             // 🔍 Cambia el grosor aquí (más alto = más grueso)

        googleMap.addPolyline(opciones);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ruta.get(0), 15));
    }


    private void cargarRutaDesdeKML(int rawResourceId) {
        try {
            KmlLayer layer = new KmlLayer(mMap, rawResourceId, getApplicationContext());
            layer.addLayerToMap();

            // 👇 Agrega este enfoque manual
            LatLng pucallpa = new LatLng(-8.3838, -74.5539);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pucallpa, 14));

            layer.setOnFeatureClickListener(feature ->
                    Log.d("KML", "Elemento tocado: " + feature.getProperty("name"))
            );
        } catch (Exception e) {
            Log.e("KML_ERROR", "Error al cargar KML: " + e.getMessage());
        }
    }





    private void escucharUbicacionChofer() {
        String rutaKey = spinnerRuta.getSelectedItem().toString().toLowerCase().replace(" ", "_");
        String viajeKey = spinnerViaje.getSelectedItem().toString().toLowerCase().replace(" ", "_");

        refFirebase.child(rutaKey).child(viajeKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Boolean activo = snapshot.child("activo").getValue(Boolean.class);
                    Double lat = snapshot.child("lat").getValue(Double.class);
                    Double lng = snapshot.child("lng").getValue(Double.class);

                    if (Boolean.TRUE.equals(activo) && lat != null && lng != null) {
                        LatLng ubicacionChofer = new LatLng(lat, lng);

                        Bitmap original = BitmapFactory.decodeResource(getResources(), R.drawable.bus_stop);
                        Bitmap pequeño = Bitmap.createScaledBitmap(original, 100, 100, false);

                        mMap.addMarker(new MarkerOptions()
                                .position(ubicacionChofer)
                                .title("Chofer en ruta")
                                .icon(BitmapDescriptorFactory.fromBitmap(pequeño)));

                        if (ubicacionEstudiante != null) {
                            float[] resultados = new float[1];
                            Location.distanceBetween(
                                    ubicacionChofer.latitude, ubicacionChofer.longitude,
                                    ubicacionEstudiante.latitude, ubicacionEstudiante.longitude,
                                    resultados
                            );

                            float distanciaMetros = resultados[0];
                            String mensaje;

                            if (distanciaMetros < 100) {
                                mensaje = "🚍 ¡El bus está muy cerca de ti!";
                            } else if (distanciaMetros < 500) {
                                mensaje = "📍 El bus está a unos " + Math.round(distanciaMetros) + " metros.";
                            } else {
                                mensaje = "📍 El bus está lejos, a " + Math.round(distanciaMetros / 1000f * 10) / 10.0 + " km.";
                            }

                            tvEstado.setText(mensaje);
                        } else {
                            tvEstado.setText("Chofer en ruta. (Ubicación tuya no disponible)");
                        }
                    } else {
                        tvEstado.setText("Esperando ubicación del chofer...");
                    }
                } else {
                    tvEstado.setText("Ruta aún no iniciada.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                tvEstado.setText("Error al obtener datos.");
            }
        });
    }



    private void obtenerUbicacionEstudiante() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        ubicacionEstudiante = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.addMarker(new MarkerOptions()
                                .position(ubicacionEstudiante)
                                .title("Tú estás aquí")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    }
                });
    }

    private double calcularDistanciaEnMetros(LatLng punto1, LatLng punto2) {
        float[] results = new float[1];
        Location.distanceBetween(
                punto1.latitude, punto1.longitude,
                punto2.latitude, punto2.longitude,
                results);
        return results[0]; // en metros
    }


}
