package com.example.pmdm_tarea_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
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
    private WebView webView; // WebView para mostrar páginas web dentro de la app
    private Button botonConexion; // Botón para comprobar conexión a Internet
    private Button botonSensor; // Botón para mostrar la lectura del sensor de humedad
    private Button botonGuardar; // Botón para guardar texto en SharedPreferences
    private Button botonCargar; // Botón para cargar texto desde SharedPreferences

    private TextInputEditText texto; // Campo de texto donde el usuario introduce información

    private SensorManager sensorManager; // Administrador de sensores
    private Sensor sensorHumedad; // Sensor de humedad relativa
    private SensorEventListener listener; // Listener para eventos del sensor
    private Float humedad; // Variable para almacenar la lectura del sensor de humedad

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Asigna el layout principal

        // Inicializa el SensorManager y el sensor de humedad
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorHumedad = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

        // Verifica si el sensor de humedad está disponible, ya que siempre está enviando datos.
        if (sensorHumedad != null) {
            listener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    // Compruebo si el evento viene del sensor de humedad
                    if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
                        System.out.println(event.values[0]); // Muestra la humedad en consola
                        humedad = event.values[0]; // Almacena el valor de humedad
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    // Método obligatorio, pero no se utiliza en este caso
                }
            };
        }

        // Inicializa el WebView y carga una página web
        webView = findViewById(R.id.webView);
        webView.loadUrl("https://www.google.com");

        // Configura los botones
        cargarBotonConexion(); // Configura el botón para comprobar conexión a Internet
        cargarBotonSensor(); // Configura el botón para mostrar datos del sensor
        cargarBotonCargar(); // Configura el botón para cargar datos de SharedPreferences
        cargarBotonGuardar(); // Configura el botón para guardar datos en SharedPreferences

        texto = findViewById(R.id.textfield); // Asocia el campo de texto desde el layout
    }

    // Configura el botón de conexión a Internet
    private void cargarBotonConexion() {
        //busca en el XML un botón con el ID botonConexion.
        botonConexion = findViewById(R.id.botonConexion);//Lo almacena en la variable botonConexion para poder usarlo en el código.
        botonConexion.setOnClickListener(new View.OnClickListener() {  // setOnClickListener permite que el botón haga algo cuando lo presionamos.
            @Override
            public void onClick(View view) {
                // Comprueba si hay conexión a Internet y muestra un mensaje correspondiente
                if (comprobarInternet()) {  //Llama a un método llamado comprobarInternet() que revisa si hay conexión a Internet.
                    muestraToast("Hay internet");
                } else {
                    muestraToast("No hay internet");
                }
            }
        });
    }

    // Configura el botón para guardar datos en SharedPreferences
    private void cargarBotonGuardar() {
        botonGuardar = findViewById(R.id.botonGuardar);  //Usa findViewById para asociar el botón en el código con el botón definido en el XML (R.id.botonGuardar).
        botonGuardar.setOnClickListener(new View.OnClickListener() {  //Asigna un evento de clic al botón.
            @Override
            public void onClick(View view) {
                String datos = texto.getText().toString(); // Obtiene el texto introducido por el usuario
                if (!texto.getText().toString().isEmpty()) {  //Verifica que el usuario haya escrito algo antes de guardar.
                    // Guarda el texto en SharedPreferences
                    SharedPreferences preferences = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE); //Crea o accede a las preferencias llamadas "MisPreferencias".
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("textoGuardado", datos);
                    editor.apply(); //aplica los cambios

                    //muestra un Toast indicando si se ha guardado el texto, o no has intrododucido nada.
                    Toast.makeText(MainActivity.this, "Texto guardado", Toast.LENGTH_LONG).show();
                    texto.setText(""); // Limpia el campo de texto
                } else {
                    //muestra un Toast indicando que no has intrododucido nada.
                    Toast.makeText(MainActivity.this, "Debes introducir texto", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Configura el botón para cargar datos desde SharedPreferences
    private void cargarBotonCargar() {
        botonCargar = findViewById(R.id.botonCargar);
        botonCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (texto.getText().toString().isEmpty()) {
                    // Recupera el texto guardado desde SharedPreferences, de modo privado y solo desde la app.
                    SharedPreferences preferences = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                    String textoGuardado = preferences.getString("textoGuardado", "No hay datos guardados");
                    texto.setText(textoGuardado); //Introduce el texto guardado en el campo.

                    //Muestra un Toast si ha leido correctamente, y otro si no has introducido nada.
                    Toast.makeText(MainActivity.this, "Leyendo desde SharedPreferences", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "No hay texto a cargar", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Configura el botón para mostrar datos del sensor
    private void cargarBotonSensor() {
        botonSensor = findViewById(R.id.botonSensor);
        botonSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Muestra la lectura del sensor o un mensaje si no hay datos disponibles
                if (humedad != null) {
                    muestraToast("La humedad es " + humedad);
                } else {
                    muestraToast("No hay datos de humedad");
                }
            }
        });
    }

    // Muestra un mensaje en forma de Toast
    private void muestraToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Registra el listener del sensor cuando la actividad está en primer plano
        if (sensorHumedad != null) {
            sensorManager.registerListener(listener, sensorHumedad, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Libera el listener del sensor cuando la actividad no está en primer plano
        if (sensorHumedad != null) {
            sensorManager.unregisterListener(listener);
        }
    }

    // Comprueba si hay conexión a Internet
    private boolean comprobarInternet() {
        //ConnectivityManager es una clase que maneja el estado de la red.
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE); //obtiene el servicio del sistema que gestiona las conexiones.
        android.net.Network network = connectivityManager.getActiveNetwork(); //getActiveNetwork() obtiene la red actualmente en uso.
        if (network != null) { //Si capabilities == null, la conexión no tiene capacidades activas.
            // Verifica los tipos de conexión disponibles (WiFi, celular o Ethernet)
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network); //NetworkCapabilities nos dice cómo está conectado el dispositivo (WiFi, datos móviles, etc.).
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
        }
        return false; // No hay conexión
    }
}
