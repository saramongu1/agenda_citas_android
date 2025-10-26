package com.example.agenda_optica_isis.presenter;

import com.example.agenda_optica_isis.model.Cita;
import com.example.agenda_optica_isis.model.Paciente;
import com.example.agenda_optica_isis.model.SistemaReservas;
import com.example.agenda_optica_isis.view.DetalleCitaFragment;

import java.util.List;

public class PresenterDetalleCitaFragment {
    private DetalleCitaFragment view;
    private SistemaReservas sistemaReservas;

    public PresenterDetalleCitaFragment(DetalleCitaFragment view) {
        this.view = view;
        this.sistemaReservas = SistemaReservas.getInstance();
    }

    public void cargarDetalle(int citaId) {
        if (citaId < 0) return;
        // buscar la cita en el modelo (usamos consultarCitas() si no hay método por id)
        List<Cita> todas = sistemaReservas.consultarCitas();
        Cita encontrada = null;
        for (Cita c : todas) {
            if (c.getId() == citaId) {
                encontrada = c;
                break;
            }
        }
        if (encontrada == null) {
            view.mostrarId("ID: " + citaId);
            view.mostrarMensaje("Cita no encontrada");
            return;
        }

        view.mostrarId(String.valueOf(encontrada.getId()));
        // buscar nombre paciente si existe
        Paciente p = sistemaReservas.leerPaciente(encontrada.getDocumento_paciente());
        String nombrePaciente = p != null ? p.getNombre() : encontrada.getDocumento_paciente();
        view.mostrarPaciente(nombrePaciente);
        view.mostrarDocumento(encontrada.getDocumento_paciente());


        String optName = sistemaReservas.getNombreOptometra(encontrada.getDocumento_optometra());
        if (optName == null) optName = encontrada.getDocumento_optometra();
        view.mostrarOptometra(optName);

        view.mostrarFecha(encontrada.getFecha().toString());
        view.mostrarHora(encontrada.getHora().toString());
        view.mostrarEstado(encontrada.getEstadoCita().toString());
        view.mostrarConsultorio(encontrada.getId_consultorio());
    }
}
