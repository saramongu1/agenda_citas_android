package com.example.agenda_optica_isis.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda_optica_isis.R;

import java.util.HashMap;

public class ConsultorioAdapter extends RecyclerView.Adapter<ConsultorioAdapter.ConsultorioViewHolder> {

    private HashMap<String, String> listaConsultorios;
    private final OnConsultorioClickListener listener;

    public interface OnConsultorioClickListener {
        void onConsultorioClick(String idConsultorio);
    }

    public ConsultorioAdapter(HashMap<String, String> listaConsultorios, OnConsultorioClickListener listener) {
        this.listaConsultorios = listaConsultorios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ConsultorioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_consultorio, parent, false);
        return new ConsultorioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsultorioViewHolder holder, int position) {
        String[] keys = listaConsultorios.keySet().toArray(new String[0]);
        String key = keys[position];
        String value = listaConsultorios.get(key);

        // Asumiendo que el value es un string con formato "dirección|ciudad"
        String[] partes = value.split("\\|");
        String direccion = partes.length > 0 ? partes[0] : "";
        String ciudad = partes.length > 1 ? partes[1] : "";

        holder.tvIdConsultorio.setText(key);
        holder.tvDireccionConsultorio.setText(direccion);
        holder.tvCiudadConsultorio.setText(ciudad);

        holder.itemView.setOnClickListener(v -> listener.onConsultorioClick(key));
    }

    @Override
    public int getItemCount() {
        return listaConsultorios.size();
    }

    public void actualizarLista(HashMap<String, String> nuevaLista) {
        this.listaConsultorios = nuevaLista;
        notifyDataSetChanged();
    }

    public static class ConsultorioViewHolder extends RecyclerView.ViewHolder {
        TextView tvIdConsultorio;
        TextView tvDireccionConsultorio;
        TextView tvCiudadConsultorio;

        public ConsultorioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIdConsultorio = itemView.findViewById(R.id.tvIdConsultorio);
            tvDireccionConsultorio = itemView.findViewById(R.id.tvDireccionConsultorio);
            tvCiudadConsultorio = itemView.findViewById(R.id.tvCiudadConsultorio);
        }
    }
}