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

public class OptometraAdapter extends RecyclerView.Adapter<OptometraAdapter.OptometraViewHolder> {
    private List<Map.Entry<String, String>> listaOptometras;
    private OnOptometraClickListener listener;

    public interface OnOptometraClickListener {
        void onOptometraClick(String documento);
    }

    public OptometraAdapter(HashMap<String, String> optometrasMap, OnOptometraClickListener listener) {
        this.listaOptometras = new ArrayList<>(optometrasMap.entrySet());
        this.listener = listener;
    }

    @NonNull
    @Override
    public OptometraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_optometra, parent, false);
        return new OptometraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OptometraViewHolder holder, int position) {
        Map.Entry<String, String> Optometra = listaOptometras.get(position);
        String documento = Optometra.getKey();
        String nombre = Optometra.getValue();

        holder.tvNombre.setText(nombre);
        holder.tvDocumento.setText("Documento: " + documento);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOptometraClick(documento);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaOptometras.size();
    }

    static class OptometraViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDocumento;

        public OptometraViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreOptometra);
            tvDocumento = itemView.findViewById(R.id.tvDocumentoOptometra);
        }
    }
}
