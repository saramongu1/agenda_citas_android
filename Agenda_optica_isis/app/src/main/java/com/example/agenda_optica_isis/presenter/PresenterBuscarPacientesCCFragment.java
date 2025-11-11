package com.example.agenda_optica_isis.presenter;

import com.example.agenda_optica_isis.exceptions.ValidacionException;
import com.example.agenda_optica_isis.exceptions.ValidarDatos;
import com.example.agenda_optica_isis.model.SistemaReservas;
import com.example.agenda_optica_isis.view.BuscarPacienteCCFragment;

public class PresenterBuscarPacientesCCFragment {
    private BuscarPacienteCCFragment view;
    private SistemaReservas sistemaReservas;

    public PresenterBuscarPacientesCCFragment(BuscarPacienteCCFragment view){
        this.view = view;
        this.sistemaReservas = SistemaReservas.getInstance();
    }

    public void cargarPacientes(){
        view.mostrarListaPacientes(sistemaReservas.retornatNombreDocPacientes());
    }

    public void cargarBusquedaPaciente(){
        try {
            String criterio = view.getTextoCriterioBusqueda();
            String pacienteBuscar = view.getTextoBusquedaPaciente();
            ValidarDatos.validarTexto("busqueda", pacienteBuscar);
            view.mostrarListaPacientes(sistemaReservas.buscarPaciente(criterio, pacienteBuscar));
        } catch (ValidacionException validacionException) {
            view.mostrarMensaje(validacionException.getMessage());
        } catch (Exception e) {
            view.mostrarMensaje("Error inesperado");
        }
    }
}
