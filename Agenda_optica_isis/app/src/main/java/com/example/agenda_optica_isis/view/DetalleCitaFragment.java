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
    private final Calendar calendar = Calendar.getInstance();



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
        deshabilitarSpinner();

        presenter = new PresenterDetalleCitaFragment(this);
        configurarSpinnerOptometras();
        configurarSpinnerConsultorios();

        btnEditar.setOnClickListener(v -> editarCita());
        btnGuardarCambios.setOnClickListener(v -> guardarCambios());
        btnCancelarCita.setOnClickListener(v-> cancelarCita());
        tvFechaCita.setOnClickListener(v -> mostrarSelectorFecha());
        tvHoraCita.setOnClickListener(v -> mostrarSelectorHora());



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

    private void mostrarSelectorFecha() {
        final Calendar fechaActual = Calendar.getInstance();
        int año = fechaActual.get(Calendar.YEAR);
        int mes = fechaActual.get(Calendar.MONTH);
        int dia = fechaActual.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    tvFechaCita.setText(sdf.format(calendar.getTime()));
                },
                año, mes, dia);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void mostrarSelectorHora() {
        final Calendar horaActual = Calendar.getInstance();
        int hora = horaActual.get(Calendar.HOUR_OF_DAY);
        int minuto = horaActual.get(Calendar.MINUTE);
        int minutosRedondeadosInicial = (minuto / 15) * 15;

        // Usar MaterialTimePicker que no tiene botón de teclado
        com.google.android.material.timepicker.MaterialTimePicker timePicker =
                new com.google.android.material.timepicker.MaterialTimePicker.Builder()
                        .setTimeFormat(com.google.android.material.timepicker.TimeFormat.CLOCK_24H)
                        .setHour(hora)
                        .setMinute(minutosRedondeadosInicial)
                        .setTitleText("Seleccionar Hora")
                        .setInputMode(com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK)
                        .build();

        timePicker.addOnPositiveButtonClickListener(dialog -> {
            int selectedHour = timePicker.getHour();
            int selectedMinute = timePicker.getMinute();

            int minutosRedondeados = (selectedMinute / 15) * 15;

            // Validar rango horario
            if (selectedHour < 6) {
                selectedHour = 6;
                minutosRedondeados = 0;
                Toast.makeText(requireContext(), "La hora mínima permitida es 6:00", Toast.LENGTH_SHORT).show();
            } else if (selectedHour > 20 || (selectedHour == 20 && minutosRedondeados > 0)) {
                selectedHour = 20;
                minutosRedondeados = 0;
                Toast.makeText(requireContext(), "La hora máxima permitida es 20:00", Toast.LENGTH_SHORT).show();
            }

            String horaFormateada = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, minutosRedondeados);
            tvHoraCita.setText(horaFormateada);
        });

        timePicker.show(getParentFragmentManager(), "TIME_PICKER");
    }




}
