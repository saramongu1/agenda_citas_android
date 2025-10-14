package com.example.agenda_optica_isis.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.agenda_optica_isis.R;

public class CalendarioFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendario, container, false);

        TableLayout tableLayout = view.findViewById(R.id.tableLayoutAgenda);

        // Horas que quieres mostrar (puedes cambiar el rango)
        String[] horas = {
                "8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM",
                "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM",
                "4:00 PM", "5:00 PM", "6:00 PM"
        };

        for (String hora : horas) {
            TableRow fila = new TableRow(getContext());
            fila.setPadding(0, 30, 0, 16);

            // Texto con la hora
            TextView tvHora = new TextView(getContext());
            tvHora.setText(hora);
            tvHora.setTextSize(16);
            tvHora.setTextColor(getResources().getColor(R.color.grisMasOscuro));
            tvHora.setPadding(16, 0, 16, 0);

            // Línea horizontal que simula la franja de la cita
            View linea = new View(getContext());
            TableRow.LayoutParams paramsLinea = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    2
            );
            paramsLinea.setMargins(8, 0, 8, 0);
            linea.setLayoutParams(paramsLinea);
            linea.setBackgroundColor(getResources().getColor(R.color.grisNormal));

            // Agregamos la hora y la línea al TableRow
            fila.addView(tvHora);
            fila.addView(linea);

            // Añadimos la fila completa al TableLayout
            tableLayout.addView(fila);
        }

        return view;
    }
}
