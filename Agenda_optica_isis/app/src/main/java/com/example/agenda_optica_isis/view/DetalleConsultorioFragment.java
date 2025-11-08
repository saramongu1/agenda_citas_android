package com.example.agenda_optica_isis.view;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.agenda_optica_isis.R;
import com.example.agenda_optica_isis.presenter.PresenterDetalleConsultorioFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

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
}