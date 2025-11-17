package com.example.agenda_optica_isis.presenter;

import com.example.agenda_optica_isis.model.Cita;
import com.example.agenda_optica_isis.model.SistemaReservas;
import com.example.agenda_optica_isis.view.AgendaFragment;
import com.example.agenda_optica_isis.view.CitaUI;

import java.time.format.DateTimeFormatter;
import java.util.*;

public class PresenterAgendaFragment {

    private final AgendaFragment view;
    private final SistemaReservas sistemaReservas;
    private final List<Cita> todasCitas;
    private final int pageSize = 20;
    private int currentIndex = 0;

    public PresenterAgendaFragment(AgendaFragment view) {
        this.view = view;
        this.sistemaReservas = SistemaReservas.getInstance();
        this.todasCitas = new ArrayList<>();
    }

    public void cargarInicial() {
        List<Cita> raw = sistemaReservas.consultarCitas();

        raw.sort((o1, o2) -> {
            int cmp = o2.getFecha().compareTo(o1.getFecha());
            if (cmp == 0) return o2.getHora().compareTo(o1.getHora());
            return cmp;
        });

        todasCitas.clear();
        todasCitas.addAll(raw);
        currentIndex = 0;
        cargarSiguientePagina();
    }

    public void cargarSiguientePagina() {
        if (currentIndex >= todasCitas.size()) {
            view.mostrarBtnCargarMas(false);
            return;
        }

        int end = Math.min(currentIndex + pageSize, todasCitas.size());
        List<Cita> sub = todasCitas.subList(currentIndex, end);
        List<CitaUI> uiList = convertirACitaUI(sub);

        if (currentIndex == 0) {
            view.mostrarCitasInicial(uiList);
        } else {
            view.agregarMasCitas(uiList);
        }

        currentIndex = end;
        view.mostrarBtnCargarMas(currentIndex < todasCitas.size());
    }

    private List<CitaUI> convertirACitaUI(List<Cita> citas) {
        List<CitaUI> uiList = new ArrayList<>();

        for (Cita c : citas) {
            String id = String.valueOf(c.getId());
            String nombrePaciente = sistemaReservas.getNombrePaciente(c.getDocumento_paciente());
            String nombreOptometra = sistemaReservas.getNombreOptometra(c.getDocumento_optometra());
            String fechaHora = c.getFecha() + " " + c.getHora();
            String estado = c.getEstadoCita();

            uiList.add(new CitaUI(id, nombrePaciente, c.getDocumento_paciente(),
                    nombreOptometra, fechaHora, estado));
        }

        return uiList;
    }
}
