package com.example.agenda_optica_isis.presenter;

import com.example.agenda_optica_isis.model.Cita;
import com.example.agenda_optica_isis.model.SistemaReservas;
import com.example.agenda_optica_isis.view.AgendaFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PresenterAgendaFragment {

    private AgendaFragment view;
    private SistemaReservas sistemaReservas;

    private List<Cita> todasCitas;
    private int pageSize = 20;
    private int currentIndex = 0;

    public PresenterAgendaFragment(AgendaFragment view) {
        this.view = view;
        this.sistemaReservas = SistemaReservas.getInstance();
        this.todasCitas = new ArrayList<>();
    }

    public void cargarInicial() {
        // traer todas las citas desde el modelo
        List<Cita> raw = sistemaReservas.consultarCitas();
        // ordenar por fecha+hora descendente (más próximas primero):
        Collections.sort(raw, new Comparator<Cita>() {
            @Override
            public int compare(Cita o1, Cita o2) {
                int cmp = o2.getFecha().compareTo(o1.getFecha()); // fecha desc
                if (cmp == 0) {
                    return o2.getHora().compareTo(o1.getHora()); // hora desc
                }
                return cmp;
            }
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
        if (currentIndex == 0) {
            view.mostrarCitasInicial(new ArrayList<>(sub));
        } else {
            view.agregarMasCitas(new ArrayList<>(sub));
        }
        currentIndex = end;
        view.mostrarBtnCargarMas(currentIndex < todasCitas.size());
    }

    public void buscarCitasPorPaciente(String documento) {
        // método opcional: filtrar por documento
        List<Cita> filtradas = new ArrayList<>();
        for (Cita c : sistemaReservas.consultarCitas()) {
            if (c.getDocumento_paciente().equalsIgnoreCase(documento)) {
                filtradas.add(c);
            }
        }
        // ordenar igual que antes
        Collections.sort(filtradas, (o1, o2) -> {
            int cmp = o2.getFecha().compareTo(o1.getFecha());
            if (cmp == 0) return o2.getHora().compareTo(o1.getHora());
            return cmp;
        });
        todasCitas = filtradas;
        currentIndex = 0;
        cargarSiguientePagina();
    }
}
