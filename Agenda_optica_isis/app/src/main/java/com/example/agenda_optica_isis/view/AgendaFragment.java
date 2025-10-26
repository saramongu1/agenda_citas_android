package com.example.agenda_optica_isis.view;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.agenda_optica_isis.R;
import com.example.agenda_optica_isis.view.CitaAdapter;
import com.example.agenda_optica_isis.model.Cita;
import com.example.agenda_optica_isis.presenter.PresenterAgendaFragment;

import java.util.ArrayList;
import java.util.List;

public class AgendaFragment extends Fragment {

    private RecyclerView recyclerCitas;
    private Button btnCargarMas;
    private CitaAdapter adapter;
    private PresenterAgendaFragment presenter;
    private final List<Cita> listaCitas = new ArrayList<>();

    public AgendaFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_agenda, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerCitas = view.findViewById(R.id.recyclerCitas);
        btnCargarMas = view.findViewById(R.id.btnCargarMas);
        recyclerCitas.setLayoutManager(new LinearLayoutManager(requireContext()));

        presenter = new PresenterAgendaFragment(this);

        // ✅ Ajustado al constructor correcto del CitaAdapter
        adapter = new CitaAdapter(requireContext(), listaCitas, cita -> {
            if (getActivity() instanceof MenuActivity) {
                DetalleCitaFragment detalle = DetalleCitaFragment.newInstance(cita.getId());
                ((MenuActivity) getActivity()).replaceFragment(detalle);
            }
        });

        recyclerCitas.setAdapter(adapter);

        btnCargarMas.setOnClickListener(v -> presenter.cargarSiguientePagina());
        presenter.cargarInicial();
    }

    // ✅ Métodos llamados desde el presenter:
    public void mostrarCitasInicial(List<Cita> citas) {
        listaCitas.clear();
        listaCitas.addAll(citas);
        adapter.notifyDataSetChanged();
    }

    public void agregarMasCitas(List<Cita> siguientes) {
        int startPos = listaCitas.size();
        listaCitas.addAll(siguientes);
        adapter.notifyItemRangeInserted(startPos, siguientes.size());
    }

    public void mostrarBtnCargarMas(boolean mostrar) {
        btnCargarMas.setVisibility(mostrar ? View.VISIBLE : View.GONE);
    }
}
