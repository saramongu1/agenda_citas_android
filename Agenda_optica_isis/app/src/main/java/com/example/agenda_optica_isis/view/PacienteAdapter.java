package com.example.agenda_optica_isis.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda_optica_isis.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PacienteAdapter extends RecyclerView.Adapter<PacienteAdapter.PacienteViewHolder> {

    private List<Map.Entry<String, String>> listaPacientes;
    private OnPacienteClickListener listener;

    public interface OnPacienteClickListener {
        void onPacienteClick(String documento);
    }

    public PacienteAdapter(HashMap<String, String> pacientesMap, OnPacienteClickListener listener) {
        this.listaPacientes = new ArrayList<>(pacientesMap.entrySet());
        this.listener = listener;
    }

    @NonNull
    @Override
    public PacienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_paciente, parent, false);
        return new PacienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PacienteViewHolder holder, int position) {
        Map.Entry<String, String> paciente = listaPacientes.get(position);
        String documento = paciente.getKey();
        String nombre = paciente.getValue();

        holder.tvNombre.setText(nombre);
        holder.tvDocumento.setText("Documento: " + documento);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPacienteClick(documento);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaPacientes.size();
    }

    static class PacienteViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDocumento;

        public PacienteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombrePaciente);
            tvDocumento = itemView.findViewById(R.id.tvDocumentoPaciente);
        }
    }
}

