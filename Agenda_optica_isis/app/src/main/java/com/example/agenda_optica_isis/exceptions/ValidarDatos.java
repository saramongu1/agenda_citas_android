package com.example.agenda_optica_isis.exceptions;

import android.util.Patterns;

import java.time.LocalDate;
import java.time.LocalTime;

public class ValidarDatos {

    public static void validarTexto(String campo, String valor) throws ValidacionException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ValidacionException("El campo " + campo + " no puede estar vacío.");
        }
    }

    public static void validarTextosInicioSesion(String mail, String contrasenia) throws ValidacionException {
        if (mail.isEmpty() || contrasenia.isEmpty()) {
            throw new ValidacionException("Debes completar todos los campos");
        }
    }

    public static void validarCorreo(String correo) throws ValidacionException {
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            throw new ValidacionException("El correo electrónico no es válido.");
        }
    }

    public static void validarLongitud(String campo, String valor, int min) throws ValidacionException {
        if (valor.length() < min) {
            throw new ValidacionException(campo + " debe tener al menos " + min + " caracteres.");
        }
    }

    public static void validarFechaCita(int anio, int mes , int dia, int hora, int minutos) throws ValidacionException{
        LocalDate fecha_cita = LocalDate.of(anio,mes,dia);
        LocalTime hora_cita = LocalTime.of(hora,minutos);
        LocalDate fecha_actual = LocalDate.now();
        LocalTime hora_actual = LocalTime.now();

        if(fecha_cita.equals(fecha_actual)){
            if(hora_cita.isBefore(hora_actual) || hora_cita.equals(hora_actual)){
                throw new ValidacionException("Ingrese una hora valida");
            }
        }

    }
}
