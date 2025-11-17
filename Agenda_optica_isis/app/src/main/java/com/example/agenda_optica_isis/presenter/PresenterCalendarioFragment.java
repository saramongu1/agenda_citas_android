package com.example.agenda_optica_isis.presenter;

import android.util.Log;

import com.example.agenda_optica_isis.model.Cita;
import com.example.agenda_optica_isis.model.SistemaReservas;
import com.example.agenda_optica_isis.model.EstadoCita;
import com.example.agenda_optica_isis.view.CalendarioFragment;
import com.example.agenda_optica_isis.view.CitaVisual;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PresenterCalendarioFragment {
    private static final String TAG = "PresenterCalendario";
    private final CalendarioFragment view;
    private final SistemaReservas sistemaReservas;
    private LocalDate fechaSeleccionada;
    private String tipoVista; // "DIA" o "SEMANA"


    public PresenterCalendarioFragment(CalendarioFragment view) {
        this.view = view;
        this.sistemaReservas = SistemaReservas.getInstance();
        this.fechaSeleccionada = LocalDate.now();
        this.tipoVista = "DIA";
        Log.d(TAG, "Presenter inicializado - Fecha: " + fechaSeleccionada + ", Vista: " + tipoVista);
    }

    public void cargarCitas() {
        try {
            Log.d(TAG, "Cargando citas - Vista: " + tipoVista + ", Fecha: " + fechaSeleccionada);

            List<Cita> citasFiltradas;

            if ("DIA".equals(tipoVista)) {
                Log.d(TAG, "Consultando citas del día: " + fechaSeleccionada);
                citasFiltradas = sistemaReservas.consultarCitasDia(
                        fechaSeleccionada.getDayOfMonth(),
                        fechaSeleccionada.getMonthValue(),
                        fechaSeleccionada.getYear()
                );
                Log.d(TAG, "Citas del día encontradas: " + citasFiltradas.size());
            } else if ("SEMANA".equals(tipoVista)) {
                LocalDate inicioSemana = fechaSeleccionada.minusDays(fechaSeleccionada.getDayOfWeek().getValue() - 1);
                LocalDate finSemana = inicioSemana.plusDays(6);
                Log.d(TAG, "Consultando citas de la semana: " + inicioSemana + " a " + finSemana);

                citasFiltradas = sistemaReservas.consultarCitasSemana(
                        inicioSemana.getDayOfMonth(),
                        finSemana.getDayOfMonth(),
                        fechaSeleccionada.getMonthValue()
                );
                Log.d(TAG, "Citas de la semana encontradas: " + citasFiltradas.size());
            } else {
                Log.e(TAG, "Tipo de vista desconocido en cargarCitas: " + tipoVista);
                citasFiltradas = new ArrayList<>();
            }

            List<CitaVisual> citasVisuales = convertirCitasVisuales(citasFiltradas);
            Log.d(TAG, "Citas visuales convertidas: " + citasVisuales.size());
            view.mostrarCitas(citasVisuales);

        } catch (Exception e) {
            Log.e(TAG, "Error al cargar citas: " + e.getMessage(), e);
            view.mostrarError("Error al cargar citas: " + e.getMessage());
        }
    }

    private List<CitaVisual> convertirCitasVisuales(List<Cita> citas) {
        List<CitaVisual> citasVisuales = new ArrayList<>();
        Log.d(TAG, "Convirtiendo " + citas.size() + " citas a visuales");

        for (Cita cita : citas) {
            String nombrePaciente = sistemaReservas.getNombrePaciente(cita.getDocumento_paciente());
            String nombreOptometra = sistemaReservas.getNombreOptometra(cita.getDocumento_optometra());

            Log.d(TAG, "Cita ID: " + cita.getId() +
                    ", Paciente: " + nombrePaciente +
                    ", Optometra: " + nombreOptometra +
                    ", Fecha: " + cita.getFecha() +
                    ", Hora: " + cita.getHora());

            citasVisuales.add(new CitaVisual(
                    cita.getId(),
                    nombrePaciente,
                    nombreOptometra,
                    cita.getId_consultorio(),
                    cita.getFecha(),
                    cita.getHora(),
                    cita.getEstadoCita()
            ));
        }
        return citasVisuales;
    }

    public void cambiarVista(String tipoVista) {
        Log.d(TAG, "Cambiando vista de " + this.tipoVista + " a " + tipoVista);
        // Convertir a mayúsculas y quinar acentos
        if (tipoVista != null) {
            String vistaNormalizada = tipoVista.toUpperCase()
                    .replace("Í", "I")
                    .replace("É", "E")
                    .replace("Á", "A")
                    .replace("Ó", "O")
                    .replace("Ú", "U");
            this.tipoVista = vistaNormalizada;
            Log.d(TAG, "Vista normalizada: " + this.tipoVista);
        }
    }

    public void cambiarFecha(LocalDate nuevaFecha) {
        Log.d(TAG, "Cambiando fecha de " + this.fechaSeleccionada + " a " + nuevaFecha);
        this.fechaSeleccionada = nuevaFecha;
    }

    public LocalDate getFechaSeleccionada() {
        return fechaSeleccionada;
    }

    public String getTipoVista() {
        return tipoVista;
    }

    public void onCitaClick(int idCita) {
        Log.d(TAG, "Clic en cita ID: " + idCita);
        view.mostrarDetalleCita(idCita);
    }

    public void onEspacioVacioClick(LocalTime hora, LocalDate fecha) {
        Log.d(TAG, "Clic en espacio vacío - Fecha: " + fecha + ", Hora: " + hora);
        view.crearNuevaCita(hora, fecha);
    }

    public List<LocalDate> getDiasSemana() {
        if ("SEMANA".equals(tipoVista)) {
            LocalDate inicioSemana = fechaSeleccionada.minusDays(fechaSeleccionada.getDayOfWeek().getValue() - 1);
            List<LocalDate> dias = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                dias.add(inicioSemana.plusDays(i));
            }
            Log.d(TAG, "Días de la semana generados: " + dias);
            return dias;
        }
        Log.d(TAG, "getDiasSemana retornando null - tipoVista: " + tipoVista);
        return null;
    }
}