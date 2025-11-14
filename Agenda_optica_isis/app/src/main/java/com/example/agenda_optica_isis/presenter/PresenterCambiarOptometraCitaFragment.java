package com.example.agenda_optica_isis.presenter;

import com.example.agenda_optica_isis.model.SistemaReservas;
import com.example.agenda_optica_isis.view.CambiarOptometraCitaFragment;

public class PresenterCambiarOptometraCitaFragment {
    private CambiarOptometraCitaFragment view;
    private SistemaReservas sistemaReservas;

    public PresenterCambiarOptometraCitaFragment(CambiarOptometraCitaFragment view) {
        this.view = view;
        this.sistemaReservas = SistemaReservas.getInstance();
    }

    public String[] listaOptometras(){
        return sistemaReservas.obtenerNombresOptometras();
    }

    public void asignarOptometraLista(){
        String anterior_optometra = view.getDocumentoOptometra();
        String nuevo_optometra = sistemaReservas.getDocumentoOptometra(view.getOptometra());
        sistemaReservas.cambiarCitasOptometra(anterior_optometra, nuevo_optometra);
        sistemaReservas.eliminarOptometra(anterior_optometra);
        view.irACalendario();
    }

}
