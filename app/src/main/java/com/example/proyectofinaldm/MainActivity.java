package com.example.proyectofinaldm;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    //Recupera las variables globales
    private ArrayAdapter<Jugador> adaptador;
    private ArrayList<Jugador> jugadores;

    private static int RQ_REGISTRO_ACTIVITY = 1; //Codigo para distinguir las subactividades lanzadas
    private static int RQ_INICIA_TORNEO = 2;
    private static int RQ_CONSULTA_ACTIVITY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicializacion de atributos
        this.jugadores = new ArrayList<>();

        //Uso del adaptador propio
        this.adaptador = new JugadorArrayAdapter(this, jugadores);

        final Button bt_torneo = this.findViewById(R.id.bt_torneo);

        bt_torneo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciaTorneo();
            }
        });

        //recupero los datos del torneo para las estadísticas
        Intent datosRecuperar = getIntent();

        //si tiene datos en el extra -> si viene de un torneo finalizado
        if(datosRecuperar.hasExtra("jug1") && datosRecuperar.hasExtra("jug2")){
            //sobreescribir los atributos con los nuevos datos en el adaptador

            Bundle datos_Jug = getIntent().getExtras();

            String jug1 = datos_Jug.getString("jug1");
            String jug2 = datos_Jug.getString("jug2");

            String[] partesJug1 = jug1.split(" ");
            String[] partesJug2 = jug2.split(" ");

            int victoriasJ1 = Integer.parseInt(partesJug1[2]);
            int torneosJ1 = Integer.parseInt(partesJug1[3]);

            int victoriasJ2 = Integer.parseInt(partesJug2[2]);
            int torneosJ2 = Integer.parseInt(partesJug2[3]);

            Jugador JUGADOR1 = new Jugador(partesJug1[0], partesJug1[1], victoriasJ1, torneosJ1);
            Jugador JUGADOR2 = new Jugador(partesJug2[0], partesJug2[1], victoriasJ2, torneosJ2);

            Toast.makeText(this, "J1 = " + JUGADOR1.toString() + " J2 = " + JUGADOR2.toString(), Toast.LENGTH_SHORT).show();

            //Crea las preferencias a almacenar
            final SharedPreferences.Editor EDIT_PREFS = this.getSharedPreferences("prefs", MODE_PRIVATE).edit();
            final SharedPreferences PREFS = this.getSharedPreferences("prefs", MODE_PRIVATE);

            final Set<String> JUGADORES = PREFS.getStringSet("jugadores", new HashSet<String>());

            //creo los jugadores a sobreescribir
            Jugador j1 = new Jugador();
            Jugador j2 = new Jugador();

            //y meto los nuevos actualizados
            JUGADORES.remove(JUGADOR1.toString());
            JUGADORES.remove(JUGADOR2.toString());
            JUGADORES.add(JUGADOR1.toString2());
            JUGADORES.add(JUGADOR2.toString2());

            EDIT_PREFS.putStringSet("jugadores", JUGADORES);
            EDIT_PREFS.apply();

        }else{
            Toast.makeText(this, "NO TIENE DATOS AUN", Toast.LENGTH_SHORT).show();

        }
    }

    //Crea el menu de opciones
    public boolean onCreateOptionsMenu(Menu menu){

        this.getMenuInflater().inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){

        //Opcion registrar nuevo jugador
        if(item.getItemId() == R.id.idMenuRegistrar){

            registra();

            //Opcion Consultar jugadores
        }else if(item.getItemId() == R.id.idMenuConsultar){

            consultar();
        }

        return super.onOptionsItemSelected(item);
    }

    private void registra() {
        this.startActivityForResult(new Intent(this, RegistroActivity.class), RQ_REGISTRO_ACTIVITY);
    }

    private void iniciaTorneo() {

        this.startActivityForResult(new Intent(this, ListaJugActivity.class), RQ_INICIA_TORNEO);
    }

    private void consultar() {

        this.startActivityForResult(new Intent(this, ConsultaJugActivity.class), RQ_CONSULTA_ACTIVITY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RQ_REGISTRO_ACTIVITY && resultCode == RESULT_OK) { //Si viene del registro y con un resultado OK
            //Recupera los datos introducidos en el RegistroActivity
            String nombre = data.getExtras().getString("nombre");
            String apellidos = data.getExtras().getString("apellidos");

            //Crea un jugador con los datos
            Jugador nuevoJug = new Jugador(nombre, apellidos);

            //Crea las preferencias a almacenar
            final SharedPreferences.Editor EDIT_PREFS = this.getSharedPreferences("prefs", MODE_PRIVATE).edit();
            final SharedPreferences PREFS = this.getSharedPreferences("prefs", MODE_PRIVATE);

            final Set<String> JUGADORES = PREFS.getStringSet("jugadores", new HashSet<String>());
            //Jugador nuevoJug2 = new Jugador("Perico", "Lazaro Jose", 2, 3);
            JUGADORES.add(nuevoJug.toString());
            //JUGADORES.add(nuevoJug2.toString2());
            EDIT_PREFS.putStringSet("jugadores", JUGADORES);
            EDIT_PREFS.apply();

        }
    }
}
