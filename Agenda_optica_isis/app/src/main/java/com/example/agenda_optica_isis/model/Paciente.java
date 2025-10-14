package com.example.agenda_optica_isis.model;
import java.time.LocalDate;

public class Paciente extends Persona{

    public Paciente (String correo_electronico,String nombre, String numero_documento,
                     String numero_celular, int anio, int mes, int dia, String tipo_documento) {
        this.setCorreo_electronico(correo_electronico);
        this.setNombre(nombre);
        this.setNumero_documento(numero_documento);
        this.setNumero_celular(numero_celular);
        this.setFecha_nacimiento(LocalDate.of(anio, mes, dia));
        super.asignarTipoDocumento(tipo_documento);
    }



}
