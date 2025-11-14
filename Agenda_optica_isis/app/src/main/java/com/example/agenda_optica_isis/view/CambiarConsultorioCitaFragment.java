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
import com.example.agenda_optica_isis.presenter.PresenterCambiarConsultorioCitaFragment;
import com.google.android.material.button.MaterialButton;

public class CambiarConsultorioCitaFragment extends Fragment {

    private MaterialButton btnAsignarConsultorio;
    private Spinner spnConsultorios;
    private TextView tvConsultorioActual;

    private PresenterCambiarConsultorioCitaFragment presenter;

    private String idConsultorioActual = "";

    public CambiarConsultorioCitaFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            idConsultorioActual = getArguments().getString("consultorio_actual", "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cambiar_consultorio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        enlazarVistas(view);
        iniciarPresenter();
        configurarSpinnerConsultorios();

        tvConsultorioActual.setText(idConsultorioActual);

        btnAsignarConsultorio.setOnClickListener(v -> asignarConsultorio());
    }

    private void iniciarPresenter() {
        presenter = new PresenterCambiarConsultorioCitaFragment(this);
    }

    public void asignarConsultorio() {
        presenter.asignarConsultorioLista();
    }

    public String getConsultorioNuevo() {
        return spnConsultorios.getSelectedItem().toString().trim();
    }

    public String getIdConsultorioActual() {
        return idConsultorioActual;
    }


    public void irAConsultorios() {
        if (getActivity() instanceof MenuActivity) {
            ((MenuActivity) getActivity()).replaceFragment(new ConsultoriosFragment());
        }
    }

    private void enlazarVistas(@NonNull View view) {
        spnConsultorios = view.findViewById(R.id.spinnerConsultoriosCambiar);
        btnAsignarConsultorio = view.findViewById(R.id.btnAsignarConsultorioCambiarCitas);
        tvConsultorioActual = view.findViewById(R.id.tvConsultorioActual);
    }

    private void configurarSpinnerConsultorios() {
        String[] consultorios = presenter.listaConsultorios();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                consultorios
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnConsultorios.setAdapter(adapter);
    }


}
