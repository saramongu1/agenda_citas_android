package com.example.agenda_optica_isis.view;
import android.app.DatePickerDialog;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import java.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.agenda_optica_isis.R;
import com.example.agenda_optica_isis.presenter.PresenterAgregarPacienteFragment;
import com.google.android.material.textfield.TextInputEditText;


public class AgregarPacienteFragment extends Fragment {
    private TextInputEditText etNombrePaciente;
    private TextInputEditText etDocumentoPaciente;
    private Spinner spnTipoDocumentoPaciente;
    private  TextInputEditText etNumeroCelularPaciente;
    private TextInputEditText etFechaNacimientoPaciente;
    private TextInputEditText etCorreoPaciente;
    private Button btnCrearPaciente;
    private PresenterAgregarPacienteFragment presenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_agregar_paciente, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iniciarPresenter();
        enlazarVistar(view);
        initInputFecha(view);
        initSpinnerTiposDocumento(view);
        btnCrearPaciente.setOnClickListener(v -> crearPaciente());
    }

    public void crearPaciente(){
        presenter.crearPaciente();
    }

    public void iniciarPresenter(){
        presenter = new PresenterAgregarPacienteFragment(this);
    }

    private void enlazarVistar(@NonNull View view){
        etNombrePaciente = view.findViewById(R.id.inputNombreCrearPaciente);
        etDocumentoPaciente = view.findViewById(R.id.inputDocumentoCrearPaciente);
        spnTipoDocumentoPaciente = view.findViewById(R.id.spinnerTipoDocumentoCrearPaciente);
        etNumeroCelularPaciente = view.findViewById(R.id.inputNumeroCelularCrearPaciente);
        etFechaNacimientoPaciente = view.findViewById(R.id.inputFechaNacimiento);
        btnCrearPaciente = view.findViewById(R.id.btnCrearPaciente);
        etCorreoPaciente = view.findViewById(R.id.inputCorreoCrearPaciente);
    }

    public String getNombrePaciente(){
        return etNombrePaciente.getText() != null ? etNombrePaciente.getText().toString().trim() : "";
    }

    public String getDocumentoPaciente(){
        return etDocumentoPaciente.getText() != null ? etDocumentoPaciente.getText().toString().trim() : "";
    }

    public String getTipoDocumentoPaciente(){
        return spnTipoDocumentoPaciente.getSelectedItem().toString();
    }

    public String getNumeroCelularPaciente(){
        return etNumeroCelularPaciente.getText() != null ? etNumeroCelularPaciente.getText().toString().trim() : "";
    }

    public String getFechaNacimientoPaciente(){
        return etFechaNacimientoPaciente.getText() != null ? etFechaNacimientoPaciente.getText().toString().trim() : "";
    }

    public String getCorreoPaciente(){
        return etCorreoPaciente.getText() != null ? etCorreoPaciente.getText().toString().trim() : "";
    }

    public void initSpinnerTiposDocumento(@NonNull View view){
        Spinner spinnerTipoDocumento = view.findViewById(R.id.spinnerTipoDocumentoCrearPaciente);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.tipos_documento,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoDocumento.setAdapter(adapter);
    }

    public void initInputFecha(@NonNull View view){
        EditText inputFecha = view.findViewById(R.id.inputFechaNacimiento);

        inputFecha.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePicker = new DatePickerDialog(
                    requireContext(),
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        String fechaSeleccionada = String.format("%02d/%02d/%04d",
                                selectedDay, selectedMonth + 1, selectedYear);
                        inputFecha.setText(fechaSeleccionada);
                    },
                    year, month, day
            );
            datePicker.getDatePicker().setMaxDate(calendar.getTimeInMillis());
            datePicker.show();
        });


    }

    public void mostrarMensaje(String mensaje) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

}