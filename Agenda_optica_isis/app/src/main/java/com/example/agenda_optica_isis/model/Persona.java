package com.example.agenda_optica_isis.model;

import com.google.firebase.database.Exclude; // AGREGAR ESTA IMPORTACIÓN

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Persona {
    private String nombre;
    private String numero_documento;
    private String numero_celular;
    private String fecha_nacimiento;
    private String tipo_documento;
    private String correo_electronico;
    private String genero;

    public Persona() {
        nombre = "";
        numero_documento = "";
        numero_celular = "";
        fecha_nacimiento = "";
        tipo_documento = "";
        correo_electronico = "";
        genero = "";
    }

    public Persona(String correo_electronico, String nombre, String numero_documento,
                   String numero_celular, String fecha_nacimiento,
                   String tipo_documento, String genero) {
        this.correo_electronico = correo_electronico;
        this.nombre = nombre;
        this.numero_documento = numero_documento;
        this.numero_celular = numero_celular;
        this.fecha_nacimiento = fecha_nacimiento;
        this.tipo_documento = tipo_documento;
        this.genero = genero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumero_documento() {
        return numero_documento;
    }

    public void setNumero_documento(String numero_documento) {
        this.numero_documento = numero_documento;
    }

    public String getNumero_celular() {
        return numero_celular;
    }

    public void setNumero_celular(String numero_celular) {
        this.numero_celular = numero_celular;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    // AGREGAR @Exclude AQUÍ
    @Exclude
    public LocalDate getFecha_nacimientoFormat() {
        String[]fecha = fecha_nacimiento.split("-");
        return LocalDate.of(Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]), Integer.parseInt(fecha[0]));
    }

    public void setFecha_nacimiento(int dia, int mes, int anio) {
        this.fecha_nacimiento = dia + "-" + mes + "-" + anio;
    }

    // AGREGAR @Exclude AQUÍ
    @Exclude
    public void setFechaNacimientoFromComponents(int anio, int mes, int dia) {
        LocalDate fecha = LocalDate.of(anio, mes, dia);
        this.fecha_nacimiento = String.format("%02d-%02d-%04d", dia, mes, anio); // Mantener formato dd-MM-yyyy
    }

    // AGREGAR @Exclude AQUÍ
    @Exclude
    public LocalDate getFechaNacimientoAsLocalDate() {
        if (fecha_nacimiento == null || fecha_nacimiento.isEmpty()) {
            return null;
        }
        try {
            String[] partes = fecha_nacimiento.split("-");
            return LocalDate.of(
                    Integer.parseInt(partes[2]), // año
                    Integer.parseInt(partes[1]), // mes
                    Integer.parseInt(partes[0])  // día
            );
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public String getTipo_documento() {
        return tipo_documento != null ? tipo_documento : "";
    }

    public void setTipo_documento(String tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    public String getCorreo_electronico() {
        return correo_electronico;
    }

    public void setCorreo_electronico(String correo_electronico) {
        this.correo_electronico = correo_electronico;
    }

    public String getGenero() {
        return genero != null ? genero : "";
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void asignarTipoDocumento(String input_tipo_documento) {
        setTipo_documento(input_tipo_documento);
    }

    public void asignarGenero(String inputGenero) {
        setGenero(inputGenero);
    }

    @Override
    public String toString() {
        return "Persona{" +
                "nombre='" + nombre + '\'' +
                ", numero_documento='" + numero_documento + '\'' +
                ", correo_electronico='" + correo_electronico + '\'' +
                ", fecha_nacimiento='" + fecha_nacimiento + '\'' +
                '}';
    }
}