package com.example.agenda_optica_isis.presenter;

import com.example.agenda_optica_isis.model.SistemaReservas;
import com.example.agenda_optica_isis.view.CambiarConsultorioCitaFragment;

public class PresenterCambiarConsultorioCitaFragment {

    private CambiarConsultorioCitaFragment view;
    private SistemaReservas sistemaReservas;

    public PresenterCambiarConsultorioCitaFragment(CambiarConsultorioCitaFragment view) {
        this.view = view;
        this.sistemaReservas = SistemaReservas.getInstance();
    }

    public String[] listaConsultorios() {
        return sistemaReservas.obtenerIdsConsultorios();
    }

    public void asignarConsultorioLista() {
        String consultorioAnterior = view.getIdConsultorioActual();
        String consultorioNuevo = view.getConsultorioNuevo();


        sistemaReservas.cambiarCitasConsultorio(consultorioAnterior, consultorioNuevo);
        sistemaReservas.eliminarConsultorio(consultorioAnterior);

        view.irAConsultorios();
    }
}
