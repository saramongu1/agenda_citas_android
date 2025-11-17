package com.example.agenda_optica_isis.presenter;

import com.example.agenda_optica_isis.view.RecuperarPasswordActivity;

public class PresenterRecuperarPassword {

    private RecuperarPasswordActivity view;

    public PresenterRecuperarPassword(RecuperarPasswordActivity view) {
        this.view = view;
    }

    public void enviarCodigoVerificacion(String email) {

        if (email.equals("usuario@ejemplo.com")) {
            view.onCodigoEnviado();
        } else {
            view.onErrorEnvio("No existe una cuenta asociada a este correo electrónico");
        }
    }

    public void verificarCodigo(String codigo) {
        if (codigo.equals("123456")) {
            view.onCodigoVerificado();
        } else {
            view.onErrorVerificacion("Código incorrecto. Intenta nuevamente.");
        }
    }

    public void cambiarPassword(String nuevaPassword) {
        if (nuevaPassword.length() >= 6) {
            view.onPasswordCambiada();
        } else {
            view.onErrorCambioPassword("Error al cambiar la contraseña. Intenta nuevamente.");
        }
    }
}