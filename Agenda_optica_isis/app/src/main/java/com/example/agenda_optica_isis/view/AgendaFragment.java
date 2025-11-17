package com.example.agenda_optica_isis.view;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.agenda_optica_isis.R;
import com.example.agenda_optica_isis.presenter.PresenterAgendaFragment;

import java.util.ArrayList;
import java.util.List;

public class AgendaFragment extends Fragment {

    private RecyclerView recyclerCitas;
    private CitaAdapter adapter;
    private PresenterAgendaFragment presenter;
    private Button btnCargarMas;
    private SwipeRefreshLayout swipeRefreshLayout;


    public AgendaFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_agenda, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerCitas = view.findViewById(R.id.recyclerCitas);
        btnCargarMas = view.findViewById(R.id.btnCargarMas);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshAgenda);
        swipeRefreshLayout.setOnRefreshListener(() -> {recargarFragment();});


        recyclerCitas.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new CitaAdapter(requireContext(), new ArrayList<>(), idCita -> {
            navegarADetalleCita(idCita);
        });

        recyclerCitas.setAdapter(adapter);

        presenter = new PresenterAgendaFragment(this);
        presenter.cargarInicial();

        btnCargarMas.setOnClickListener(v -> presenter.cargarSiguientePagina());
    }

    private void recargarFragment() {
        if (getActivity() instanceof MenuActivity) {
            ((MenuActivity) getActivity()).replaceFragment(new AgendaFragment());
        }
        swipeRefreshLayout.setRefreshing(false);
    }


    public void mostrarCitasInicial(List<CitaUI> lista) {
        adapter = new CitaAdapter(requireContext(), lista, idCita -> navegarADetalleCita(idCita));
        recyclerCitas.setAdapter(adapter);
    }

    public void agregarMasCitas(List<CitaUI> lista) {
        int posicionInicial = adapter.getItemCount();
        adapter.notifyItemRangeInserted(posicionInicial, lista.size());
    }

    public void mostrarBtnCargarMas(boolean visible) {
        btnCargarMas.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void navegarADetalleCita(String idCita) {
        DetalleCitaFragment detalle = DetalleCitaFragment.newInstance(idCita);

        if (getActivity() instanceof MenuActivity) {
            ((MenuActivity) getActivity()).replaceFragment(detalle);
        }
    }
}
