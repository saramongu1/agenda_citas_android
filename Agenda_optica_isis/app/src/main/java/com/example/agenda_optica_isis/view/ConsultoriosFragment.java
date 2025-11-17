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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.agenda_optica_isis.R;
import com.example.agenda_optica_isis.presenter.PresenterConsultoriosFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;

public class ConsultoriosFragment extends Fragment {

    private TextInputEditText etBusquedaConsultorio;
    private Spinner spnCriterioBusquedaConsultorio;
    private ImageButton iBtnBuscarConsultorio;
    private FloatingActionButton fBtnAgregarConsultorio;
    private RecyclerView recyclerConsultorios;
    private ConsultorioAdapter adapter;
    private PresenterConsultoriosFragment presenter;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_consultorios, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        enlazarVistas(view);
        configurarSpinner();
        iniciarPresenter();
        presenter.cargarConsultorios();

        iBtnBuscarConsultorio.setOnClickListener(v -> buscarConsultorio());
        fBtnAgregarConsultorio.setOnClickListener(v -> agregarConsultorio());
        swipeRefreshLayout.setOnRefreshListener(() -> {recargarFragment();});
    }

    private void recargarFragment() {
        if (getActivity() instanceof MenuActivity) {
            ((MenuActivity) getActivity()).replaceFragment(new ConsultoriosFragment());
        }
        swipeRefreshLayout.setRefreshing(false);
    }


    public void iniciarPresenter(){
        presenter = new PresenterConsultoriosFragment(this);
    }

    private void buscarConsultorio() {
        presenter.cargarBusquedaConsultorio();
    }

    private void agregarConsultorio() {
        if (getActivity() instanceof MenuActivity) {
            ((MenuActivity) getActivity()).replaceFragment(new AgregarConsultorioFragment());
        }
    }

    public void mostrarMensaje(String mensaje) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public void mostrarListaConsultorios(HashMap<String, String> listaConsultorios) {
        adapter = new ConsultorioAdapter(listaConsultorios, idConsultorio -> {
            if (getActivity() instanceof MenuActivity) {
                DetalleConsultorioFragment detalle = DetalleConsultorioFragment.nuevaInstancia(idConsultorio);
                ((MenuActivity) getActivity()).replaceFragment(detalle);
            }
        });
        recyclerConsultorios.setAdapter(adapter);
    }

    public String getTextoBusquedaConsultorio(){
        return etBusquedaConsultorio.getText() != null ? etBusquedaConsultorio.getText().toString().trim() : "";
    }

    public String getTextoCriterioBusqueda(){
        return spnCriterioBusquedaConsultorio.getSelectedItem().toString();
    }

    private void enlazarVistas(@NonNull View view) {
        etBusquedaConsultorio = view.findViewById(R.id.inputBuscarConsultorio);
        spnCriterioBusquedaConsultorio = view.findViewById(R.id.spinnerTipoBusquedaConsultorio);
        iBtnBuscarConsultorio = view.findViewById(R.id.btnBuscarConsultorio);
        fBtnAgregarConsultorio = view.findViewById(R.id.btnAgregarConsultorio);
        recyclerConsultorios = view.findViewById(R.id.recyclerConsultorios);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshConsultorios);
        recyclerConsultorios.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void configurarSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.tipos_busqueda_consultorio,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCriterioBusquedaConsultorio.setAdapter(adapter);
    }
}