package com.example.agenda_optica_isis.presenter;

import com.example.agenda_optica_isis.exceptions.ValidacionException;
import com.example.agenda_optica_isis.exceptions.ValidarDatos;
import com.example.agenda_optica_isis.model.EmailService;
import com.example.agenda_optica_isis.model.SistemaReservas;
import com.example.agenda_optica_isis.model.Usuario;
import com.example.agenda_optica_isis.view.LoginActivity;

public class PresenterLoginActivity {
    private LoginActivity view;
    private SistemaReservas sistemaReservas;
    private EmailService emailService;

    public PresenterLoginActivity(LoginActivity view) {
        this.view = view;
        this.sistemaReservas = SistemaReservas.getInstance();
        this.emailService = new EmailService();
    }

    public void iniciarSesion() {
        try {
            String mail = view.getMailText();
            String contrasenia = view.getPasswordText();

            ValidarDatos.validarTexto("correo electrónico", mail);
            ValidarDatos.validarTexto("contraseña", contrasenia);
            ValidarDatos.validarCorreo(mail);
            ValidarDatos.validarLongitud("La contraseña", contrasenia, 8);
            ValidarDatos.validarTextosInicioSesion(mail, contrasenia);

            boolean exito = sistemaReservas.iniciarSesion(mail, contrasenia);
            if (exito) {
                Usuario usuario = buscarUsuarioPorCorreo(mail);
                sistemaReservas.setUsuarioActual(usuario);
                view.irAMenu();
            } else {
                view.mostrarMensaje("Credenciales incorrectas");
            }

        } catch (ValidacionException e) {
            view.mostrarMensaje(e.getMessage());
        } catch (Exception e) {
            view.mostrarMensaje("Error inesperado: " + e.getMessage());
        }
    }

    public void recuperarContrasena() {
        try {
            String mail = view.getMailText();

            // Validar que el correo no esté vacío
            ValidarDatos.validarTexto("correo electrónico", mail);
            ValidarDatos.validarCorreo(mail);

            // Buscar usuario por correo
            Usuario usuario = buscarUsuarioPorCorreo(mail);

            if (usuario == null) {
                view.mostrarMensaje("No existe un usuario con este correo electrónico");
                return;
            }

            // Generar nueva contraseña temporal
            String nuevaContrasena = sistemaReservas.generarContrasena();

            // Actualizar contraseña en el sistema
            boolean contrasenaActualizada = sistemaReservas.actualizarContrasena(mail, nuevaContrasena);

            if (contrasenaActualizada) {
                // Enviar correo con la nueva contraseña
                emailService.enviarCorreoRecuperacion(mail, usuario.getNombre(), nuevaContrasena,
                        new EmailService.EmailCallback() {
                            @Override
                            public void onSuccess() {
                                view.runOnUiThread(() -> {
                                    view.mostrarMensaje("Se ha enviado una nueva contraseña a tu correo");
                                    view.limpiarCampoContrasena(); // Limpiar campo de contraseña
                                });
                            }

                            @Override
                            public void onError(String error) {
                                view.runOnUiThread(() -> {
                                    view.mostrarMensaje("Contraseña actualizada pero error enviando correo: " + error);
                                });
                            }
                        });
            } else {
                view.mostrarMensaje("Error actualizando la contraseña");
            }

        } catch (ValidacionException e) {
            view.mostrarMensaje(e.getMessage());
        } catch (Exception e) {
            view.mostrarMensaje("Error inesperado: " + e.getMessage());
        }
    }

    private Usuario buscarUsuarioPorCorreo(String correo) {
        for (Usuario usuario : sistemaReservas.getTodosUsuarios().values()) {
            if (usuario.getCorreo_electronico().equalsIgnoreCase(correo)) {
                return usuario;
            }
        }
        return null;
    }
}