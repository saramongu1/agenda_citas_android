package com.example.agenda_optica_isis.presenter;

import com.example.agenda_optica_isis.exceptions.ValidacionException;
import com.example.agenda_optica_isis.exceptions.ValidarDatos;
import com.example.agenda_optica_isis.model.SistemaReservas;
import com.example.agenda_optica_isis.model.Usuario;
import com.example.agenda_optica_isis.view.LoginActivity;

public class PresenterLoginActivity {
    private LoginActivity view;
    private SistemaReservas sistemaReservas;

    public PresenterLoginActivity(LoginActivity view) {
        this.view = view;
        this.sistemaReservas = SistemaReservas.getInstance();
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

    private Usuario buscarUsuarioPorCorreo(String correo) {

        for (Usuario usuario : sistemaReservas.getTodosUsuarios().values()) {
            if (usuario.getCorreo_electronico().equalsIgnoreCase(correo)) {
                return usuario;
            }
        }
        return null;
    }
}
