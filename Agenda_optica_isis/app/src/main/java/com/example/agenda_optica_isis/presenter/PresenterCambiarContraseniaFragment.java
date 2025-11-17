package com.example.agenda_optica_isis.presenter;

import com.example.agenda_optica_isis.exceptions.ValidacionException;
import com.example.agenda_optica_isis.exceptions.ValidarDatos;
import com.example.agenda_optica_isis.model.SistemaReservas;
import com.example.agenda_optica_isis.model.Usuario;
import com.example.agenda_optica_isis.view.CambiarContraseniaFragment;

public class PresenterCambiarContraseniaFragment {
    private CambiarContraseniaFragment view;
    private SistemaReservas sistemaReservas;

    public PresenterCambiarContraseniaFragment(CambiarContraseniaFragment view) {
        this.view = view;
        this.sistemaReservas = SistemaReservas.getInstance();
    }

    public void cambiarContrasenia() {
        try {
            String contraseniaActual = view.getContraseniaActual();
            String nuevaContrasenia = view.getNuevaContrasenia();
            String confirmarContrasenia = view.getConfirmarContrasenia();

            // Validaciones
            ValidarDatos.validarTexto("contraseña actual", contraseniaActual);
            ValidarDatos.validarTexto("nueva contraseña", nuevaContrasenia);
            ValidarDatos.validarTexto("confirmar contraseña", confirmarContrasenia);
            ValidarDatos.validarLongitud("La nueva contraseña", nuevaContrasenia, 6); // Cambié a validarLongitud

            // Verificar que las nuevas contraseñas coincidan
            if (!nuevaContrasenia.equals(confirmarContrasenia)) {
                throw new ValidacionException("Las nuevas contraseñas no coinciden");
            }

            // Obtener usuario actual
            Usuario usuarioActual = sistemaReservas.getUsuarioActual();
            if (usuarioActual == null) {
                throw new Exception("No se pudo obtener la información del usuario");
            }

            // Verificar contraseña actual
            if (!usuarioActual.getContrasenia().equals(contraseniaActual)) {
                throw new ValidacionException("La contraseña actual es incorrecta");
            }

            // Verificar que la nueva contraseña sea diferente a la actual
            if (contraseniaActual.equals(nuevaContrasenia)) {
                throw new ValidacionException("La nueva contraseña debe ser diferente a la actual");
            }

            // Cambiar contraseña - CORREGIDO: usar el método correcto
            boolean seCambio = sistemaReservas.actualizarContrasena(
                    usuarioActual.getCorreo_electronico(), nuevaContrasenia
            );

            if (seCambio) {
                view.mostrarMensaje("Contraseña cambiada exitosamente");
                view.limpiarCampos();
                // Pequeño delay antes de regresar para que se vea el mensaje
                new android.os.Handler().postDelayed(() -> {
                    view.irAUsuarioFragment();
                }, 1000);
            } else {
                throw new Exception("Error al cambiar la contraseña en el sistema");
            }

        } catch (ValidacionException validacionException) {
            view.mostrarMensaje(validacionException.getMessage());
        } catch (Exception e) {
            view.mostrarMensaje("Error: " + e.getMessage());
        }
    }
}