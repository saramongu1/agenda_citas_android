package com.example.agenda_optica_isis.view;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.agenda_optica_isis.R;
import com.example.agenda_optica_isis.presenter.PresenterDetalleCitaFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DetalleCitaFragment extends Fragment {

    private static final String ARG_ID = "id_cita";

    private TextInputEditText tvIdcita;
    private TextInputEditText tvNombrePaciente;
    private TextInputEditText tvDocumentoPaciente;
    private TextInputEditText tvFechaCita;
    private TextInputEditText tvHoraCita;
    private Spinner spnOptometra;
    private Spinner spnConsultorio;
    private TextInputEditText tvEstadoCita;
    private MaterialButton btnEditar;
    private MaterialButton btnGuardarCambios;
    private MaterialButton btnCancelarCita;

    private PresenterDetalleCitaFragment presenter;


    public static DetalleCitaFragment newInstance(String citaId) {
        DetalleCitaFragment fragment = new DetalleCitaFragment();
        Bundle b = new Bundle();
        b.putString(ARG_ID, citaId);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalle_cita, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        enlazarVistas(view);
        initInputFecha(view);
        initInputHora(view);
        deshabilitarSpinner();

        presenter = new PresenterDetalleCitaFragment(this);
        configurarSpinnerOptometras();
        configurarSpinnerConsultorios();

        btnEditar.setOnClickListener(v -> editarCita());
        btnGuardarCambios.setOnClickListener(v -> guardarCambios());
        btnCancelarCita.setOnClickListener(v-> cancelarCita());

        String idCita = getArguments() != null ? getArguments().getString(ARG_ID) : null;
        if (idCita == null || idCita.isEmpty()) {
            mostrarMensaje("Id de cita inválido");
        } else {
            presenter.cargarDetalleCita(idCita);
        }
    }

    public void deshabilitarSpinner(){
        spnConsultorio.setEnabled(false);
        spnOptometra.setEnabled(false);
    }

    private void enlazarVistas(@NonNull View view) {
        tvIdcita = view.findViewById(R.id.inputIDCitaDetalleCita);
        tvNombrePaciente = view.findViewById(R.id.inputNombrePacienteDetalleCita); // ✅ corregido
        tvDocumentoPaciente = view.findViewById(R.id.inputDocumentoPacienteDetalleCita);
        tvFechaCita = view.findViewById(R.id.inputFechaCitaDetalleCita);
        tvHoraCita = view.findViewById(R.id.inputHoraCitaDetalleCita);
        spnOptometra = view.findViewById(R.id.spinnerOptometraDetalleCita);
        spnConsultorio = view.findViewById(R.id.spinnerConsultorioDetalleCita);
        tvEstadoCita = view.findViewById(R.id.inputEstadoCitaDetalle);
        btnEditar = view.findViewById(R.id.btnEditarCita);
        btnGuardarCambios = view.findViewById(R.id.btnGuardarCambiosCita);
        btnCancelarCita = view.findViewById(R.id.btnCancelarCita);
    }

    public void mostrarDetalle(String id, String nombrePaciente, String documentoPaciente,
                               int nombreOptometra, String fecha, String hora, String estado, int consultorio) {
        tvIdcita.setText(id);
        tvNombrePaciente.setText(nombrePaciente);
        tvDocumentoPaciente.setText(documentoPaciente);
        tvFechaCita.setText(fecha);
        tvHoraCita.setText(hora);
        tvEstadoCita.setText(estado);
        spnOptometra.setSelection(nombreOptometra);
        spnConsultorio.setSelection(consultorio);
    }

    public void guardarCambios() {
        presenter.guardarCambiosEditarCita();
    }

    public void editarCita() {
        btnEditar.setVisibility(GONE);
        btnGuardarCambios.setVisibility(VISIBLE);

        tvFechaCita.setEnabled(true);
        tvHoraCita.setEnabled(true);
        spnOptometra.setEnabled(true);
        spnConsultorio.setEnabled(true);
    }

    public void cancelarCita(){
        presenter.cancelarCita();
    }

    public String getIdCita() { return getText(tvIdcita); }
    public String getNombrePaciente() { return getText(tvNombrePaciente); }
    public String getDocumentoPaciente() { return getText(tvDocumentoPaciente); }
    public String getFechaCita() { return getText(tvFechaCita); }
    public String getHoraCita() { return getText(tvHoraCita); }
    public String getEstadoCita() { return getText(tvEstadoCita); }
    public String getOptometra() { return spnOptometra.getSelectedItem().toString(); }
    public String getConsultorio() { return spnConsultorio.getSelectedItem().toString(); }

    private String getText(TextInputEditText input) {
        return input.getText() != null ? input.getText().toString().trim() : "";
    }

    private void configurarSpinnerOptometras() {
        String[] optometras = presenter.listaOptometras();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, optometras);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnOptometra.setAdapter(adapter);
    }

    private void configurarSpinnerConsultorios() {
        String[] consultorios = presenter.listaConsultorios();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, consultorios);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnConsultorio.setAdapter(adapter);
    }

    public void irAAgendaCitas(){
        if (getActivity() instanceof MenuActivity) {
            ((MenuActivity) getActivity()).replaceFragment(new AgendaFragment());
        }
    }

    public void mostrarMensaje(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void initInputFecha(@NonNull View view){
        EditText inputFecha = view.findViewById(R.id.inputFechaCitaDetalleCita);
        inputFecha.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePicker = new DatePickerDialog(
                    requireContext(),
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        String fechaSeleccionada = String.format("%04d-%02d-%02d",
                                 selectedYear, selectedMonth + 1, selectedDay);
                        inputFecha.setText(fechaSeleccionada);
                    },
                    year,month,day
            );
            datePicker.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePicker.show();
        });
    }

    public void initInputHora(@NonNull View view) {
        EditText inputHora = view.findViewById(R.id.inputHoraCitaDetalleCita);
        inputHora.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePicker = new TimePickerDialog(
                    requireContext(),
                    (view1, selectedHour, selectedMinute) -> {
                        int minutoAjustado = Math.round(selectedMinute / 5f) * 5;
                        if (minutoAjustado == 60) {
                            minutoAjustado = 0;
                            selectedHour = (selectedHour + 1) % 24;
                        }

                        String horaSeleccionada = String.format("%02d:%02d", selectedHour, minutoAjustado);
                        inputHora.setText(horaSeleccionada);
                    },
                    hour,
                    minute,
                    true
            );
            timePicker.show();
        });
    }



}
