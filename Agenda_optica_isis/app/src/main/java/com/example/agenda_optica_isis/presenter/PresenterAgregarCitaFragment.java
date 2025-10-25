package com.example.agenda_optica_isis.presenter;

import com.example.agenda_optica_isis.model.SistemaReservas;
import com.example.agenda_optica_isis.view.AgregarCitaFragment;
import com.example.agenda_optica_isis.view.AgregarPacienteFragment;

public class PresenterAgregarCitaFragment {
    private AgregarCitaFragment view;
    private SistemaReservas sistemaReservas;

    public PresenterAgregarCitaFragment(AgregarCitaFragment view) {
        this.view = view;
        this.sistemaReservas = SistemaReservas.getInstance();
    }
}
