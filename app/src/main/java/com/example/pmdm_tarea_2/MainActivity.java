package com.example.pmdm_tarea_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private Button botonConexion;
    private Button botonSensor;
    private Button botonGuardar;
    private Button botonCargar;

    private TextInputEditText texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);

        botonConexion = findViewById(R.id.botonConexion);
        botonConexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(comprobarInternet()) {
                    Toast.makeText(MainActivity.this, "Hay internet", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "No hay internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        botonSensor = findViewById(R.id.botonSensor);
        botonSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }


        });

        botonCargar = findViewById(R.id.botonCargar);
        botonCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }


        });

        botonGuardar = findViewById(R.id.botonGuardar);
        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        texto = findViewById(R.id.textfield);

        webView.loadUrl("https://www.google.com");
    }

    private boolean comprobarInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.Network network = connectivityManager.getActiveNetwork();
        if (network != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
        }

        return false;
    }


}