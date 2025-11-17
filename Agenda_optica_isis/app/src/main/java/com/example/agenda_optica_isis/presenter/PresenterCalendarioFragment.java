package com.example.agenda_optica_isis.presenter;

import com.example.agenda_optica_isis.model.Cita;
import com.example.agenda_optica_isis.model.SistemaReservas;
import com.example.agenda_optica_isis.view.CalendarioFragment;
import com.example.agenda_optica_isis.view.CitaVisual;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PresenterCalendarioFragment {
    private final CalendarioFragment view;
    private final SistemaReservas sistemaReservas;
    private LocalDate fechaSeleccionada;
    private String tipoVista; // "DIA" o "SEMANA"


    public PresenterCalendarioFragment(CalendarioFragment view) {
        this.view = view;
        this.sistemaReservas = SistemaReservas.getInstance();
        this.fechaSeleccionada = LocalDate.now();
        this.tipoVista = "DIA";
    }

    public void cargarCitas() {
        try {

            List<Cita> citasFiltradas;

            if ("DIA".equals(tipoVista)) {
                citasFiltradas = sistemaReservas.consultarCitasDia(
                        fechaSeleccionada.getDayOfMonth(),
                        fechaSeleccionada.getMonthValue(),
                        fechaSeleccionada.getYear()
                );
            } else if ("SEMANA".equals(tipoVista)) {
                LocalDate inicioSemana = fechaSeleccionada.minusDays(fechaSeleccionada.getDayOfWeek().getValue() - 1);
                LocalDate finSemana = inicioSemana.plusDays(6);
                citasFiltradas = sistemaReservas.consultarCitasSemana(
                        inicioSemana.getDayOfMonth(),
                        finSemana.getDayOfMonth(),
                        fechaSeleccionada.getMonthValue()
                );
            } else {
                citasFiltradas = new ArrayList<>();
            }

            List<CitaVisual> citasVisuales = convertirCitasVisuales(citasFiltradas);
            view.mostrarCitas(citasVisuales);

        } catch (Exception e) {
            view.mostrarError("Error al cargar citas: " + e.getMessage());
        }
    }

    private List<CitaVisual> convertirCitasVisuales(List<Cita> citas) {
        List<CitaVisual> citasVisuales = new ArrayList<>();

        for (Cita cita : citas) {
            String nombrePaciente = sistemaReservas.getNombrePaciente(cita.getDocumento_paciente());
            String nombreOptometra = sistemaReservas.getNombreOptometra(cita.getDocumento_optometra());

            citasVisuales.add(new CitaVisual(
                    cita.getId(),
                    nombrePaciente,
                    nombreOptometra,
                    cita.getId_consultorio(),
                    cita.getFechaFormat(),
                    cita.getHoraFormat(),
                    cita.getEstadoCita()
            ));
        }
        return citasVisuales;
    }

    public void cambiarVista(String tipoVista) {
        if (tipoVista != null) {
            String vistaNormalizada = tipoVista.toUpperCase()
                    .replace("Í", "I")
                    .replace("É", "E")
                    .replace("Á", "A")
                    .replace("Ó", "O")
                    .replace("Ú", "U");
            this.tipoVista = vistaNormalizada;
        }
    }

    public void cambiarFecha(LocalDate nuevaFecha) {
        this.fechaSeleccionada = nuevaFecha;
    }

    public LocalDate getFechaSeleccionada() {
        return fechaSeleccionada;
    }

    public String getTipoVista() {
        return tipoVista;
    }

    public void onCitaClick(int idCita) {
        view.mostrarDetalleCita(idCita);
    }

    public void onEspacioVacioClick(LocalTime hora, LocalDate fecha) {
        view.crearNuevaCita(hora, fecha);
    }

    public List<LocalDate> getDiasSemana() {
        if ("SEMANA".equals(tipoVista)) {
            LocalDate inicioSemana = fechaSeleccionada.minusDays(fechaSeleccionada.getDayOfWeek().getValue() - 1);
            List<LocalDate> dias = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                dias.add(inicioSemana.plusDays(i));
            }
            return dias;
        }
        return null;
    }
}