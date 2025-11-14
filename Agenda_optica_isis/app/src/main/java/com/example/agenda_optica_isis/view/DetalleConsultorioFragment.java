package com.example.agenda_optica_isis.view;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.agenda_optica_isis.R;
import com.example.agenda_optica_isis.presenter.PresenterDetalleConsultorioFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Map;

public class DetalleConsultorioFragment extends Fragment {

    private static final String ARG_ID_CONSULTORIO = "id_consultorio";

    private TextInputEditText tvIdConsultorio;
    private TextInputEditText tvDireccionConsultorio;
    private TextInputEditText tvCiudadConsultorio;
    private MaterialButton btnEditar;
    private MaterialButton btnGuardarCambios;
    private MaterialButton btnEliminarConsultorio;

    private PresenterDetalleConsultorioFragment presenter;
    private String idConsultorio;

    public static DetalleConsultorioFragment nuevaInstancia(String idConsultorio) {
        DetalleConsultorioFragment fragment = new DetalleConsultorioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID_CONSULTORIO, idConsultorio);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalle_consultorio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            idConsultorio = getArguments().getString(ARG_ID_CONSULTORIO);
        }

        enlazarVistas(view);
        iniciarPresenter();

        btnEditar.setOnClickListener(v -> editarConsultorio());
        btnGuardarCambios.setOnClickListener(v -> guardarCambios());
        btnEliminarConsultorio.setOnClickListener(v -> eliminarConsultorio());

        if (idConsultorio == null || idConsultorio.isEmpty()) {
            mostrarMensaje("ID de consultorio inválido");
        } else {
            presenter.cargarDetalleConsultorio();
        }
    }

    private void enlazarVistas(@NonNull View view) {
        tvIdConsultorio = view.findViewById(R.id.inputIDConsultorioDetalle);
        tvDireccionConsultorio = view.findViewById(R.id.inputDireccionConsultorioDetalle);
        tvCiudadConsultorio = view.findViewById(R.id.inputCiudadConsultorioDetalle);
        btnEditar = view.findViewById(R.id.btnEditarConsultorio);
        btnGuardarCambios = view.findViewById(R.id.btnGuardarCambiosConsultorio);
        btnEliminarConsultorio = view.findViewById(R.id.btnEliminarConsultorio);
    }

    public void mostrarDetalle(String id, String direccion, String ciudad) {
        tvIdConsultorio.setText(id);
        tvDireccionConsultorio.setText(direccion);
        tvCiudadConsultorio.setText(ciudad);
    }

    public void editarConsultorio() {
        btnEditar.setVisibility(GONE);
        btnGuardarCambios.setVisibility(VISIBLE);

        tvDireccionConsultorio.setEnabled(true);
        tvCiudadConsultorio.setEnabled(true);
    }

    public void guardarCambios() {
        presenter.guardarCambiosConsultorio();
    }

    public void eliminarConsultorio() {
        presenter.eliminarConsultorio();
    }

    public String getIdConsultorio() {
        return getText(tvIdConsultorio);
    }

    public String getDireccionConsultorio() {
        return getText(tvDireccionConsultorio);
    }

    public String getCiudadConsultorio() {
        return getText(tvCiudadConsultorio);
    }

    private String getText(TextInputEditText input) {
        return input.getText() != null ? input.getText().toString().trim() : "";
    }

    public void irAConsultorios(){
        if (getActivity() instanceof MenuActivity) {
            ((MenuActivity) getActivity()).replaceFragment(new ConsultoriosFragment());
        }
    }

    public void mostrarMensaje(String mensaje) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public void habilitarEdicion(boolean habilitar) {
        tvDireccionConsultorio.setEnabled(habilitar);
        tvCiudadConsultorio.setEnabled(habilitar);

        if (habilitar) {
            btnEditar.setVisibility(GONE);
            btnGuardarCambios.setVisibility(VISIBLE);
        } else {
            btnEditar.setVisibility(VISIBLE);
            btnGuardarCambios.setVisibility(GONE);
        }
    }

    public void iniciarPresenter(){
        presenter = new PresenterDetalleConsultorioFragment(this, idConsultorio);
    }

    public void irACambiarConsultorio(String idConsultorio) {
        if (getActivity() instanceof MenuActivity) {

            CambiarConsultorioCitaFragment fragment = new CambiarConsultorioCitaFragment();

            Bundle args = new Bundle();
            args.putString("consultorio_actual", idConsultorio);

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