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
import com.example.agenda_optica_isis.presenter.PresenterOptometrasFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;

public class OptometrasFragment extends Fragment {

    private TextInputEditText etBusquedaOptometra;
    private Spinner spnCriterioBusquedaOptometra;
    private ImageButton iBtnBuscarOptometra;
    private FloatingActionButton fBtnAgregarOptometra;

    private RecyclerView recyclerOptometras;
    private OptometraAdapter adapter;
    private PresenterOptometrasFragment presenter;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_optometras, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        enlazarVistas(view);
        configurarSpinner();
        iniciarPresenter();
        presenter.cargarOptometras();

        iBtnBuscarOptometra.setOnClickListener(v -> buscarOptometra());
        fBtnAgregarOptometra.setOnClickListener(v -> agregarOptometra());
        swipeRefreshLayout.setOnRefreshListener(() -> {recargarFragment();});

    }

    private void recargarFragment() {
        if (getActivity() instanceof MenuActivity) {
            ((MenuActivity) getActivity()).replaceFragment(new OptometrasFragment());
        }
        swipeRefreshLayout.setRefreshing(false);
    }



    private void iniciarPresenter() {
        presenter = new PresenterOptometrasFragment(this);
    }

    private void enlazarVistas(@NonNull View view) {
        etBusquedaOptometra = view.findViewById(R.id.inputBuscarOptometra);
        spnCriterioBusquedaOptometra = view.findViewById(R.id.spinnerTipoBusquedaOptometra);
        iBtnBuscarOptometra = view.findViewById(R.id.btnBuscarOptometra);
        recyclerOptometras = view.findViewById(R.id.recyclerOptometras);
        fBtnAgregarOptometra = view.findViewById(R.id.btnAgregarOptometra);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshOptometras);
        recyclerOptometras.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void configurarSpinner() {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.tipos_busqueda,
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCriterioBusquedaOptometra.setAdapter(spinnerAdapter);
    }

    public void mostrarListaOptometras(HashMap<String, String> listaOptometras) {
        adapter = new OptometraAdapter(listaOptometras, documento -> {
            if (getActivity() instanceof MenuActivity) {
                DetalleOptometraFragment detalle = DetalleOptometraFragment.nuevaInstancia(documento);
                ((MenuActivity) getActivity()).replaceFragment(detalle);
            }
        });
        recyclerOptometras.setAdapter(adapter);
    }

    private void buscarOptometra() {
        presenter.cargarBusquedaOptometra();
    }

    public void agregarOptometra(){
        if (getActivity() instanceof MenuActivity) {
            ((MenuActivity) getActivity()).replaceFragment(new AgregarOptometraFragment());
        }
    }

    public String getTextoBusquedaOptometra(){
        return etBusquedaOptometra.getText() != null ? etBusquedaOptometra.getText().toString().trim() : "";
    }

    public String getTextoCriterioBusqueda(){
        return spnCriterioBusquedaOptometra.getSelectedItem().toString();
    }

    public void mostrarMensaje(String mensaje) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

}
