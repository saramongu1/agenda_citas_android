package com.example.agenda_optica_isis.view;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda_optica_isis.R;
import com.example.agenda_optica_isis.presenter.PresenterDetallePacienteFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class DetallePacienteFragment extends Fragment {

    private static final String ARG_DOCUMENTO = "documento";

    private TextInputEditText etNombrePaciente;
    private TextInputEditText etDocumentoPaciente;
    private Spinner spnTipoDocumentoPaciente;
    private TextInputEditText etCorreoPaciente;
    private TextInputEditText etNumeroCelularPaciente;
    private TextInputEditText etFechaNacimientoPaciente;
    private Spinner spnGeneroPaciente;
    private MaterialButton btnGuardarCambios;
    private MaterialButton btnEditarPaciente;
    private MaterialButton btnEliminarPaciente;
    private RecyclerView recyclerCitas;
    private String documentoPaciente;
    private PresenterDetallePacienteFragment presenter;

    public static DetallePacienteFragment nuevaInstancia(String documento) {
        DetallePacienteFragment fragment = new DetallePacienteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DOCUMENTO, documento);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalle_paciente, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        enlazarVistas(view);
        initSpinnerTiposDocumento(view);
        initSpinnerGenero(view);
        deshabilitarSpinners();
        initPresentador();
        initInputFecha(view);
        cargarDatosPaciente(); 

        btnGuardarCambios.setOnClickListener(v -> guardarCambios());
        btnEditarPaciente.setOnClickListener(v -> editarPaciente());
        btnEliminarPaciente.setOnClickListener(v -> eliminarPaciente());
    }

    public void eliminarPaciente(){
        presenter.eliminarPaciente();
    }
    public void editarPaciente(){
        etNombrePaciente.setEnabled(true);
        spnTipoDocumentoPaciente.setEnabled(true);
        spnGeneroPaciente.setEnabled(true);
        etNumeroCelularPaciente.setEnabled(true);
        etCorreoPaciente.setEnabled(true);
        etFechaNacimientoPaciente.setEnabled(true);

        btnGuardarCambios.setVisibility(VISIBLE);
        btnEditarPaciente.setVisibility(GONE);
    }

    private void initPresentador(){
        presenter = new PresenterDetallePacienteFragment(this);
    }


    public void enlazarVistas(@NonNull View view){
        etNombrePaciente = view.findViewById(R.id.inputNombrePacienteDetalle);
        etDocumentoPaciente = view.findViewById(R.id.inputDocumentoPacienteDetalle);
        etCorreoPaciente = view.findViewById(R.id.inputCorreoPacienteDetalle);
        etNumeroCelularPaciente = view.findViewById(R.id.inputCelularPacienteDetalle);
        etFechaNacimientoPaciente = view.findViewById(R.id.inputFechaNacimientoPacienteDetalle);
        btnGuardarCambios = view.findViewById(R.id.btnGuardarCambios);
        spnTipoDocumentoPaciente = view.findViewById(R.id.spinnerTipoDocumentoPacienteDetalle);
        spnGeneroPaciente = view.findViewById(R.id.spinnerGeneroPacienteDetalle);
        btnEditarPaciente = view.findViewById(R.id.btnEditarPaciente);
        btnEliminarPaciente = view.findViewById(R.id.btnEliminarPaciente);


        recyclerCitas = view.findViewById(R.id.recyclerCitasPaciente);
        recyclerCitas.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    public void initSpinnerTiposDocumento(@NonNull View view){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.tipos_documento,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTipoDocumentoPaciente.setAdapter(adapter);
    }

    public void initSpinnerGenero(@NonNull View view){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.tipos_genero,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGeneroPaciente.setAdapter(adapter);
    }

    public void asignarDetallesPaciente(String nombre, String documento, String tipo_documento, String genero,
                                        String numero_celular, String correo, String fecha_nacimiento){
        etNombrePaciente.setText(nombre);
        etDocumentoPaciente.setText(documento);
        spnTipoDocumentoPaciente.setSelection(posicionSeleccionTipoDocumento(tipo_documento));
        spnGeneroPaciente.setSelection(posicionSeleccionGenero(genero));
        etNumeroCelularPaciente.setText(numero_celular);
        etCorreoPaciente.setText(correo);
        etFechaNacimientoPaciente.setText(fecha_nacimiento);

    }

    public String getEtNombrePaciente() {
        return etNombrePaciente.getText().toString().trim();
    }

    public String getEtDocumentoPaciente() {
        return etDocumentoPaciente.getText().toString().trim();
    }

    public String getSpnTipoDocumentoPaciente() {
        return spnTipoDocumentoPaciente.getSelectedItem().toString();
    }

    public String getEtCorreoPaciente() {
        return etCorreoPaciente.getText().toString().trim();
    }

    public String getEtNumeroCelularPaciente() {
        return etNumeroCelularPaciente.getText().toString().trim();
    }

    public String getEtFechaNacimientoPaciente() {
        return etFechaNacimientoPaciente.getText().toString().trim();
    }

    public String getSpnGeneroPaciente() {
        return spnGeneroPaciente.getSelectedItem().toString();
    }

    public String getDocumentoAntiguoPaciente(){
        return documentoPaciente;
    }

    private int posicionSeleccionTipoDocumento(String tipo_documento){
        int posicion = 0;
        if(tipo_documento.equalsIgnoreCase("T.I.")){
            posicion = 1;
        } else if (tipo_documento.equalsIgnoreCase("C.E.")) {
            posicion = 2;
        } else if (tipo_documento.equalsIgnoreCase("PAS")) {
            posicion = 4;
        }
        return posicion;
    }

    public void deshabilitarSpinners(){
        spnGeneroPaciente.setEnabled(false);
        spnTipoDocumentoPaciente.setEnabled(false);
    }

    private int posicionSeleccionGenero(String genero){
        int posicion = 0;
        if (genero.equalsIgnoreCase("masculino")){
            posicion = 1;
        } else if (genero.equalsIgnoreCase("otro")) {
            posicion = 2;
        }
        return  posicion;
    }



    private void cargarDatosPaciente() {
        documentoPaciente = getArguments().getString(ARG_DOCUMENTO);
        presenter.AsignarInformacionPaciente(documentoPaciente);
    }



    private void guardarCambios() {
        presenter.actualizarPaciente();
    }

    public void mostrarMensaje(String mensaje) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public void initInputFecha(@NonNull View view){
        EditText inputFecha = view.findViewById(R.id.inputFechaNacimientoPacienteDetalle);

        inputFecha.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePicker = new DatePickerDialog(
                    requireContext(),
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        String fechaSeleccionada = String.format("%02d-%02d-%04d",
                                selectedDay, selectedMonth + 1, selectedYear);
                        inputFecha.setText(fechaSeleccionada);
                    },
                    year, month, day
            );
            datePicker.getDatePicker().setMaxDate(calendar.getTimeInMillis());
            datePicker.show();
        });


    }

    public void irAPacientes(){
        if (getActivity() instanceof MenuActivity) {
            ((MenuActivity) getActivity()).replaceFragment(new PacientesFragment());
        }
    }


}
