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
import com.example.agenda_optica_isis.presenter.PresenterAgregarOptometraFragment;
import com.google.android.material.textfield.TextInputEditText;

public class AgregarOptometraFragment extends Fragment {
    private TextInputEditText etNombreOptometra;
    private TextInputEditText etDocumentoOptometra;
    private Spinner spnTipoDocumentoOptometra;
    private TextInputEditText etNumeroCelularOptometra;
    private TextInputEditText etFechaNacimientoOptometra;
    private TextInputEditText etCorreoOptometra;
    private Button btnCrearOptometra;
    private PresenterAgregarOptometraFragment presenter;
    private Spinner spnGeneroOptometra;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_agregar_optometra, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iniciarPresenter();
        enlazarVistas(view);
        initInputFecha(view);
        initSpinnerTiposDocumento(view);
        initSpinnerGenero(view);
        btnCrearOptometra.setOnClickListener(v -> crearOptometra());
    }

    public void crearOptometra(){
        presenter.crearOptometra();
    }

    public void irAOptometras(){
        if (getActivity() instanceof MenuActivity) {
            ((MenuActivity) getActivity()).replaceFragment(new OptometrasFragment());
        }
    }

    public void iniciarPresenter(){
        presenter = new PresenterAgregarOptometraFragment(this);
    }

    private void enlazarVistas(@NonNull View view){
        etNombreOptometra = view.findViewById(R.id.inputNombreCrearOptometra);
        etDocumentoOptometra = view.findViewById(R.id.inputDocumentoCrearOptometra);
        spnTipoDocumentoOptometra = view.findViewById(R.id.spinnerTipoDocumentoCrearOptometra);
        etNumeroCelularOptometra = view.findViewById(R.id.inputNumeroCelularCrearOptometra);
        etFechaNacimientoOptometra = view.findViewById(R.id.inputFechaNacimientoOptometra);
        btnCrearOptometra = view.findViewById(R.id.btnCrearOptometra);
        etCorreoOptometra = view.findViewById(R.id.inputCorreoCrearOptometra);
        spnGeneroOptometra = view.findViewById(R.id.spinnerGeneroCrearOptometra);
    }

    public String getNombreOptometra(){
        return etNombreOptometra.getText() != null ? etNombreOptometra.getText().toString().trim() : "";
    }

    public String getDocumentoOptometra(){
        return etDocumentoOptometra.getText() != null ? etDocumentoOptometra.getText().toString().trim() : "";
    }

    public String getTipoDocumentoOptometra(){
        return spnTipoDocumentoOptometra.getSelectedItem().toString();
    }

    public String getNumeroCelularOptometra(){
        return etNumeroCelularOptometra.getText() != null ? etNumeroCelularOptometra.getText().toString().trim() : "";
    }

    public String getFechaNacimientoOptometra(){
        return etFechaNacimientoOptometra.getText() != null ? etFechaNacimientoOptometra.getText().toString().trim() : "";
    }

    public String getCorreoOptometra(){
        return etCorreoOptometra.getText() != null ? etCorreoOptometra.getText().toString().trim() : "";
    }

    public String getGeneroOptometra(){
        return spnGeneroOptometra.getSelectedItem().toString();
    }

    public void initSpinnerTiposDocumento(@NonNull View view){
        Spinner spinnerTipoDocumento = view.findViewById(R.id.spinnerTipoDocumentoCrearOptometra);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.tipos_documento,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoDocumento.setAdapter(adapter);
    }

    public void initSpinnerGenero(@NonNull View view){
        Spinner spinnerGeneroOptometra = view.findViewById(R.id.spinnerGeneroCrearOptometra);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.tipos_genero,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGeneroOptometra.setAdapter(adapter);
    }

    public void initInputFecha(@NonNull View view){
        EditText inputFecha = view.findViewById(R.id.inputFechaNacimientoOptometra);

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