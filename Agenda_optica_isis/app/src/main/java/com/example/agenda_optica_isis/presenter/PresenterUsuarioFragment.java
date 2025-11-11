package com.example.agenda_optica_isis.presenter;

import com.example.agenda_optica_isis.exceptions.ValidacionException;
import com.example.agenda_optica_isis.exceptions.ValidarDatos;
import com.example.agenda_optica_isis.model.SistemaReservas;
import com.example.agenda_optica_isis.model.Usuario;
import com.example.agenda_optica_isis.view.UsuarioFragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PresenterUsuarioFragment {
    private UsuarioFragment view;
    private SistemaReservas sistemaReservas;

    public PresenterUsuarioFragment(UsuarioFragment view) {
        this.view = view;
        this.sistemaReservas = SistemaReservas.getInstance();
    }

    public void cargarInformacionUsuario() {
        try {
            Usuario usuarioActual = sistemaReservas.getUsuarioActual();

            if (usuarioActual != null) {
                String nombre = usuarioActual.getNombre();
                String documento = usuarioActual.getNumero_documento();
                String tipo_documento = usuarioActual.getTipo_documento();
                String genero = usuarioActual.getGenero();
                String numero_celular = usuarioActual.getNumero_celular();
                String correo_electronico = usuarioActual.getCorreo_electronico();

                // Formatear fecha de nacimiento correctamente
                LocalDate fechaNacimiento = usuarioActual.getFecha_nacimientoFormat();
                String fecha_nacimiento = "";
                if (fechaNacimiento != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    fecha_nacimiento = fechaNacimiento.format(formatter);
                }

                view.asignarDetallesUsuario(nombre, documento, tipo_documento, genero,
                        numero_celular, correo_electronico, fecha_nacimiento);
            } else {
                view.mostrarMensaje("No se pudo cargar la información del usuario");
            }
        } catch (Exception e) {
            view.mostrarMensaje("Error al cargar información: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void actualizarUsuario() {
        try {
            String correo_electronico = view.getCorreoUsuario();
            String nombre = view.getNombreUsuario();
            String numero_documento = view.getDocumentoUsuario().trim();
            String numero_celular = view.getNumeroCelularUsuario();
            String fecha_nacimiento = view.getFechaNacimientoUsuario();
            String tipo_documento = view.getTipoDocumentoUsuario();
            String genero = view.getGeneroUsuario();

            // Validar que la fecha no esté vacía
            if (fecha_nacimiento.isEmpty()) {
                view.mostrarMensaje("La fecha de nacimiento es obligatoria");
                return;
            }

            int anio = getAnioNacimiento(fecha_nacimiento);
            int mes = getMesNacimiento(fecha_nacimiento);
            int dia = getDiaNacimiento(fecha_nacimiento);

            // Validaciones
            if (!correo_electronico.isEmpty()) {
                ValidarDatos.validarCorreo(correo_electronico);
            }

            ValidarDatos.validarTexto("nombre", nombre);
            ValidarDatos.validarTexto("documento", numero_documento);
            ValidarDatos.validarTexto("tipo de documento", tipo_documento);
            ValidarDatos.validarTexto("fecha de nacimiento", fecha_nacimiento);
            ValidarDatos.validarTexto("Número de celular", numero_celular);
            ValidarDatos.validarLongitud("documento", numero_documento, 10);
            ValidarDatos.validarLongitud("número de celular", numero_celular, 10);
            ValidarDatos.validarLongitud("nombre", nombre, 6);

            boolean seActualizo = sistemaReservas.actualizarUsuario(
                    numero_documento, correo_electronico, nombre, numero_celular,
                    anio, mes, dia, tipo_documento, genero
            );

            if (seActualizo) {
                view.mostrarMensaje("Perfil actualizado exitosamente");
                view.deshabilitarCampos();
                view.irAMenu();
            } else {
                view.mostrarMensaje("No se pudo actualizar el perfil");
            }
        } catch (ValidacionException validacionException) {
            view.mostrarMensaje(validacionException.getMessage());
        } catch (Exception e) {
            view.mostrarMensaje("Error al actualizar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int getDiaNacimiento(String fecha_nacimiento) {
        try {
            String[] fechaSplit = fecha_nacimiento.split("/");
            return Integer.parseInt(fechaSplit[0]);
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use DD/MM/AAAA");
        }
    }

    public int getMesNacimiento(String fecha_nacimiento) {
        try {
            String[] fechaSplit = fecha_nacimiento.split("/");
            return Integer.parseInt(fechaSplit[1]);
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use DD/MM/AAAA");
        }
    }

    public int getAnioNacimiento(String fecha_nacimiento) {
        try {
            String[] fechaSplit = fecha_nacimiento.split("/");
            return Integer.parseInt(fechaSplit[2]);
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use DD/MM/AAAA");
        }
    }
}