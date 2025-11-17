package com.example.agenda_optica_isis.presenter;

import com.example.agenda_optica_isis.exceptions.ValidacionException;
import com.example.agenda_optica_isis.exceptions.ValidarDatos;
import com.example.agenda_optica_isis.model.EmailService;
import com.example.agenda_optica_isis.model.SistemaReservas;
import com.example.agenda_optica_isis.model.Usuario;
import com.example.agenda_optica_isis.view.RecuperarPasswordActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PresenterRecuperarPassword {

    private RecuperarPasswordActivity view;
    private SistemaReservas sistemaReservas;
    private EmailService emailService;
    private Map<String, String> codigosVerificacion; // <email, codigo>
    private Map<String, Long> tiemposExpiracion; // <email, timestamp>

    public PresenterRecuperarPassword(RecuperarPasswordActivity view) {
        this.view = view;
        this.sistemaReservas = SistemaReservas.getInstance();
        this.emailService = new EmailService();
        this.codigosVerificacion = new HashMap<>();
        this.tiemposExpiracion = new HashMap<>();
    }

    public void enviarCodigoVerificacion(String email) {
        try {
            // Validar email
            ValidarDatos.validarTexto("correo electrónico", email);
            ValidarDatos.validarCorreo(email);

            // Verificar si el usuario existe
            Usuario usuario = buscarUsuarioPorCorreo(email);
            if (usuario == null) {
                view.onErrorEnvio("No existe un usuario con este correo electrónico");
                return;
            }

            // Generar código de verificación
            String codigo = generarCodigoVerificacion();

            // Guardar código y tiempo de expiración (10 minutos)
            codigosVerificacion.put(email, codigo);
            tiemposExpiracion.put(email, System.currentTimeMillis() + 10 * 60 * 1000);

            // Enviar código por correo
            emailService.enviarCorreoVerificacion(email, usuario.getNombre(), codigo,
                    new EmailService.EmailCallback() {
                        @Override
                        public void onSuccess() {
                            view.onCodigoEnviado();
                        }

                        @Override
                        public void onError(String error) {
                            // Limpiar datos si hay error
                            codigosVerificacion.remove(email);
                            tiemposExpiracion.remove(email);
                            view.onErrorEnvio("Error enviando código: " + error);
                        }
                    });

        } catch (ValidacionException e) {
            view.onErrorEnvio(e.getMessage());
        } catch (Exception e) {
            view.onErrorEnvio("Error inesperado: " + e.getMessage());
        }
    }

    public void verificarCodigo(String email, String codigoIngresado) {
        try {
            ValidarDatos.validarTexto("código de verificación", codigoIngresado);

            // Verificar si el código existe y no ha expirado
            String codigoGuardado = codigosVerificacion.get(email);
            Long tiempoExpiracion = tiemposExpiracion.get(email);

            if (codigoGuardado == null) {
                view.onErrorVerificacion("Código no encontrado. Solicita uno nuevo.");
                return;
            }

            if (tiempoExpiracion == null || System.currentTimeMillis() > tiempoExpiracion) {
                // Limpiar código expirado
                codigosVerificacion.remove(email);
                tiemposExpiracion.remove(email);
                view.onErrorVerificacion("El código ha expirado. Solicita uno nuevo.");
                return;
            }

            if (!codigoGuardado.equals(codigoIngresado)) {
                view.onErrorVerificacion("Código incorrecto. Intenta nuevamente.");
                return;
            }

            // Código verificado correctamente
            view.onCodigoVerificado();

        } catch (ValidacionException e) {
            view.onErrorVerificacion(e.getMessage());
        } catch (Exception e) {
            view.onErrorVerificacion("Error verificando código: " + e.getMessage());
        }
    }

    public void cambiarPassword(String email, String nuevaPassword) {
        try {
            ValidarDatos.validarTexto("nueva contraseña", nuevaPassword);
            ValidarDatos.validarLongitud("La nueva contraseña", nuevaPassword, 8);

            // Verificar que el código fue previamente verificado
            if (!codigosVerificacion.containsKey(email)) {
                view.onErrorCambioPassword("Debes verificar el código primero");
                return;
            }

            // Actualizar contraseña en el sistema
            boolean contrasenaActualizada = sistemaReservas.actualizarContrasena(email, nuevaPassword);

            if (contrasenaActualizada) {
                // Limpiar código después de cambio exitoso
                codigosVerificacion.remove(email);
                tiemposExpiracion.remove(email);

                view.onPasswordCambiada();
            } else {
                view.onErrorCambioPassword("Error actualizando la contraseña en el sistema");
            }

        } catch (ValidacionException e) {
            view.onErrorCambioPassword(e.getMessage());
        } catch (Exception e) {
            view.onErrorCambioPassword("Error inesperado: " + e.getMessage());
        }
    }

    private String generarCodigoVerificacion() {
        Random random = new Random();
        int codigo = 100000 + random.nextInt(900000); // Genera número entre 100000 y 999999
        return String.valueOf(codigo);
    }

    private Usuario buscarUsuarioPorCorreo(String correo) {
        for (Usuario usuario : sistemaReservas.getTodosUsuarios().values()) {
            if (usuario.getCorreo_electronico().equalsIgnoreCase(correo)) {
                return usuario;
            }
        }
        return null;
    }

    public void limpiarCodigosExpirados() {
        long ahora = System.currentTimeMillis();
        for (Map.Entry<String, Long> entry : tiemposExpiracion.entrySet()) {
            if (ahora > entry.getValue()) {
                codigosVerificacion.remove(entry.getKey());
                tiemposExpiracion.remove(entry.getKey());
            }
        }
    }
}