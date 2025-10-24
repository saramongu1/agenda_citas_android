package com.example.agenda_optica_isis.model;
import java.time.LocalDate;

public class Optometra extends Usuario{

    public Optometra(String correo_electronico,String contrasenia ,boolean isAdmin,String nombre, String numero_documento,
                     String numero_celular, int anio, int mes, int dia, String tipo_documento, String genero) {
        super(correo_electronico, contrasenia , isAdmin, nombre, numero_documento, numero_celular,  anio,  mes,  dia, tipo_documento, genero);
    }




}