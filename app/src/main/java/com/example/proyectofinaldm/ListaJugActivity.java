package com.example.proyectofinaldm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ListaJugActivity extends AppCompatActivity {

    //Recupera las variables globales
    private ArrayAdapter<Jugador> adaptador;
    private ArrayList<Jugador> jugadores;

    //variable para saber si el torneo ya esta completo (4 jugadores inscritos)
    boolean completo = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_jug_layout);

        //Inicializacion de atributos
        this.jugadores = new ArrayList<>();

        //Creacion de listeners
        final ListView lv_jug = this.findViewById(R.id.lv_jug);
        final Button bt_volver = this.findViewById(R.id.bt_volver);
        final Button bt_iniciar = this.findViewById(R.id.bt_iniciar);
        final CheckBox cb_seleccionado = this.findViewById(R.id.cb_seleccionado);

        //Uso del adaptador propio
        this.adaptador = new JugadorArrayAdapter(this, jugadores);
        lv_jug.setAdapter(this.adaptador);

        bt_iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                iniciar();
            }
        });

        bt_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salir();
            }
        });

        //Recupero los datos de los jugadores guardado en las preferencias
        final SharedPreferences PREFS = this.getSharedPreferences("prefs", MODE_PRIVATE);

        final Set<String> JUGADORES = PREFS.getStringSet("jugadores", new HashSet<String>());

        for (String strJug : JUGADORES) {
            String[] partesJugador = strJug.split(" "); //separa nombre y apellido por el espacio

            if (partesJugador.length == 2) { //Si solo mete nombre y apellido
                this.adaptador.add(new Jugador(partesJugador[0], partesJugador[1]));

            } else if (partesJugador.length == 3) { //Si mete nombre y dos apellidos
                this.adaptador.add(new Jugador(partesJugador[0], partesJugador[1] + " " + partesJugador[2]));

            } else if (partesJugador.length == 4) { //Si mete el nombre, un apellido y las estadisiticas de torneos y victorias
                int numVictorias = Integer.parseInt(partesJugador[2]);
                int numTorneos = Integer.parseInt(partesJugador[3]);
                this.adaptador.add(new Jugador(partesJugador[0], partesJugador[1], numVictorias, numTorneos));

            } else { //Si mete el nombre, dos apellidos y las estadisticas
                int numVictorias = Integer.parseInt(partesJugador[3]);
                int numTorneos = Integer.parseInt(partesJugador[4]);
                this.adaptador.add(new Jugador(partesJugador[0], partesJugador[1] + " " + partesJugador[2], numVictorias, numTorneos));

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        final SharedPreferences.Editor PREFS = this.getSharedPreferences("prefs", MODE_PRIVATE).edit();
        final Set<String> JUGADORES = new HashSet<>();

        PREFS.clear();

        //Recorre los jugadores
        for (Jugador jug : this.jugadores) {
            JUGADORES.add(jug.toString2());
        }

        //Se añade el set
        PREFS.putStringSet("jugadores", JUGADORES);

        //Se aplican los cambios
        PREFS.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        final SharedPreferences PREFS = this.getSharedPreferences("prefs", MODE_PRIVATE);

        final Set<String> JUGADORES = PREFS.getStringSet("jugadores", new HashSet<String>());

        this.jugadores.clear();
        for (String strJug : JUGADORES) {
            String[] partesJugador = strJug.split(" "); //separa nombre y apellido por el espacio

            if (partesJugador.length == 2) { //Si solo mete nombre y apellido
                this.adaptador.add(new Jugador(partesJugador[0], partesJugador[1]));

            } else if (partesJugador.length == 3) { //Si mete nombre y dos apellidos
                this.adaptador.add(new Jugador(partesJugador[0], partesJugador[1] + " " + partesJugador[2]));

            } else if (partesJugador.length == 4) { //Si mete el nombre, un apellido y las estadisiticas de torneos y victorias
                int numVictorias = Integer.parseInt(partesJugador[2]);
                int numTorneos = Integer.parseInt(partesJugador[3]);
                this.adaptador.add(new Jugador(partesJugador[0], partesJugador[1], numVictorias, numTorneos));

            } else { //Si mete el nombre, dos apellidos y las estadisticas
                int numVictorias = Integer.parseInt(partesJugador[3]);
                int numTorneos = Integer.parseInt(partesJugador[4]);
                this.adaptador.add(new Jugador(partesJugador[0], partesJugador[1] + " " + partesJugador[2], numVictorias, numTorneos));

            }
        }
    }

    private void salir() {
        Intent salir = new Intent(this, MainActivity.class);
        Toast.makeText(ListaJugActivity.this, "Torneo cancelado", Toast.LENGTH_SHORT).show();
        startActivity(salir);
    }

    public void iniciar(){

        int contador = 0;

        //bucle para contar cuantos jugadores ha seleccionado
        for (Jugador jug : this.jugadores) {
            if (jug.getParticipa()){
                contador++;

            }
        }

        if(contador < 4){ //Si NO se ha seleccionado a 4 jugadores
            Toast.makeText(ListaJugActivity.this, "Selecciona a 4 jugadores para iniciar el torneo", Toast.LENGTH_SHORT).show();

        }else {
            Intent JUG_TORNEO = new Intent(this, TorneoActivity.class);
            final CheckBox cb_seleccionado = this.findViewById(R.id.cb_seleccionado);

            if(contador == 4){ //Si hay cuatro jugadores seleccionados

                int i = 0;

                //Bucle para recoger los jugadores seleccionados
                for (Jugador jug : this.jugadores) {
                    if (jug.getParticipa()){
                        if (i == 0) {
                            JUG_TORNEO.putExtra("jug1", jug.getNombre() + " " + jug.getApellidos());
                            i++;
                        } else {
                            if (i == 1) {
                                JUG_TORNEO.putExtra("jug2", jug.getNombre() + " " + jug.getApellidos());
                                i++;
                            } else {
                                if (i == 2) {
                                    JUG_TORNEO.putExtra("jug3", jug.getNombre() + " " + jug.getApellidos());
                                    i++;
                                } else {
                                    JUG_TORNEO.putExtra("jug4", jug.getNombre() + " " + jug.getApellidos());
                                    i = 0;
                                }
                            }
                        }
                    }
                }
                startActivity(JUG_TORNEO);

            }else{
                Toast.makeText(this, "Has seleccionado mas de 4 jugadores", Toast.LENGTH_SHORT).show();
            }
        }
    }
}