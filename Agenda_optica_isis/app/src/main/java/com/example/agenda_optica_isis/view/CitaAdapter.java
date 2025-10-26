package com.example.agenda_optica_isis.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda_optica_isis.R;
import com.example.agenda_optica_isis.model.Cita;
import com.example.agenda_optica_isis.model.EstadoCita;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class CitaAdapter extends RecyclerView.Adapter<CitaAdapter.CitaViewHolder> {

    private final List<Cita> listaCitas;
    private final OnCitaClickListener listener;
    private final Context context;
    public interface OnCitaClickListener {
        void onCitaClick(Cita cita);
    }

    public CitaAdapter(Context context, List<Cita> listaCitas, OnCitaClickListener listener) {
        this.context = context;
        this.listaCitas = listaCitas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cita, parent, false);
        return new CitaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CitaViewHolder holder, int position) {
        Cita cita = listaCitas.get(position);

        // Formatear fecha y hora
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

        holder.tvIdCita.setText("ID: " + cita.getId());
        holder.tvPacienteCita.setText("Paciente: " + cita.getDocumento_paciente());
        holder.tvDocumentoCita.setText("Documento: " + cita.getDocumento_paciente());
        holder.tvOptometraCita.setText("Optómetra: " + cita.getDocumento_optometra());
        holder.tvFechaHoraCita.setText("Fecha: " + cita.getFecha().format(formatoFecha) +
                " " + cita.getHora().format(formatoHora));
        holder.tvEstadoCita.setText("Estado: " + cita.getEstadoCita().name());

        // Color lateral según estado
        holder.estadoCitaView.setBackgroundColor(colorPorEstado(cita.getEstadoCita()));

        // Click
        holder.itemView.setOnClickListener(v -> listener.onCitaClick(cita));
    }

    @Override
    public int getItemCount() {
        return listaCitas.size();
    }

    // Método auxiliar para cambiar el color según el estado
    private int colorPorEstado(EstadoCita estado) {
        switch (estado) {
            case PROGRAMADA:
                return Color.parseColor("#4CAF50"); // verde
            case EN_PROGRESO:
                return Color.parseColor("#2196F3"); // azul
            case COMPLETADA:
                return Color.parseColor("#9C27B0"); // morado
            case CANCELADA:
                return Color.parseColor("#F44336"); // rojo
            case NO_ASISTIO:
                return Color.parseColor("#FF9800"); // naranja
            default:
                return Color.GRAY;
        }
    }

    public static class CitaViewHolder extends RecyclerView.ViewHolder {
        TextView tvIdCita, tvPacienteCita, tvDocumentoCita, tvOptometraCita, tvFechaHoraCita, tvEstadoCita;
        View estadoCitaView;
        CardView cardCita;

        public CitaViewHolder(@NonNull View itemView) {
            super(itemView);
            cardCita = itemView.findViewById(R.id.cardCita);
            estadoCitaView = itemView.findViewById(R.id.estadoCitaView);
            tvIdCita = itemView.findViewById(R.id.tvIdCita);
            tvPacienteCita = itemView.findViewById(R.id.tvPacienteCita);
            tvDocumentoCita = itemView.findViewById(R.id.tvDocumentoCita);
            tvOptometraCita = itemView.findViewById(R.id.tvOptometraCita);
            tvFechaHoraCita = itemView.findViewById(R.id.tvFechaHoraCita);
            tvEstadoCita = itemView.findViewById(R.id.tvEstadoCita);
        }
    }
}
