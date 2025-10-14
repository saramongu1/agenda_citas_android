package com.example.agenda_optica_isis.model;
import java.time.LocalDate;
import java.time.LocalTime;

public class Cita {
    private static int contador = 0;
    private int id;
    private String documento_optometra;
    private String documento_paciente;
    private String id_consultorio;
    private LocalDate fecha;
    private LocalTime hora;
    private EstadoCita estadoCita;

    public Cita(String documento_optometra, String documento_paciente, String id_consultorio, int anio, int mes, int dia,
                int hora, int minutos) {
        this.id = asignarID();;
        this.documento_optometra = documento_optometra;
        this.documento_paciente = documento_paciente;
        this.id_consultorio = id_consultorio;
        this.setFecha(LocalDate.of(anio, mes, dia));
        this.setHora(LocalTime.of(hora, minutos));
        initEstadoCita("PROGRAMADA");
    }

    public int asignarID() {
        return contador++;
    }

    public boolean initEstadoCita(String estadoCita) {
        boolean exists = true;
        if(estadoCita.equalsIgnoreCase("PROGRAMADA")) {
            setEstadoCita(EstadoCita.PROGRAMADA);
        }if(estadoCita.equalsIgnoreCase("CANCELADA")) {
            setEstadoCita(EstadoCita.CANCELADA);
        }if(estadoCita.equalsIgnoreCase("COMPLETADA")) {
            setEstadoCita(EstadoCita.COMPLETADA);
        }if(estadoCita.equalsIgnoreCase("NO ASISTIO")) {
            setEstadoCita(EstadoCita.NO_ASISTIO);
        }else {
            exists = false;
        }
        return exists;
    }

    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        Cita.contador = contador;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocumento_optometra() {
        return documento_optometra;
    }

    public void setDocumento_optometra(String documento_optometra) {
        this.documento_optometra = documento_optometra;
    }

    public String getDocumento_paciente() {
        return documento_paciente;
    }

    public void setDocumento_paciente(String documento_paciente) {
        this.documento_paciente = documento_paciente;
    }

    public String getId_consultorio() {
        return id_consultorio;
    }

    public void setId_consultorio(String id_consultorio) {
        this.id_consultorio = id_consultorio;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public EstadoCita getEstadoCita() {
        return estadoCita;
    }

    public void setEstadoCita(EstadoCita estadoCita) {
        this.estadoCita = estadoCita;
    }
}
