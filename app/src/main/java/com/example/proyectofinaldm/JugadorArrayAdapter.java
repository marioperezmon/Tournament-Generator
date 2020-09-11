package com.example.proyectofinaldm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class JugadorArrayAdapter extends ArrayAdapter<Jugador> {

    public JugadorArrayAdapter(Context context, ArrayList <Jugador> listaJug){
        super(context, 0, listaJug);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final Jugador jug = this.getItem(position);

        if(convertView == null){
            final LayoutInflater INFLATER = LayoutInflater.from(this.getContext());

            convertView = INFLATER.inflate(R.layout.entrada_jugador, null);

        }

        final TextView label_jug = convertView.findViewById(R.id.label_jugador);

        label_jug.setText(jug.getNombre() + " " + jug.getApellidos());

        final TextView label_victoria = convertView.findViewById(R.id.label_victorias);

        String vicJug = String.valueOf(jug.getNumVictorias());

        label_victoria.setText(vicJug);

        final TextView label_torneo = convertView.findViewById(R.id.label_torneos);

        String torneoJug = String.valueOf(jug.getNumTorneos());

        label_torneo.setText(torneoJug);

        CheckBox checkView = convertView.findViewById(R.id.cb_seleccionado);
        checkView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                jug.setParticipa(b);
                jug.setEliminar(b);
            }
        });
        return convertView;
    }
}
