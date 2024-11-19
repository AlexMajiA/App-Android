package com.example.pmdm_tarea_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private Button botonConexion;
    private Button botonSensor;
    private Button botonGuardar;
    private Button botonCargar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);

        botonConexion = findViewById(R.id.botonConexion);
        botonConexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        webView.loadUrl("https://www.google.com");
    }
}