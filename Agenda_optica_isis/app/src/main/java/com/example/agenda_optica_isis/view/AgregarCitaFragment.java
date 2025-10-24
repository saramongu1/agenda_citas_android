package com.example.agenda_optica_isis.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import com.example.agenda_optica_isis.R;
import com.example.agenda_optica_isis.presenter.PresenterAgregarCitaFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class AgregarCitaFragment extends Fragment {

    private TextInputEditText etBuscarPaciente, etFecha, etHora;
    private Spinner spnCriterioBusqueda, spnOptometra;
    private ImageButton btnBuscarPaciente;
    private FloatingActionButton btnAgregarPaciente;
    private RecyclerView recyclerPacientes;
    private PacienteAdapter adapter;

    private PresenterAgregarCitaFragment presenter;

    private final Calendar calendar = Calendar.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_agregar_cita, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        enlazarVistas(view);
        configurarSpinnerBusqueda();
        configurarSpinnerOptometras();
        iniciarPresenter();

        btnBuscarPaciente.setOnClickListener(v -> buscarPaciente());
        btnAgregarPaciente.setOnClickListener(v -> abrirAgregarPaciente());
        etFecha.setOnClickListener(v -> mostrarSelectorFecha());
        etHora.setOnClickListener(v -> mostrarSelectorHora());
    }

    private void iniciarPresenter() {
        presenter = new PresenterAgregarCitaFragment(this);
    }

    private void enlazarVistas(@NonNull View view) {
        etBuscarPaciente = view.findViewById(R.id.inputBuscarPaciente);
        etFecha = view.findViewById(R.id.inputFechaCita);
        etHora = view.findViewById(R.id.inputHoraCita);
        spnCriterioBusqueda = view.findViewById(R.id.spinnerTipoBusqueda);
        spnOptometra = view.findViewById(R.id.spinnerOptometra);
        btnBuscarPaciente = view.findViewById(R.id.btnBuscarPaciente);
        btnAgregarPaciente = view.findViewById(R.id.btnAgregarPaciente);
        recyclerPacientes = view.findViewById(R.id.recyclerPacientes);
        recyclerPacientes.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void configurarSpinnerBusqueda() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.tipos_busqueda,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCriterioBusqueda.setAdapter(adapter);
    }

    private void configurarSpinnerOptometras() {
        // Temporal: se puede llenar desde el presenter más adelante
        String[] optometras = {"Seleccione un optómetra", "Laura Gómez", "Carlos Pérez", "Ana Morales"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                optometras
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnOptometra.setAdapter(adapter);
    }

    private void mostrarSelectorFecha() {
        final Calendar fechaActual = Calendar.getInstance();
        int año = fechaActual.get(Calendar.YEAR);
        int mes = fechaActual.get(Calendar.MONTH);
        int dia = fechaActual.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    etFecha.setText(sdf.format(calendar.getTime()));
                },
                año, mes, dia);
        // No permitir fechas pasadas
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void mostrarSelectorHora() {
        final Calendar horaActual = Calendar.getInstance();
        int hora = horaActual.get(Calendar.HOUR_OF_DAY);
        int minuto = horaActual.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                (view, hourOfDay, minute1) -> {
                    // Redondear a múltiplos de 10
                    int minutosRedondeados = (minute1 / 10) * 10;
                    String horaFormateada = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minutosRedondeados);
                    etHora.setText(horaFormateada);
                }, hora, minuto, true);
        timePickerDialog.show();
    }

    private void buscarPaciente() {
        presenter.buscarPaciente();
    }

    private void abrirAgregarPaciente() {
        if (getActivity() instanceof MenuActivity) {
            ((MenuActivity) getActivity()).replaceFragment(new AgregarPacienteFragment());
        }
    }

    public void mostrarListaPacientes(HashMap<String, String> listaPacientes) {
        adapter = new PacienteAdapter(listaPacientes, documento -> {
            // Selección de paciente desde la lista
            Toast.makeText(requireContext(), "Paciente seleccionado: " + documento, Toast.LENGTH_SHORT).show();
        });
        recyclerPacientes.setAdapter(adapter);
    }

    public String getTextoBusquedaPaciente() {
        return etBuscarPaciente.getText() != null ? etBuscarPaciente.getText().toString().trim() : "";
    }

    public String getCriterioBusqueda() {
        return spnCriterioBusqueda.getSelectedItem().toString();
    }

    public void mostrarMensaje(String mensaje) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
    }
}
