package com.example.agenda_optica_isis.view;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.agenda_optica_isis.R;
import com.example.agenda_optica_isis.presenter.PresenterDetalleOptometraFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

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

    public void irAOptometras() {
        if (getActivity() instanceof MenuActivity) {
            ((MenuActivity) getActivity()).replaceFragment(new OptometrasFragment());
        }
    }

    public void irACambiarOptometra(String nombreOptometra, String documentoOptometra) {
        if (getActivity() instanceof MenuActivity) {

            CambiarOptometraCitaFragment fragment = new CambiarOptometraCitaFragment();

            Bundle args = new Bundle();
            args.putString("nombre_optometra", nombreOptometra);
            args.putString("documento_optometra", documentoOptometra);

            fragment.setArguments(args);

            ((MenuActivity) getActivity()).replaceFragment(fragment);
        }
    }


    public void mostrarDialogoPersonalizado(String titulo, String mensaje, Map<String, Runnable> opciones) {
        if (getContext() == null) return;

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_personalizado, null);

        TextView tvTitulo = dialogView.findViewById(R.id.tvTituloDialogo);
        TextView tvMensaje = dialogView.findViewById(R.id.tvMensajeDialogo);
        LinearLayout layoutBotones = dialogView.findViewById(R.id.layoutBotonesDialogo);

        tvTitulo.setText(titulo);
        tvMensaje.setText(mensaje);

        // Creamos la instancia del diálogo aquí para poder cerrarla desde los botones.
        AlertDialog dialog = new MaterialAlertDialogBuilder(getContext(), R.style.CustomAlertDialogTheme)
                .setView(dialogView)
                .create();

        // 🔹 Crear botones dinámicamente a partir de las opciones pasadas
        for (Map.Entry<String, Runnable> entry : opciones.entrySet()) {
            MaterialButton boton = new MaterialButton(getContext(), null, com.google.android.material.R.attr.materialButtonOutlinedStyle);
            boton.setText(entry.getKey());
            boton.setTextColor(getResources().getColor(R.color.azulOscuro));
            boton.setStrokeColor(android.content.res.ColorStateList.valueOf(getResources().getColor(R.color.azulOscuro)));
            boton.setStrokeWidth(3);
            boton.setCornerRadius(20);
            boton.setAllCaps(false);
            boton.setPadding(0, 16, 0, 16);
            boton.setTextSize(16);

            boton.setOnClickListener(v -> {
                entry.getValue().run();
                dialog.dismiss(); // Cierra el diálogo después de ejecutar la acción
            });

            layoutBotones.addView(boton);
        }

        // 🔹 Añadir el botón "Cancelar" por defecto
        MaterialButton botonCancelar = new MaterialButton(getContext(), null, com.google.android.material.R.attr.materialButtonOutlinedStyle);
        botonCancelar.setText("Cancelar");
        botonCancelar.setTextColor(getResources().getColor(R.color.azulOscuro));
        botonCancelar.setStrokeColor(android.content.res.ColorStateList.valueOf(getResources().getColor(R.color.azulOscuro)));
        botonCancelar.setStrokeWidth(3);
        botonCancelar.setCornerRadius(20);
        botonCancelar.setAllCaps(false);
        botonCancelar.setPadding(0, 16, 0, 16);
        botonCancelar.setTextSize(16);

        botonCancelar.setOnClickListener(v -> {
            dialog.dismiss(); // Simplemente cierra el diálogo
        });

        layoutBotones.addView(botonCancelar);

        dialog.show();
    }


}
