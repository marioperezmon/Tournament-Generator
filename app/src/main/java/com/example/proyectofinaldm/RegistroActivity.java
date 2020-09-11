package com.example.proyectofinaldm;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RegistroActivity extends AppCompatActivity {

    // Recupera las variables globales
    private ArrayAdapter<Jugador> adaptador;
    private ArrayList<Jugador> jugadores;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_layout); // Se ajusta el layout propio de esta clase

        // Inicializacion de atributos
        this.jugadores = new ArrayList<>();

        final Button bt_registro = this.findViewById(R.id.bt_registro);
        final Button bt_cancela = this.findViewById(R.id.bt_cancelar);

        bt_cancela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salir();
            }
        });

        bt_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inserta();
            }
        });

        // Recupero los datos de los jugadores guardado en las preferencias
        final SharedPreferences PREFS = this.getSharedPreferences("prefs", MODE_PRIVATE);

        final Set<String> JUGADORES = PREFS.getStringSet("jugadores", new HashSet<String>());

        for (String strJug : JUGADORES) {
            String[] partesJugador = strJug.split(" "); // separa nombre y apellido por el espacio

            if (partesJugador.length == 2) { // Si solo mete un apellido
                // Aqui no uso el adaptador, sino directamente el arraylist
                jugadores.add(new Jugador(partesJugador[0], partesJugador[1]));

                // this.adaptador.add(new Jugador(partesJugador[0], partesJugador[1]));

            } else { // Si mete ambos apellidos
                // Aqui no uso el adaptador, sino directamente el arraylist
                jugadores.add(new Jugador(partesJugador[0], partesJugador[1] + " " + partesJugador[2]));

                // this.adaptador.add(new Jugador(partesJugador[0], partesJugador[1] + " " +
                // partesJugador[2]));

            }
        }
    }

    private void salir() {
        Toast.makeText(RegistroActivity.this, "Jugador no registrado", Toast.LENGTH_SHORT).show();
        this.setResult(RESULT_CANCELED);
        this.finish();
    }

    private void inserta() {

        final EditText et_nombre = this.findViewById(R.id.et_nombre);
        final EditText et_apellidos = this.findViewById(R.id.et_apellidos);

        String str_nombre = et_nombre.getText().toString();
        String str_apellidos = et_apellidos.getText().toString();

        if (nuevoJug(str_nombre, str_apellidos)) {
            final Intent DATOS_JUG = new Intent();

            // pendiente cambiar por str_nombre y str_apellidos
            if (et_nombre.getText().toString().equals("") || et_apellidos.getText().toString().equals("")) {
                Toast.makeText(this, "Rellena ambos campos", Toast.LENGTH_SHORT).show();

            } else {
                DATOS_JUG.putExtra("nombre", et_nombre.getText().toString());
                DATOS_JUG.putExtra("apellidos", et_apellidos.getText().toString());

                Toast.makeText(RegistroActivity.this, "Jugador registrado con exito", Toast.LENGTH_SHORT).show();

                this.setResult(Activity.RESULT_OK, DATOS_JUG);
                this.finish();
            }
        } else {
            Toast.makeText(this, "Este jugador ya existe", Toast.LENGTH_SHORT).show();
        }
    }

    // funcion que devuelve true si el jugador no existe ya en la app
    private boolean nuevoJug(String nombre, String apellidos) {

        // bucle para comparar los nombres y apellidos de los jugadores ya existentes
        for (Jugador jug : this.jugadores) {
            if (jug.getNombre().equals(nombre) && jug.getApellidos().equals(apellidos)) {
                return false;
            }
        }

        // si no encontro un jugador con ese nombre y apellidos, devuelve true
        return true;
    }
}