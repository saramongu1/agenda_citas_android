package com.example.agenda_optica_isis.view;

import com.example.agenda_optica_isis.model.EstadoCita;

import java.time.LocalDate;
import java.time.LocalTime;

public class CitaVisual {
    private int id;
    private String nombrePaciente;
    private String nombreOptometra;
    private String consultorio;
    private LocalDate fecha;
    private LocalTime hora;
    private EstadoCita estado;

    public CitaVisual(int id, String nombrePaciente, String nombreOptometra,
                      String consultorio, LocalDate fecha, LocalTime hora, EstadoCita estado) {
        this.id = id;
        this.nombrePaciente = nombrePaciente;
        this.nombreOptometra = nombreOptometra;
        this.consultorio = consultorio;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
    }

    // Getters
    public int getId() { return id; }
    public String getNombrePaciente() { return nombrePaciente; }
    public String getNombreOptometra() { return nombreOptometra; }
    public String getConsultorio() { return consultorio; }
    public LocalDate getFecha() { return fecha; }
    public LocalTime getHora() { return hora; }
    public EstadoCita getEstado() { return estado; }
}