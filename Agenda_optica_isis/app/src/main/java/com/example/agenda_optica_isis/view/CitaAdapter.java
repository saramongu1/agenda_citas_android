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
import com.example.agenda_optica_isis.view.CitaUI;

import java.util.List;

public class CitaAdapter extends RecyclerView.Adapter<CitaAdapter.CitaViewHolder> {

    private final List<CitaUI> listaCitas;
    private final OnCitaClickListener listener;
    private final Context context;

    public interface OnCitaClickListener {
        void onCitaClick(String idCita);
    }

    public CitaAdapter(Context context, List<CitaUI> listaCitas, OnCitaClickListener listener) {
        this.context = context;
        this.listaCitas = listaCitas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cita, parent, false);
        return new CitaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CitaViewHolder holder, int position) {
        CitaUI cita = listaCitas.get(position);

        holder.tvIdCita.setText("ID: " + cita.getId());
        holder.tvPacienteCita.setText("Paciente: " + cita.getNombrePaciente());
        holder.tvDocumentoCita.setText("Documento: " + cita.getDocumentoPaciente());
        holder.tvOptometraCita.setText("Optómetra: " + cita.getNombreOptometra());
        holder.tvFechaHoraCita.setText("Fecha: " + cita.getFechaHora());
        holder.tvEstadoCita.setText("Estado: " + cita.getEstado());

        holder.estadoCitaView.setBackgroundColor(colorPorEstado(cita.getEstado()));

        holder.itemView.setOnClickListener(v -> listener.onCitaClick(cita.getId()));
    }

    @Override
    public int getItemCount() {
        return listaCitas.size();
    }

    private int colorPorEstado(String estado) {
        switch (estado) {
            case "PROGRAMADA": return Color.parseColor("#4CAF50");
            case "EN_PROGRESO": return Color.parseColor("#2196F3");
            case "COMPLETADA": return Color.parseColor("#9C27B0");
            case "CANCELADA": return Color.parseColor("#F44336");
            case "NO_ASISTIO": return Color.parseColor("#FF9800");
            default: return Color.GRAY;
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
            tvIdCita = itemView.findViewById(R.id.tvIdCitaAdapter);
            tvPacienteCita = itemView.findViewById(R.id.tvPacienteCitaAdapter);
            tvDocumentoCita = itemView.findViewById(R.id.tvDocumentoPacienteCitaAdapter);
            tvOptometraCita = itemView.findViewById(R.id.tvOptometraCitaAdapter);
            tvFechaHoraCita = itemView.findViewById(R.id.tvFechaHoraCitaAdapter);
            tvEstadoCita = itemView.findViewById(R.id.tvEstadoCitaAdapter);
        }
    }
}
