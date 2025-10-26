package com.example.agenda_optica_isis.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.agenda_optica_isis.R;
import com.example.agenda_optica_isis.presenter.PresenterAgregarCitaFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
public class AgregarCitaFragment extends Fragment {
    private static String nombrePacienteGuardado;
    private static String documentoPacienteGuardado;
    private static String fechaGuardada;
    private static String horaGuardada;
    private static int optometraSeleccionada;
    private static int consultorioSeleccionado;
    private String nombrePendiente;
    private String documentoPendiente;
    private TextView tvNombrePaciente;
    private TextView tvDocumentoPaciente;
    private TextInputEditText etFecha;
    private TextInputEditText etHora;
    private Spinner spnOptometra;
    private Spinner spnConsultorio;
    private Button btnBuscarPaciente;
    private MaterialButton btnGuardarCita;
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
        iniciarPresenter();
        configurarSpinnerOptometras();
        configurarSpinnerConsultorios();

        btnBuscarPaciente.setOnClickListener(v -> irABuscarPaciente());
        btnGuardarCita.setOnClickListener(v -> guardarCita());
        etFecha.setOnClickListener(v -> mostrarSelectorFecha());
        etHora.setOnClickListener(v -> mostrarSelectorHora());


        llenarPaciente();
        cambiarPaciente();
    }

    @Override
    public void onPause() {
        super.onPause();
        nombrePacienteGuardado = tvNombrePaciente.getText().toString();
        documentoPacienteGuardado = tvDocumentoPaciente.getText().toString();
        fechaGuardada = etFecha.getText().toString();
        horaGuardada = etHora.getText().toString();
        optometraSeleccionada = spnOptometra.getSelectedItemPosition();
        consultorioSeleccionado = spnConsultorio.getSelectedItemPosition();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (nombrePacienteGuardado != null && !nombrePacienteGuardado.isEmpty()) {
            tvNombrePaciente.setText(nombrePacienteGuardado);
        }
        if (documentoPacienteGuardado != null && !documentoPacienteGuardado.isEmpty()) {
            tvDocumentoPaciente.setText(documentoPacienteGuardado);
        }
        if (fechaGuardada != null && !fechaGuardada.isEmpty()) {
            etFecha.setText(fechaGuardada);
        }
        if (horaGuardada != null && !horaGuardada.isEmpty()) {
            etHora.setText(horaGuardada);
        }
        spnOptometra.setSelection(optometraSeleccionada);
        spnConsultorio.setSelection(consultorioSeleccionado);
    }

    public static AgregarCitaFragment nuevaInstancia(String nombrePaciente, String documentoPaciente) {
        AgregarCitaFragment fragment = new AgregarCitaFragment();
        Bundle args = new Bundle();
        args.putString("nombrePaciente", nombrePaciente);
        args.putString("documentoPaciente", documentoPaciente);
        fragment.setArguments(args);
        return fragment;
    }

    private void iniciarPresenter() {
        presenter = new PresenterAgregarCitaFragment(this);
    }

    private void enlazarVistas(@NonNull View view) {
        tvNombrePaciente = view.findViewById(R.id.tvNombrePacienteCC);
        tvDocumentoPaciente = view.findViewById(R.id.tvDocumentoPacienteCC);
        etFecha = view.findViewById(R.id.inputFechaCitaCC);
        etHora = view.findViewById(R.id.inputHoraCitaCC);
        spnOptometra = view.findViewById(R.id.spinnerOptometraCC);
        spnConsultorio = view.findViewById(R.id.spinnerConsultorioCC);
        btnBuscarPaciente = view.findViewById(R.id.btnBuscarPacienteCC);
        btnGuardarCita = view.findViewById(R.id.btnGuardarCitaCC);

    }


    public void actualizarPaciente(String nombre, String documento) {
        if (tvNombrePaciente == null || tvDocumentoPaciente == null) {
            nombrePendiente = nombre;
            documentoPendiente = documento;
            return;
        }

        tvNombrePaciente.setText(nombre);
        tvDocumentoPaciente.setText(documento);

        nombrePacienteGuardado = nombre;
        documentoPacienteGuardado = documento;
    }

    public void llenarPaciente() {
        String nombrePaciente = null;
        String documentoPaciente = null;

        if (getArguments() != null) {
            nombrePaciente = getArguments().getString("nombrePaciente");
            documentoPaciente = getArguments().getString("documentoPaciente");
        }

        if (nombrePaciente != null && !nombrePaciente.isEmpty()) {
            tvNombrePaciente.setText(nombrePaciente);
            nombrePacienteGuardado = nombrePaciente;
        } else if (nombrePacienteGuardado != null && !nombrePacienteGuardado.isEmpty()) {
            tvNombrePaciente.setText(nombrePacienteGuardado);
        } else {
            tvNombrePaciente.setText("Nombre del paciente");
        }

        if (documentoPaciente != null && !documentoPaciente.isEmpty()) {
            tvDocumentoPaciente.setText(documentoPaciente);
            documentoPacienteGuardado = documentoPaciente;
        } else if (documentoPacienteGuardado != null && !documentoPacienteGuardado.isEmpty()) {
            tvDocumentoPaciente.setText(documentoPacienteGuardado);
        } else {
            tvDocumentoPaciente.setText("Documento del paciente");
        }
    }

    private void cambiarPaciente() {
        if (nombrePendiente != null && documentoPendiente != null) {
            actualizarPaciente(nombrePendiente, documentoPendiente);
            nombrePendiente = null;
            documentoPendiente = null;
        }
    }


    private void irABuscarPaciente() {
        if (getActivity() instanceof MenuActivity) {
            MenuActivity activity = (MenuActivity) getActivity();
            activity.guardarFragmentActivo(this, "AGREGAR_CITA");
            activity.mostrarFragmentConDatos(new BuscarPacienteCCFragment(), "BUSCAR_PACIENTE");
        }
    }

    private void configurarSpinnerOptometras() {
        String []optometras = presenter.listaOptometras();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                optometras
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnOptometra.setAdapter(adapter);
    }

    private void configurarSpinnerConsultorios(){
        String []consultorios = presenter.listaConsultorios();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                consultorios
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnConsultorio.setAdapter(adapter);
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
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void mostrarSelectorHora() {
        final Calendar horaActual = Calendar.getInstance();
        int hora = horaActual.get(Calendar.HOUR_OF_DAY);
        int minuto = horaActual.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                (view, hourOfDay, minute1) -> {
                    int minutosRedondeados = (minute1 / 10) * 10;
                    String horaFormateada = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minutosRedondeados);
                    etHora.setText(horaFormateada);
                }, hora, minuto, true);
        timePickerDialog.show();
    }

    public void guardarCita(){
        presenter.agregarCita();
    }

    public String getNombrePaciente(){
        return  tvNombrePaciente.getText() != null ? tvNombrePaciente.getText().toString().trim() :"";
    }

    public String getDocumentoPaciente(){
        return tvDocumentoPaciente.getText() != null ? tvDocumentoPaciente.getText().toString().trim() : "";
    }

    public String getFechaCita(){
        return etFecha.getText() != null ? etFecha.getText().toString().trim(): "";
    }

    public String getHoraCita(){
        return etHora.getText() != null ? etHora.getText().toString().trim() : "";
    }

    public String getOptometra(){
        return spnOptometra.getSelectedItem().toString().trim();
    }

    public String getConsultorio(){
        return spnConsultorio.getSelectedItem().toString().trim();
    }

    public void irACalendario(){
        if (getActivity() instanceof MenuActivity) {
            ((MenuActivity) getActivity()).replaceFragment(new CalendarioFragment());
        }
    }


    public void mostrarMensaje(String mensaje) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
    }
}
