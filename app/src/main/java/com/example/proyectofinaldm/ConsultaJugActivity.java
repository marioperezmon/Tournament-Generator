package com.example.proyectofinaldm;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ConsultaJugActivity extends AppCompatActivity {

    //Recupera las variables globales
    private ArrayAdapter<Jugador> adaptador;
    private ArrayList<Jugador> jugadores;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consulta_jug_layout);

        //Inicializacion de atributos
        this.jugadores = new ArrayList<>();

        //Creacion de listeners
        final ListView lv_jug = this.findViewById(R.id.lv_jug);
        final Button bt_volver = this.findViewById(R.id.bt_volverCons);

        //Uso del adaptador propio
        adaptador = new ArrayAdapter<Jugador>(ConsultaJugActivity.this, android.R.layout.simple_selectable_list_item, jugadores);
        lv_jug.setAdapter(this.adaptador);

        bt_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salir();
            }
        });

        //Para que la lista permita detectar pulsaciones de larga duracion:
        lv_jug.setLongClickable(true);
        this.registerForContextMenu(lv_jug);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId() == R.id.lv_jug){
            getMenuInflater().inflate(R.menu.context_menu, menu); //Añadir el layout al contexto

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //Para saber la posicion relativa del click del usuario en la lista
        final int actualPosition = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;

        //EXPLICACION del parseo anterior:
        //en menu item recibimos las opciones borrar o elminiar pero esta sujeto a un menu info que depende de algo
        //al castear a AdapterContextMenuInfo y acceder al position, me da la position del elemento en la lista que he pulsado

        if(item.getItemId() == R.id.idBorrar){


            AlertDialog.Builder builder = new AlertDialog.Builder(ConsultaJugActivity.this);
            builder.setTitle("¿Está seguro?");

            builder.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ConsultaJugActivity.this.jugadores.remove(actualPosition);
                    ConsultaJugActivity.this.adaptador.notifyDataSetChanged();
                }
            });

            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            builder.create().show();

        }

        return super.onContextItemSelected(item);

    }

    private void salir() {
        this.setResult(RESULT_CANCELED);
        this.finish();
    }
}