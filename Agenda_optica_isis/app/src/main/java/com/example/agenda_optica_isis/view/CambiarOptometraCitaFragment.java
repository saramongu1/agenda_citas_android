package com.example.agenda_optica_isis.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.agenda_optica_isis.R;
import com.example.agenda_optica_isis.presenter.PresenterCambiarOptometraCitaFragment;
import com.google.android.material.button.MaterialButton;


public class CambiarOptometraCitaFragment extends Fragment {

    private MaterialButton btnAsignarOptometra;
    private Spinner spnOptometra;
    private TextView tvNombreOptometraEliminar;
    private PresenterCambiarOptometraCitaFragment presenter;

    private String nombreOptometraActual = "";
    private String documentoOptometraActual = "";

    public CambiarOptometraCitaFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            nombreOptometraActual = getArguments().getString("nombre_optometra", "");
            documentoOptometraActual = getArguments().getString("documento_optometra", "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cambiar_optometra_cita, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        enlazarVistas(view);
        iniciarPresenter();
        configurarSpinnerOptometras();

        tvNombreOptometraEliminar.setText(nombreOptometraActual);

        btnAsignarOptometra.setOnClickListener(v -> asignarOptometra());
    }

    public void iniciarPresenter() {
        presenter = new PresenterCambiarOptometraCitaFragment(this);
    }

    public void asignarOptometra() {
        presenter.asignarOptometraLista();
    }

    public String getOptometra() {
        return spnOptometra.getSelectedItem().toString().trim();
    }

    public String getDocumentoOptometra() {
        return documentoOptometraActual;
    }

    public void irACalendario() {
        if (getActivity() instanceof MenuActivity) {
            ((MenuActivity) getActivity()).replaceFragment(new CalendarioFragment());
        }
    }

    private void enlazarVistas(@NonNull View view) {
        spnOptometra = view.findViewById(R.id.spinnerOptometraCambiar);
        btnAsignarOptometra = view.findViewById(R.id.btnAsignarOptometraCambiarCitas);
        tvNombreOptometraEliminar = view.findViewById(R.id.tvNombreOptometraEliminar);
    }

    private void configurarSpinnerOptometras() {
        String[] optometras = presenter.listaOptometras();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                optometras
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnOptometra.setAdapter(adapter);
    }
}

