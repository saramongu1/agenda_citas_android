package com.example.agenda_optica_isis.model;

import com.google.firebase.database.Exclude; // AGREGAR ESTA IMPORTACIÓN

public class Cita {
    private static int contador = 0;
    private int id;
    private String documento_optometra;
    private String documento_paciente;
    private String id_consultorio;
    private String fecha;
    private String hora;
    private String estadoCita;

    public Cita(String documento_optometra, String documento_paciente, String id_consultorio, int anio, int mes, int dia,
                int hora, int minutos) {
        this.id = asignarID();;
        this.documento_optometra = documento_optometra;
        this.documento_paciente = documento_paciente;
        this.id_consultorio = id_consultorio;
        this.setFecha(dia, mes, anio);
        this.setHora(hora, minutos);
        initEstadoCita("PROGRAMADA");
    }

    public Cita() {
    }

    public int asignarID() {
        return contador++;
    }

    public void initEstadoCita(String estadoCita) {
        setEstadoCita(estadoCita);
    }

    public void setFecha(int dia, int mes, int anio) {
        this.fecha = String.format("%02d-%02d-%04d", dia, mes, anio);
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    // AGREGAR @Exclude AQUÍ
    @Exclude
    public int getAnio() {
        if (fecha != null && fecha.contains("-")) {
            String[] partes = fecha.split("-");
            return Integer.parseInt(partes[2]);
        }
        return 0;
    }

    // AGREGAR @Exclude AQUÍ
    @Exclude
    public int getMes() {
        if (fecha != null && fecha.contains("-")) {
            String[] partes = fecha.split("-");
            return Integer.parseInt(partes[1]);
        }
        return 0;
    }

    // AGREGAR @Exclude AQUÍ
    @Exclude
    public int getDia() {
        if (fecha != null && fecha.contains("-")) {
            String[] partes = fecha.split("-");
            return Integer.parseInt(partes[0]);
        }
        return 0;
    }

    public void setHora(int hora, int minutos) {
        this.hora = String.format("%02d:%02d", hora, minutos);
    }

    public void setHora(int hora, String minutos) {
        this.hora = String.format("%02d:%02d", hora, Integer.parseInt(minutos));
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    // AGREGAR @Exclude AQUÍ
    @Exclude
    public int getHoraInt() {
        if (hora != null && hora.contains(":")) {
            return Integer.parseInt(hora.split(":")[0]);
        }
        return 0;
    }

    // AGREGAR @Exclude AQUÍ
    @Exclude
    public int getMinutosInt() {
        if (hora != null && hora.contains(":")) {
            return Integer.parseInt(hora.split(":")[1]);
        }
        return 0;
    }

    // AGREGAR @Exclude AQUÍ
    @Exclude
    public java.time.LocalDate getFechaFormat() {
        if (fecha != null && fecha.contains("-")) {
            String[] partes = fecha.split("-");
            return java.time.LocalDate.of(
                    Integer.parseInt(partes[2]),
                    Integer.parseInt(partes[1]),
                    Integer.parseInt(partes[0])
            );
        }
        return java.time.LocalDate.now();
    }

    // AGREGAR @Exclude AQUÍ
    @Exclude
    public java.time.LocalTime getHoraFormat() {
        if (hora != null && hora.contains(":")) {
            String[] partes = hora.split(":");
            return java.time.LocalTime.of(
                    Integer.parseInt(partes[0]),
                    Integer.parseInt(partes[1])
            );
        }
        return java.time.LocalTime.now();
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

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getEstadoCita() {
        return estadoCita;
    }

    public void setEstadoCita(String estadoCita) {
        this.estadoCita = estadoCita;
    }

    // AGREGAR @Exclude AQUÍ
    @Exclude
    public boolean esParaFecha(int anio, int mes, int dia) {
        return this.getAnio() == anio && this.getMes() == mes && this.getDia() == dia;
    }

    // AGREGAR @Exclude AQUÍ
    @Exclude
    public boolean esParaHorario(int hora, int minutos) {
        return this.getHoraInt() == hora && this.getMinutosInt() == minutos;
    }

    @Override
    public String toString() {
        return "Cita{" +
                "id=" + id +
                ", optometra='" + documento_optometra + '\'' +
                ", paciente='" + documento_paciente + '\'' +
                ", consultorio='" + id_consultorio + '\'' +
                ", fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                ", estado='" + estadoCita + '\'' +
                '}';
    }

    public void cambiarEstado(String nuevoEstado) {
        this.estadoCita = nuevoEstado;
    }

    // AGREGAR @Exclude AQUÍ
    @Exclude
    public boolean estaProgramada() {
        return "PROGRAMADA".equals(estadoCita);
    }

    // AGREGAR @Exclude AQUÍ
    @Exclude
    public boolean estaCancelada() {
        return "CANCELADA".equals(estadoCita);
    }

    // AGREGAR @Exclude AQUÍ
    @Exclude
    public boolean estaCompletada() {
        return "COMPLETADA".equals(estadoCita);
    }

    // AGREGAR @Exclude AQUÍ
    @Exclude
    public void setFechaFromComponents(int anio, int mes, int dia) {
        this.fecha = String.format("%02d-%02d-%04d", dia, mes, anio);
    }

    // AGREGAR @Exclude AQUÍ
    @Exclude
    public void setHoraFromComponents(int hora, int minutos) {
        this.hora = String.format("%02d:%02d", hora, minutos);
    }
}