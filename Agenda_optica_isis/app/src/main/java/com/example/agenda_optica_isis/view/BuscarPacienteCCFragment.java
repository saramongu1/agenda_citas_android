package com.example.agenda_optica_isis.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.agenda_optica_isis.R;
import com.example.agenda_optica_isis.presenter.PresenterBuscarPacientesCCFragment;
import com.example.agenda_optica_isis.presenter.PresenterPacientesFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;

public class BuscarPacienteCCFragment extends Fragment {

    private TextInputEditText etBusquedaPaciente;
    private Spinner spnCriterioBusquedaPaciente;
    private ImageButton iBtnBuscarPaciente;
    private FloatingActionButton fBtnAgregarPaciente;
    private RecyclerView recyclerPacientes;
    private PacienteAdapter adapter;
    private PresenterBuscarPacientesCCFragment presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buscar_paciente_c_c, container, false);
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
    }

    public void iniciarPresenter() {
        presenter = new PresenterBuscarPacientesCCFragment(this);
    }

    private void buscarPaciente() {
        presenter.cargarBusquedaPaciente();
    }

    private void agregarPaciente() {
        if (getActivity() instanceof MenuActivity) {
            ((MenuActivity) getActivity()).replaceFragment(new CrearPacienteCCFragment());
        }
    }

    public void mostrarMensaje(String mensaje) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public void mostrarListaPacientes(HashMap<String, String> listaPacientes) {
        adapter = new PacienteAdapter(listaPacientes, documento -> {
            if (getActivity() instanceof MenuActivity) {
                String nombre = listaPacientes.get(documento);
                MenuActivity activity = (MenuActivity) getActivity();

                FragmentManager fm = activity.getSupportFragmentManager();
                // Buscamos si ya existe un fragmento de agregar cita
                AgregarCitaFragment fragmentExistente = (AgregarCitaFragment) fm.findFragmentByTag("AGREGAR_CITA");

                if (fragmentExistente != null && fragmentExistente.isVisible()) {
                    // ✅ Si ya existe, solo actualizamos los datos del paciente
                    fragmentExistente.actualizarPaciente(nombre, documento);
                } else {
                    // ✅ Si no existe, creamos una nueva instancia y mostramos
                    AgregarCitaFragment nuevoFragment = AgregarCitaFragment.nuevaInstancia(nombre, documento);
                    activity.mostrarFragmentConDatos(nuevoFragment, "AGREGAR_CITA");
                }
            }
        });
        recyclerPacientes.setAdapter(adapter);
    }

    public String getTextoBusquedaPaciente() {
        return etBusquedaPaciente.getText() != null ? etBusquedaPaciente.getText().toString().trim() : "";
    }

    public String getTextoCriterioBusqueda() {
        return spnCriterioBusquedaPaciente.getSelectedItem().toString();
    }

    private void enlazarVistas(@NonNull View view) {
        etBusquedaPaciente = view.findViewById(R.id.inputBuscarPacienteCC);
        spnCriterioBusquedaPaciente = view.findViewById(R.id.spinnerTipoBusquedaCC);
        iBtnBuscarPaciente = view.findViewById(R.id.btnBuscarPacienteCC);
        fBtnAgregarPaciente = view.findViewById(R.id.btnAgregarPacienteCC);
        recyclerPacientes = view.findViewById(R.id.recyclerPacientesCC);
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
