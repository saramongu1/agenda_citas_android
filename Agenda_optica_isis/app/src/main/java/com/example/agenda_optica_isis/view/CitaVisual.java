package com.example.agenda_optica_isis.view;

import java.time.LocalDate;
import java.time.LocalTime;

public class CitaVisual {
    private int id;
    private String nombrePaciente;
    private String nombreOptometra;
    private String consultorio;
    private LocalDate fecha;
    private LocalTime hora;
    private String estado;

    public CitaVisual(int id, String nombrePaciente, String nombreOptometra,
                      String consultorio, LocalDate fecha, LocalTime hora, String estado) {
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
    public String getEstado() { return estado; }
}