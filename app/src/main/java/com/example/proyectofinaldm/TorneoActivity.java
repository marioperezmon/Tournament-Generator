package com.example.proyectofinaldm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TorneoActivity extends AppCompatActivity {

    private ArrayAdapter<Jugador> adaptador;
    private ArrayList<Jugador> jugadores;

    private static int RQ_TERMINA_TORNEO = 4;

    private Intent JUG_TORNEO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.torneo_layout); //Se ajusta el layout propio de esta clase

        this.jugadores = new ArrayList<>();
        this.adaptador = new JugadorArrayAdapter(this, jugadores);

        final Button bt_salir = this.findViewById(R.id.bt_finalizar);

        bt_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salir();
            }
        });

        //Recupero los datos del jugador guardado en las preferencias
        final SharedPreferences PREFS = this.getSharedPreferences("prefs", MODE_PRIVATE);

        final Set<String> JUGADORES = PREFS.getStringSet("jugadores", new HashSet<String>());

        for (String strJug : JUGADORES) {
            String[] partesJugador = strJug.split(" "); //separa nombre y apellido por el espacio

            if (partesJugador.length == 2) { //Si solo mete nombre y apellido
                this.adaptador.add(new Jugador(partesJugador[0], partesJugador[1]));

            } else if (partesJugador.length == 3) { //Si mete nombre y dos apellidos //ESTO TIENE UN ERROR, se puede pasar nombre apellido y victorias SOLO
                this.adaptador.add(new Jugador(partesJugador[0], partesJugador[1] + " " + partesJugador[2]));

            } else if (partesJugador.length == 4) { //Si mete el nombre, un apellido y las estadisiticas de torneos y victorias
                int numVictorias = Integer.parseInt(partesJugador[2]);
                int numTorneos = Integer.parseInt(partesJugador[3]);
                this.adaptador.add(new Jugador(partesJugador[0], partesJugador[1],numVictorias, numTorneos));

            } else { //Si mete el nombre, dos apellidos y las estadisticas
                int numVictorias = Integer.parseInt(partesJugador[3]);
                int numTorneos = Integer.parseInt(partesJugador[4]);
                this.adaptador.add(new Jugador(partesJugador[0], partesJugador[1] + " " + partesJugador[2], numVictorias, numTorneos));

            }
        }

        cruzar();
    }

    public void onCheckboxClicked(View view) {

        Bundle datos_Jug = getIntent().getExtras();

        final TextView jugador31 = this.findViewById(R.id.jugador31);
        final TextView jugador32 = this.findViewById(R.id.jugador32);
        final TextView jugador11 = this.findViewById(R.id.jugador11);
        final TextView jugador12 = this.findViewById(R.id.jugador12);
        final TextView jugador21 = this.findViewById(R.id.jugador21);
        final TextView jugador22 = this.findViewById(R.id.jugador22);
        String jug1final = "";
        String jug2final = "";
        String jug3final = "";
        String jug4final = "";
        String ganador = "";

        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.cb_semifinal11:
                if (checked)
                    jug1final = jugador11.getText().toString();
                jugador31.setText(jug1final);
                Toast.makeText(this, jug1final + " pasa a la final", Toast.LENGTH_SHORT).show();
                final CheckBox cb_semifinal12 = this.findViewById(R.id.cb_semifinal12);
                cb_semifinal12.setChecked(false);
                break;
            case R.id.cb_semifinal12:
                if (checked)
                    jug2final = jugador12.getText().toString();
                jugador31.setText(jug2final);
                Toast.makeText(this, jug2final + " pasa a la final", Toast.LENGTH_SHORT).show();
                final CheckBox cb_semifinal11 = this.findViewById(R.id.cb_semifinal11);
                cb_semifinal11.setChecked(false);
                break;
            case R.id.cb_semifinal21:
                if (checked)
                    jug3final = jugador21.getText().toString();
                jugador32.setText(jug3final);
                Toast.makeText(this, jug3final + " pasa a la final", Toast.LENGTH_SHORT).show();
                final CheckBox cb_semifinal22 = this.findViewById(R.id.cb_semifinal22);
                cb_semifinal22.setChecked(false);
                break;
            case R.id.cb_semifinal22:
                if (checked)
                    jug4final = jugador22.getText().toString();
                jugador32.setText(jug4final);
                Toast.makeText(this, jug4final + " pasa a la final", Toast.LENGTH_SHORT).show();
                final CheckBox cb_semifinal21 = this.findViewById(R.id.cb_semifinal21);
                cb_semifinal21.setChecked(false);
                break;
            case R.id.cb_final11:
                if (checked)
                    ganador = jugador31.getText().toString();
                final CheckBox cb_final12 = this.findViewById(R.id.cb_final12);
                cb_final12.setChecked(false);
                break;
            case R.id.cb_final12:
                if (checked)
                    ganador = jugador32.getText().toString();
                final CheckBox cb_final11 = this.findViewById(R.id.cb_final11);
                cb_final11.setChecked(false);
                break;
        }
    }

    private void cruzar(){

        Bundle datos_Jug = getIntent().getExtras();

        final TextView jugador11 = this.findViewById(R.id.jugador11);
        final TextView jugador12 = this.findViewById(R.id.jugador12);
        final TextView jugador21 = this.findViewById(R.id.jugador21);
        final TextView jugador22 = this.findViewById(R.id.jugador22);

        String jug11 = datos_Jug.getString("jug1");
        String jug12 = datos_Jug.getString("jug2");
        String jug21 = datos_Jug.getString("jug3");
        String jug22 = datos_Jug.getString("jug4");

        double randomPos = Math.round(Math.random() * 2);

        int randomIntPos = (int) randomPos;

        switch (randomIntPos){
            case 0:
                jugador11.setText(jug11);
                jugador12.setText(jug12);

                jugador21.setText(jug21);
                jugador22.setText(jug22);
                break;

            case 1:
                jugador11.setText(jug21);
                jugador12.setText(jug11);

                jugador21.setText(jug12);
                jugador22.setText(jug22);
                break;

            case 2:
                jugador11.setText(jug22);
                jugador12.setText(jug11);

                jugador21.setText(jug12);
                jugador22.setText(jug21);
                break;
        }
    }

    //Vuelve al MainActivity y suma las victorias a los jugadores
    private void salir() {

        final CheckBox cb_semifinal11 = this.findViewById(R.id.cb_semifinal11);
        final CheckBox cb_semifinal21 = this.findViewById(R.id.cb_semifinal21);
        final CheckBox cb_final11 = this.findViewById(R.id.cb_final11);
        final CheckBox cb_final12 = this.findViewById(R.id.cb_final12);

        final TextView jugador11 = this.findViewById(R.id.jugador11);
        final TextView jugador12 = this.findViewById(R.id.jugador12);
        final TextView jugador21 = this.findViewById(R.id.jugador21);
        final TextView jugador22 = this.findViewById(R.id.jugador22);
        final TextView jugador31 = this.findViewById(R.id.jugador31);
        final TextView jugador32 = this.findViewById(R.id.jugador32);

        JUG_TORNEO = new Intent(this, MainActivity.class);

        if ((cb_final11.isChecked() && !jugador31.getText().toString().isEmpty()) ||
                (cb_final12.isChecked() && !jugador32.getText().toString().isEmpty())) {

            if (cb_semifinal11.isChecked()) {
                for (Jugador jug : this.jugadores) {
                    if (jug.toString().equals(jugador11.getText().toString())) {
                        jug.sumarVictoria();
                        JUG_TORNEO.putExtra("jug1", jug.getNombre() + " " + jug.getApellidos() + " " + jug.getNumVictorias() + " " + jug.getNumTorneos());
                    }
                }
            } else {
                for (Jugador jug : this.jugadores) {
                    if (jug.toString().equals(jugador12.getText().toString())) {

                        jug.sumarVictoria();
                        JUG_TORNEO.putExtra("jug1", jug.getNombre() + " " + jug.getApellidos() + " " + jug.getNumVictorias() + " " + jug.getNumTorneos());
                    }
                }
            }

            if (cb_semifinal21.isChecked()) {
                for (Jugador jug : this.jugadores) {
                    if (jug.toString().equals(jugador21.getText().toString())) {

                        jug.sumarVictoria();
                        JUG_TORNEO.putExtra("jug2", jug.getNombre() + " " + jug.getApellidos() + " " + jug.getNumVictorias() + " " + jug.getNumTorneos());
                    }
                }
            } else {
                for (Jugador jug : this.jugadores) {
                    if (jug.toString().equals(jugador22.getText().toString())) {

                        jug.sumarVictoria();
                        JUG_TORNEO.putExtra("jug2", jug.getNombre() + " " + jug.getApellidos() + " " + jug.getNumVictorias() + " " + jug.getNumTorneos());
                    }
                }
            }

            if (cb_final11.isChecked()) {
                for (Jugador jug : this.jugadores) {
                    if (jug.toString().equals(jugador31.getText().toString())) {
                        jug.sumarTorneo();
                        jug.sumarVictoria();
                        JUG_TORNEO.getExtras().remove("jug1");
                        JUG_TORNEO.putExtra("jug1", jug.getNombre() + " " + jug.getApellidos() + " " + jug.getNumVictorias() + " " + jug.getNumTorneos());
                        Toast.makeText(TorneoActivity.this, jug.toString() + " ganó el torneo. Lleva ganados " + jug.getNumTorneos(), Toast.LENGTH_SHORT).show();
                    }
                }

            } else {
                for (Jugador jug : this.jugadores) {
                    if (jug.toString().equals(jugador32.getText().toString())) {
                        jug.sumarTorneo();
                        jug.sumarVictoria();
                        JUG_TORNEO.getExtras().remove("jug2");
                        JUG_TORNEO.putExtra("jug2", jug.getNombre() + " " + jug.getApellidos() + " " + jug.getNumVictorias() + " " + jug.getNumTorneos());
                        Toast.makeText(TorneoActivity.this, jug.toString() + " ganó el torneo. Lleva ganados " + jug.getNumTorneos(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            startActivityForResult(JUG_TORNEO, RQ_TERMINA_TORNEO);
            //this.setResult(Activity.RESULT_OK, JUG_TORNEO);
            //this.finish();

        }else {

            Toast.makeText(this, "No hay jugador seleccionado como ganador", Toast.LENGTH_SHORT).show();

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
}
