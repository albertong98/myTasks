package com.uniovi.mytasks;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.uniovi.mytasks.databinding.ActivityMapaBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapaActivity extends FragmentActivity implements OnMapReadyCallback {

    private Marker marcaUsuario = null;
    private GoogleMap mMap;
    private ActivityMapaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button buttonAñadirUb = findViewById(R.id.btnAñadirUbicacion);
        Button buttonCancelar = findViewById(R.id.btnCancelar);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        buttonAñadirUb.setOnClickListener(view ->{

            Intent intentResult = new Intent();
//            intentResult.putExtra(FormularioEventos.MARKER,addresses.size() > 0 ? addresses.get(0).getLocality():""+marcaUsuario.getPosition());
            String LatLng = marcaUsuario.getPosition().latitude + ", " + marcaUsuario.getPosition().longitude;
            intentResult.putExtra(FormularioEventos.MARKER,LatLng);
            setResult(RESULT_OK,intentResult);
            finish();
        });

        buttonCancelar.setOnClickListener(view ->{
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Sydney and move the camera
        LatLng oviedo = new LatLng(33.3851, -9.6808);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(oviedo));
        seleccionarUbicacion();
    }

    private void seleccionarUbicacion(){
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if(marcaUsuario!=null){
                    marcaUsuario.remove();
                }
                marcaUsuario = mMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                                .title("Ubicacion Evento"));
            }
        });
    }
}