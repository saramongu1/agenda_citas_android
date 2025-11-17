package com.example.agenda_optica_isis.model;

import java.time.LocalDate;

public class Usuario extends Persona{
    private String contrasenia;
    private boolean isAdmin;

    public Usuario(String correo_electronico,String contrasenia ,boolean isAdmin,String nombre, String numero_documento,
                   String numero_celular, int anio, int mes, int dia, String tipo_documento, String genero) {
        this.setCorreo_electronico(correo_electronico);
        this.setNombre(nombre);
        this.setNumero_documento(numero_documento);
        this.setNumero_celular(numero_celular);
        this.setFecha_nacimiento(dia, mes, anio);
        this.setContrasenia(contrasenia);
        this.setAdmin(isAdmin);
        super.asignarTipoDocumento(tipo_documento);
        super.asignarGenero(genero);
    }

    public Usuario() {
        super();
    }



    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
