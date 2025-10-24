package com.example.agenda_optica_isis.presenter;

import com.example.agenda_optica_isis.model.SistemaReservas;
import com.example.agenda_optica_isis.view.OptometrasFragment;

import java.util.HashMap;

public class PresenterOptometrasFragment {
    private OptometrasFragment view;
    private SistemaReservas sistemaReservas;

    public PresenterOptometrasFragment(OptometrasFragment view) {
        this.view = view;
        this.sistemaReservas = SistemaReservas.getInstance();
    }

    public void cargarOptometras() {
        HashMap<String, String> lista = sistemaReservas.obtenerListaOptometras();
        view.mostrarListaOptometras(lista);
    }
}
