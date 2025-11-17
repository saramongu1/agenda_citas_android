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

    public void enviarCorreoVerificacion(String emailDestino, String nombreUsuario,
                                         String codigoVerificacion, EmailCallback callback) {
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
                message.setSubject("Código de Verificación - Óptica ISIS");
                message.setContent(crearContenidoCorreoVerificacion(nombreUsuario, codigoVerificacion),
                        "text/html; charset=utf-8");

                Transport.send(message);

                Log.i(TAG, "Correo de verificación enviado a: " + emailDestino);
                callback.onSuccess();

            } catch (Exception e) {
                Log.e(TAG, "Error enviando correo de verificación: " + e.getMessage());
                callback.onError("Error enviando correo: " + e.getMessage());
            }
        }).start();
    }

    private String crearContenidoCorreoVerificacion(String nombreUsuario, String codigoVerificacion) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }" +
                ".container { max-width: 600px; margin: 0 auto; background: white; border-radius: 10px; overflow: hidden; }" +
                ".header { background: #2196F3; color: white; padding: 20px; text-align: center; }" +
                ".content { padding: 30px; }" +
                ".code { background: #e3f2fd; padding: 25px; text-align: center; margin: 20px 0; border-radius: 8px; border: 2px dashed #2196F3; }" +
                ".verification-code { font-size: 32px; font-weight: bold; color: #1976D2; letter-spacing: 8px; }" +
                ".warning { background: #fff3e0; padding: 15px; border-radius: 5px; margin: 15px 0; border-left: 4px solid #FF9800; }" +
                ".footer { background: #333; color: white; padding: 15px; text-align: center; font-size: 12px; }" +
                ".info { background: #e8f5e8; padding: 15px; border-radius: 5px; margin: 15px 0; border-left: 4px solid #4CAF50; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>Código de Verificación</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<h2>Hola, " + nombreUsuario + "!</h2>" +
                "<p>Has solicitado recuperar tu contraseña para el sistema de Óptica ISIS.</p>" +
                "<div class='info'>" +
                "<p>Utiliza el siguiente código para verificar tu identidad:</p>" +
                "</div>" +
                "<div class='code'>" +
                "<h3>Tu código de verificación:</h3>" +
                "<div class='verification-code'>" + codigoVerificacion + "</div>" +
                "</div>" +
                "<div class='warning'>" +
                "<p><strong>⚠️ Importante:</strong></p>" +
                "<ul>" +
                "<li>Este código expirará en 10 minutos</li>" +
                "<li>No compartas este código con nadie</li>" +
                "<li>Si no solicitaste este código, ignora este mensaje</li>" +
                "</ul>" +
                "</div>" +
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
    // En tu EmailService.java, agrega este método:
    public void enviarRecordatorioCita(String emailDestino, String nombrePaciente,
                                       String nombreOptometra, String fecha, String hora,
                                       String consultorio, String direccion, String ciudad,EmailCallback callback) {
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
                message.setSubject("Recordatorio de Cita - Óptica ISIS");
                message.setContent(crearContenidoRecordatorioCita(nombrePaciente, nombreOptometra, fecha, hora, consultorio, direccion, ciudad),
                        "text/html; charset=utf-8");

                Transport.send(message);

                Log.i(TAG, "Recordatorio de cita enviado a: " + emailDestino);
                callback.onSuccess();

            } catch (Exception e) {
                Log.e(TAG, "Error enviando recordatorio de cita: " + e.getMessage());
                callback.onError("Error enviando recordatorio: " + e.getMessage());
            }
        }).start();
    }

    private String crearContenidoRecordatorioCita(String nombrePaciente, String nombreOptometra,
                                                  String fecha, String hora, String consultorio, String direccion, String ciudad) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }" +
                ".container { max-width: 600px; margin: 0 auto; background: white; border-radius: 10px; overflow: hidden; }" +
                ".header { background: #2196F3; color: white; padding: 20px; text-align: center; }" +
                ".content { padding: 30px; }" +
                ".appointment-info { background: #e3f2fd; padding: 20px; border-radius: 8px; margin: 20px 0; border-left: 4px solid #2196F3; }" +
                ".info-item { margin-bottom: 10px; }" +
                ".info-label { font-weight: bold; color: #1976D2; }" +
                ".reminder { background: #fff3e0; padding: 15px; border-radius: 5px; margin: 15px 0; border-left: 4px solid #FF9800; }" +
                ".footer { background: #333; color: white; padding: 15px; text-align: center; font-size: 12px; }" +
                ".logo { text-align: center; margin-bottom: 20px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>Recordatorio de Cita</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<div class='logo'>" +
                "<h2 style='color: #2196F3; margin: 0;'>Óptica ISIS</h2>" +
                "<p style='color: #666; margin: 5px 0;'>Cuidando tu visión</p>" +
                "</div>" +
                "<h2>Hola, " + nombrePaciente + "!</h2>" +
                "<p>Te recordamos que tienes una cita programada con nosotros.</p>" +
                "<div class='appointment-info'>" +
                "<h3>Detalles de tu cita:</h3>" +
                "<div class='info-item'><span class='info-label'>Fecha:</span> " + fecha + "</div>" +
                "<div class='info-item'><span class='info-label'>Hora:</span> " + hora + "</div>" +
                "<div class='info-item'><span class='info-label'>Optómetra:</span> " + nombreOptometra + "</div>" +
                "<div class='info-item'><span class='info-label'>Consultorio:</span> " + consultorio + "</div>" +
                "</div>" +
                "<div class='reminder'>" +
                "<p><strong>📋 Recordatorio importante:</strong></p>" +
                "<ul>" +
                "<li>Llega 10 minutos antes de tu cita</li>" +
                "<li>Trae tus lentes actuales si los usas</li>" +
                "<li>Trae cualquier documento médico relevante</li>" +
                "<li>Si no puedes asistir, cancela con 24 horas de anticipación</li>" +
                "</ul>" +
                "</div>" +
                "<p><strong>📍 Dirección:</strong><br>" + direccion + ", " + ciudad + "</p>" +
                "<p><strong>📞 Teléfono:</strong> 3229045891</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>© 2024 Óptica ISIS. Todos los derechos reservados.</p>" +
                "<p>Este es un mensaje automático, por favor no respondas a este correo.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}