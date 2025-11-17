package com.example.agenda_optica_isis.model;

import android.util.Log;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {
    private static final String TAG = "EmailService";

    // Configuración Zoho Mail (más fácil que Gmail)
    private final String SMTP_HOST = "smtp.zoho.com";
    private final String SMTP_PORT = "587";
    private final String EMAIL_FROM = "mensajes_citas_isis@zohomail.com"; // Regístrate en zoho.com/mail
    private final String EMAIL_PASSWORD = "OPTICAisis123"; // No necesita app password

    public interface EmailCallback {
        void onSuccess();
        void onError(String error);
    }

    public void enviarCorreoOptometra(String emailDestino, String nombreOptometra,
                                      String contrasena, EmailCallback callback) {
        new Thread(() -> {
            try {
                Properties properties = new Properties();
                properties.put("mail.smtp.host", SMTP_HOST);
                properties.put("mail.smtp.port", SMTP_PORT);
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", "true");
                properties.put("mail.smtp.ssl.trust", SMTP_HOST);

                // Configuraciones para mejor rendimiento
                properties.put("mail.smtp.connectiontimeout", "10000");
                properties.put("mail.smtp.timeout", "10000");

                Session session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(EMAIL_FROM, EMAIL_PASSWORD);
                    }
                });

                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(EMAIL_FROM, "Óptica ISIS"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDestino));
                message.setSubject("Bienvenido a Óptica ISIS - Credenciales de Acceso");
                message.setContent(crearContenidoCorreoHTML(nombreOptometra, contrasena), "text/html; charset=utf-8");

                Transport.send(message);

                Log.i(TAG, "Correo enviado exitosamente a: " + emailDestino);
                callback.onSuccess();

            } catch (Exception e) {
                Log.e(TAG, "Error enviando correo: " + e.getMessage());
                callback.onError("Error enviando correo: " + e.getMessage());
            }
        }).start();
    }

    private String crearContenidoCorreoHTML(String nombreOptometra, String contrasena) {
        return "<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body>" +
                "<h2>Bienvenido a Óptica ISIS</h2>" +
                "<p>Hola " + nombreOptometra + ",</p>" +
                "<p>Se ha creado tu cuenta como optómetra en nuestro sistema.</p>" +
                "<div style='background: #f8f9fa; padding: 15px; border-left: 4px solid #4CAF50; margin: 15px 0;'>" +
                "<h3>Tus credenciales de acceso:</h3>" +
                "<p><strong>Contraseña:</strong> <span style='color: #d32f2f; font-weight: bold;'>" + contrasena + "</span></p>" +
                "</div>" +
                "<p><strong>Importante:</strong> Por seguridad, cambia tu contraseña después del primer inicio de sesión.</p>" +
                "<p>Saludos cordiales,<br>Equipo Óptica ISIS</p>" +
                "</body></html>";
    }

    // En tu EmailService.java, agrega este método:
    public void enviarCorreoRecuperacion(String emailDestino, String nombreUsuario,
                                         String nuevaContrasena, EmailCallback callback) {
        new Thread(() -> {
            try {
                Properties properties = new Properties();
                properties.put("mail.smtp.host", SMTP_HOST);
                properties.put("mail.smtp.port", SMTP_PORT);
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", "true");
                properties.put("mail.smtp.ssl.trust", SMTP_HOST);

                properties.put("mail.smtp.connectiontimeout", "10000");
                properties.put("mail.smtp.timeout", "10000");

                Session session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(EMAIL_FROM, EMAIL_PASSWORD);
                    }
                });

                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(EMAIL_FROM, "Óptica ISIS"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDestino));
                message.setSubject("Recuperación de Contraseña - Óptica ISIS");
                message.setContent(crearContenidoCorreoRecuperacion(nombreUsuario, nuevaContrasena),
                        "text/html; charset=utf-8");

                Transport.send(message);

                Log.i(TAG, "Correo de recuperación enviado a: " + emailDestino);
                callback.onSuccess();

            } catch (Exception e) {
                Log.e(TAG, "Error enviando correo de recuperación: " + e.getMessage());
                callback.onError("Error enviando correo: " + e.getMessage());
            }
        }).start();
    }

    private String crearContenidoCorreoRecuperacion(String nombreUsuario, String nuevaContrasena) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }" +
                ".container { max-width: 600px; margin: 0 auto; background: white; border-radius: 10px; overflow: hidden; }" +
                ".header { background: #FF9800; color: white; padding: 20px; text-align: center; }" +
                ".content { padding: 30px; }" +
                ".credentials { background: #fff3e0; padding: 20px; border-left: 4px solid #FF9800; margin: 20px 0; }" +
                ".password { font-size: 18px; font-weight: bold; color: #d32f2f; }" +
                ".warning { background: #ffebee; padding: 15px; border-radius: 5px; margin: 15px 0; }" +
                ".footer { background: #333; color: white; padding: 15px; text-align: center; font-size: 12px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>Recuperación de Contraseña</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<h2>Hola, " + nombreUsuario + "!</h2>" +
                "<p>Has solicitado recuperar tu contraseña para el sistema de Óptica ISIS.</p>" +
                "<div class='credentials'>" +
                "<h3>Tu nueva contraseña temporal:</h3>" +
                "<p><strong>Contraseña:</strong> <span class='password'>" + nuevaContrasena + "</span></p>" +
                "</div>" +
                "<div class='warning'>" +
                "<p><strong>⚠️ Importante:</strong> Por seguridad, cambia esta contraseña temporal inmediatamente después de iniciar sesión.</p>" +
                "</div>" +
                "<p>Si no solicitaste este cambio, por favor contacta al administrador del sistema.</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>© 2024 Óptica ISIS. Todos los derechos reservados.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
    private String generarUsuario(String nombreCompleto) {
        return nombreCompleto.split(" ")[0].toLowerCase() + ".optometra";
    }
}