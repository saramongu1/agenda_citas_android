package com.example.agenda_optica_isis.view;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda_optica_isis.R;

import java.util.List;

public class HoraAdapter extends RecyclerView.Adapter<HoraAdapter.HoraViewHolder> {

    private List<String> horas;
    private static final int HORA_INICIO = 6;
    private static final int HORA_FIN = 19;

    public HoraAdapter() {
        // Generar lista de horas de 6:00 a 19:00
        horas = new java.util.ArrayList<>();
        for (int i = HORA_INICIO; i <= HORA_FIN; i++) {
            horas.add(String.format("%02d:00", i));
        }
    }

    @NonNull
    @Override
    public HoraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hora, parent, false);
        return new HoraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HoraViewHolder holder, int position) {
        holder.tvHora.setText(horas.get(position));
    }

    @Override
    public int getItemCount() {
        return horas.size();
    }

    static class HoraViewHolder extends RecyclerView.ViewHolder {
        TextView tvHora;
        View columnaCitas;

        public HoraViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHora = itemView.findViewById(R.id.tvHora);
            columnaCitas = itemView.findViewById(R.id.columnaCitas);
        }
    }
}
