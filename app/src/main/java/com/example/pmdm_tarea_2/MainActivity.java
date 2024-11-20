package com.example.pmdm_tarea_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private Button botonConexion;
    private Button botonSensor;
    private Button botonGuardar;
    private Button botonCargar;

    private TextInputEditText texto;

    private SensorManager sensorManager;
    private Sensor sensorHumedad;
    private SensorEventListener listener;
    private Float humedad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorHumedad = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

        if (sensorHumedad != null) {
            listener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
                        System.out.println(event.values[0]);
                        humedad = event.values[0];
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                }
            };
        }

        webView = findViewById(R.id.webView);

        botonConexion = findViewById(R.id.botonConexion);
        botonConexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(comprobarInternet()) {
                    muestraToast("Hay internet");
                } else {
                    muestraToast("No hay internet");
                }
            }
        });

        botonSensor = findViewById(R.id.botonSensor);
        botonSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(humedad != null) {
                    muestraToast("La humedad es " + humedad);
                } else {
                    muestraToast("No hay datos de humedad");
                }
            }
        });

        botonCargar = findViewById(R.id.botonCargar);
        botonCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    //Abro el archivo en modo de lectura.
                    InputStreamReader fin = new InputStreamReader(openFileInput("fichero_interno.txt"));
                    BufferedReader bufferedReader = new BufferedReader(fin);

                    StringBuilder contenido = new StringBuilder();
                    String linea;

                    while ((linea = bufferedReader.readLine()) != null){
                        contenido.append(linea).append("\n");

                    }
                    fin.close();

                    texto.setText(contenido.toString());
                    Toast.makeText(MainActivity.this, "fichero_interno.txt", Toast.LENGTH_LONG).show();

                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }



        });

        botonGuardar = findViewById(R.id.botonGuardar);
        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String datos = texto.getText().toString();
                try {
                    OutputStreamWriter fout = new OutputStreamWriter(openFileOutput("fichero_interno.txt", Context.MODE_PRIVATE));

                    fout.write(datos);
                    fout.close();
                    datos = getFileStreamPath("fichero_interno.txt").toString();
                    Toast.makeText(MainActivity.this, "Se ha escrito correctamente", Toast.LENGTH_SHORT).show();
                    texto.setText("");

                } catch (FileNotFoundException e) {
                    //throw new RuntimeException(e);
                    Toast.makeText(MainActivity.this,"no se ha escrito", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }
        });

        texto = findViewById(R.id.textfield);

        webView.loadUrl("https://www.google.com");
    }



    private void muestraToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorHumedad != null) {
            sensorManager.registerListener(listener, sensorHumedad, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorHumedad != null) {
            sensorManager.unregisterListener(listener);
        }
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