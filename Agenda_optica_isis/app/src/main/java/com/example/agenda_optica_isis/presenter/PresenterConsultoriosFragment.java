package com.example.agenda_optica_isis.presenter;

import com.example.agenda_optica_isis.model.Consultorio;
import com.example.agenda_optica_isis.model.SistemaReservas;
import com.example.agenda_optica_isis.view.ConsultoriosFragment;

import java.util.HashMap;

public class PresenterConsultoriosFragment {

    private final ConsultoriosFragment view;
    private final SistemaReservas sistemaReservas;

    public PresenterConsultoriosFragment(ConsultoriosFragment view) {
        this.view = view;
        this.sistemaReservas = SistemaReservas.getInstance();
    }

    public void cargarConsultorios() {
        try {
            HashMap<String, String> consultoriosMap = new HashMap<>();
            String[] ids = sistemaReservas.obtenerIdsConsultorios();

            for (String id : ids) {
                Consultorio consultorio = sistemaReservas.leerConsultorio(id);
                if (consultorio != null) {
                    String value = consultorio.getDireccion() + "|" + consultorio.getCiudad();
                    consultoriosMap.put(id, value);
                }
            }

            view.mostrarListaConsultorios(consultoriosMap);
        } catch (Exception e) {
            view.mostrarMensaje("Error al cargar consultorios: " + e.getMessage());
        }
    }

    public void cargarBusquedaConsultorio() {
        String textoBusqueda = view.getTextoBusquedaConsultorio();
        String criterio = view.getTextoCriterioBusqueda();

        if (textoBusqueda.isEmpty()) {
            cargarConsultorios();
            return;
        }

        try {
            HashMap<String, String> consultoriosFiltrados = new HashMap<>();
            String[] ids = sistemaReservas.obtenerIdsConsultorios();

            for (String id : ids) {
                Consultorio consultorio = sistemaReservas.leerConsultorio(id);
                if (consultorio != null) {
                    boolean coincide = false;

                    switch (criterio) {
                        case "ID":
                            coincide = id.toLowerCase().contains(textoBusqueda.toLowerCase());
                            break;
                        case "Dirección":
                            coincide = consultorio.getDireccion().toLowerCase().contains(textoBusqueda.toLowerCase());
                            break;
                        case "Ciudad":
                            coincide = consultorio.getCiudad().toLowerCase().contains(textoBusqueda.toLowerCase());
                            break;
                    }

                    if (coincide) {
                        String value = consultorio.getDireccion() + "|" + consultorio.getCiudad();
                        consultoriosFiltrados.put(id, value);
                    }
                }
            }

            view.mostrarListaConsultorios(consultoriosFiltrados);

            if (consultoriosFiltrados.isEmpty()) {
                view.mostrarMensaje("No se encontraron consultorios con ese criterio");
            }
        } catch (Exception e) {
            view.mostrarMensaje("Error al buscar consultorios: " + e.getMessage());
        }
    }
}