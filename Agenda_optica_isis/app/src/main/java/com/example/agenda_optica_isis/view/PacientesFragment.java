package com.example.agenda_optica_isis.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.agenda_optica_isis.R;
import com.example.agenda_optica_isis.presenter.PresenterPacientesFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;

public class PacientesFragment extends Fragment {

    private TextInputEditText etBusquedaPaciente;
    private Spinner spnCriterioBusquedaPaciente;
    private ImageButton iBtnBuscarPaciente;
    private FloatingActionButton fBtnAgregarPaciente;
    private RecyclerView recyclerPacientes;
    private PacienteAdapter adapter;
    private PresenterPacientesFragment presenter;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pacientes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        enlazarVistas(view);
        configurarSpinner();
        iniciarPresenter();
        presenter.cargarPacientes();

        iBtnBuscarPaciente.setOnClickListener(v -> buscarPaciente());
        fBtnAgregarPaciente.setOnClickListener(v -> agregarPaciente());
        swipeRefreshLayout.setOnRefreshListener(() -> {recargarFragment();});
    }

    public void iniciarPresenter(){
        presenter = new PresenterPacientesFragment(this);
    }

    private void buscarPaciente() {
        presenter.cargarBusquedaPaciente();
    }

    private void recargarFragment() {
        if (getActivity() instanceof MenuActivity) {
            ((MenuActivity) getActivity()).replaceFragment(new PacientesFragment());
        }
        swipeRefreshLayout.setRefreshing(false);
    }




    private void agregarPaciente() {
        if (getActivity() instanceof MenuActivity) {
            ((MenuActivity) getActivity()).replaceFragment(new AgregarPacienteFragment());
        }
    }

    public void mostrarMensaje(String mensaje) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public void mostrarListaPacientes(HashMap<String, String> listaPacientes) {
        adapter = new PacienteAdapter(listaPacientes, documento -> {
            if (getActivity() instanceof MenuActivity) {
                DetallePacienteFragment detalle = DetallePacienteFragment.nuevaInstancia(documento);
                ((MenuActivity) getActivity()).replaceFragment(detalle);
            }
        });
        recyclerPacientes.setAdapter(adapter);
    }

    public String getTextoBusquedaPaciente(){
        return etBusquedaPaciente.getText() != null ? etBusquedaPaciente.getText().toString().trim() : "";
    }

    public String getTextoCriterioBusqueda(){
        return spnCriterioBusquedaPaciente.getSelectedItem().toString();
    }

    private void enlazarVistas(@NonNull View view) {
        etBusquedaPaciente = view.findViewById(R.id.inputBuscarPaciente);
        spnCriterioBusquedaPaciente = view.findViewById(R.id.spinnerTipoBusqueda);
        iBtnBuscarPaciente = view.findViewById(R.id.btnBuscarPaciente);
        fBtnAgregarPaciente = view.findViewById(R.id.btnAgregarPaciente);
        recyclerPacientes = view.findViewById(R.id.recyclerPacientes);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshPacientes);
        recyclerPacientes.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void configurarSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.tipos_busqueda,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCriterioBusquedaPaciente.setAdapter(adapter);
    }
}
