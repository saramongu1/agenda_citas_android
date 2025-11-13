package com.example.agenda_optica_isis.view;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.DatePickerDialog;
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
import com.example.agenda_optica_isis.presenter.PresenterDetalleOptometraFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class DetalleOptometraFragment extends Fragment {

    private static final String ARG_DOCUMENTO = "documento";

    private TextInputEditText etNombreOptometra;
    private TextInputEditText etDocumentoOptometra;
    private Spinner spnTipoDocumentoOptometra;
    private TextInputEditText etCorreoOptometra;
    private TextInputEditText etNumeroCelularOptometra;
    private TextInputEditText etFechaNacimientoOptometra;
    private Spinner spnGeneroOptometra;

    private MaterialButton btnGuardarCambios;
    private MaterialButton btnEditarOptometra;
    private MaterialButton btnEliminarOptometra;

    private String documentoOptometra;
    private PresenterDetalleOptometraFragment presenter;

    public static DetalleOptometraFragment nuevaInstancia(String documento) {
        DetalleOptometraFragment fragment = new DetalleOptometraFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DOCUMENTO, documento);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalle_optometra, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        enlazarVistas(view);
        deshabilitarSpinners();
        initSpinnerTiposDocumento();
        initSpinnerGenero();
        initPresentador();
        initInputFecha();
        cargarDatosOptometra();

        btnGuardarCambios.setOnClickListener(v -> guardarCambios());
        btnEditarOptometra.setOnClickListener(v -> editarOptometra());
        btnEliminarOptometra.setOnClickListener(v -> eliminarOptometra());
    }

    private void initPresentador() {
        presenter = new PresenterDetalleOptometraFragment(this);
    }

    public void deshabilitarSpinners(){
        spnGeneroOptometra.setEnabled(false);
        spnTipoDocumentoOptometra.setEnabled(false);
    }
    private void enlazarVistas(@NonNull View view) {
        etNombreOptometra = view.findViewById(R.id.inputNombreOptometraDetalle);
        etDocumentoOptometra = view.findViewById(R.id.inputDocumentoOptometraDetalle);
        etCorreoOptometra = view.findViewById(R.id.inputCorreoOptometraDetalle);
        etNumeroCelularOptometra = view.findViewById(R.id.inputCelularOptometraDetalle);
        etFechaNacimientoOptometra = view.findViewById(R.id.inputFechaNacimientoOptometraDetalle);
        spnTipoDocumentoOptometra = view.findViewById(R.id.spinnerTipoDocumentoOptometraDetalle);
        spnGeneroOptometra = view.findViewById(R.id.spinnerGeneroOptometraDetalle);
        btnEditarOptometra = view.findViewById(R.id.btnEditarOptometra);
        btnEliminarOptometra = view.findViewById(R.id.btnEliminarOptometra);
        btnGuardarCambios = view.findViewById(R.id.btnGuardarCambiosOptometra);
    }

    private void initSpinnerTiposDocumento() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.tipos_documento,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTipoDocumentoOptometra.setAdapter(adapter);
    }

    private void initSpinnerGenero() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.tipos_genero,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGeneroOptometra.setAdapter(adapter);
    }

    public void asignarDetallesOptometra(String nombre, String documento, String tipo_documento,
                                         String genero, String numero_celular,
                                         String correo, String fecha_nacimiento) {

        etNombreOptometra.setText(nombre);
        etDocumentoOptometra.setText(documento);
        spnTipoDocumentoOptometra.setSelection(posicionSeleccionTipoDocumento(tipo_documento));
        spnGeneroOptometra.setSelection(posicionSeleccionGenero(genero));
        etNumeroCelularOptometra.setText(numero_celular);
        etCorreoOptometra.setText(correo);
        etFechaNacimientoOptometra.setText(fecha_nacimiento);
    }

    public String getEtNombreOptometra() {
        return etNombreOptometra.getText().toString().trim();
    }

    public String getEtDocumentoOptometra() {
        return etDocumentoOptometra.getText().toString().trim();
    }

    public String getSpnTipoDocumentoOptometra() {
        return spnTipoDocumentoOptometra.getSelectedItem().toString();
    }

    public String getEtCorreoOptometra() {
        return etCorreoOptometra.getText().toString().trim();
    }

    public String getEtNumeroCelularOptometra() {
        return etNumeroCelularOptometra.getText().toString().trim();
    }

    public String getEtFechaNacimientoOptometra() {
        return etFechaNacimientoOptometra.getText().toString().trim();
    }

    public String getSpnGeneroOptometra() {
        return spnGeneroOptometra.getSelectedItem().toString();
    }

    public String getDocumentoAntiguoOptometra() {
        return documentoOptometra;
    }

    private int posicionSeleccionTipoDocumento(String tipo_documento) {
        int posicion = 0;
        if (tipo_documento.equalsIgnoreCase("T.I.")) {
            posicion = 1;
        } else if (tipo_documento.equalsIgnoreCase("C.E.")) {
            posicion = 2;
        } else if (tipo_documento.equalsIgnoreCase("PAS")) {
            posicion = 4;
        }
        return posicion;
    }

    private int posicionSeleccionGenero(String genero) {
        int posicion = 0;
        if (genero.equalsIgnoreCase("masculino")) {
            posicion = 1;
        } else if (genero.equalsIgnoreCase("otro")) {
            posicion = 2;
        }
        return posicion;
    }

    private void cargarDatosOptometra() {
        documentoOptometra = getArguments().getString(ARG_DOCUMENTO);
        presenter.asignarInformacionOptometra(documentoOptometra);
    }

    private void editarOptometra() {
        etNombreOptometra.setEnabled(true);
        spnTipoDocumentoOptometra.setEnabled(true);
        spnGeneroOptometra.setEnabled(true);
        etNumeroCelularOptometra.setEnabled(true);
        etCorreoOptometra.setEnabled(true);
        etFechaNacimientoOptometra.setEnabled(true);

        btnGuardarCambios.setVisibility(VISIBLE);
        btnEditarOptometra.setVisibility(GONE);
    }

    private void guardarCambios(){
        presenter.actualizarOptometra();
    }

    private void eliminarOptometra(){
        presenter.eliminarOptometra();
    }



    public void mostrarMensaje(String mensaje) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    private void initInputFecha() {
        EditText inputFecha = etFechaNacimientoOptometra;

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

    public void irAOptometras() {
        if (getActivity() instanceof MenuActivity) {
            ((MenuActivity) getActivity()).replaceFragment(new OptometrasFragment());
        }
    }
}
