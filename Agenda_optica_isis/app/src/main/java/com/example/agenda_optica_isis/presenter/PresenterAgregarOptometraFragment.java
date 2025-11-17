package com.example.agenda_optica_isis.presenter;

import com.example.agenda_optica_isis.exceptions.ValidacionException;
import com.example.agenda_optica_isis.exceptions.ValidarDatos;
import com.example.agenda_optica_isis.model.EmailService;
import com.example.agenda_optica_isis.model.SistemaReservas;
import com.example.agenda_optica_isis.view.AgregarOptometraFragment;

public class PresenterAgregarOptometraFragment {

    private AgregarOptometraFragment view;
    private SistemaReservas sistemaReservas;
    private EmailService emailService;

    public PresenterAgregarOptometraFragment(AgregarOptometraFragment view) {
        this.view = view;
        this.sistemaReservas = SistemaReservas.getInstance();
        this.emailService = new EmailService();
    }

    public void crearOptometra(){
        try{
            String nombre_optometra = view.getNombreOptometra();
            String documento_optometra = view.getDocumentoOptometra();
            String tipo_documento = view.getTipoDocumentoOptometra();
            String genero_optometra = view.getGeneroOptometra();
            String numero_celular_optometra = view.getNumeroCelularOptometra();
            String fecha_nacimiento_optometra = view.getFechaNacimientoOptometra();
            String correo_optometra = view.getCorreoOptometra();
            String contrasenia = sistemaReservas.generarContrasena();

            // Validaciones
            ValidarDatos.validarTexto("nombre", nombre_optometra);
            ValidarDatos.validarTexto("documento", documento_optometra);
            ValidarDatos.validarTexto("tipo de documento", tipo_documento);
            ValidarDatos.validarTexto("correo", correo_optometra);
            ValidarDatos.validarTexto("fecha de nacimiento", fecha_nacimiento_optometra);
            ValidarDatos.validarTexto("número de celular", numero_celular_optometra);
            ValidarDatos.validarLongitud("documento", documento_optometra, 10);
            ValidarDatos.validarLongitud("número de celular", numero_celular_optometra, 10);
            ValidarDatos.validarLongitud("nombre", nombre_optometra, 6);
            ValidarDatos.validarCorreo(correo_optometra);

            boolean seCreoOptometra = sistemaReservas.crearOptometra(
                    correo_optometra,
                    contrasenia,
                    nombre_optometra,
                    documento_optometra,
                    numero_celular_optometra,
                    getAnioNacimiento(fecha_nacimiento_optometra),
                    getMesNacimiento(fecha_nacimiento_optometra),
                    getDiaNacimiento(fecha_nacimiento_optometra),
                    tipo_documento,
                    genero_optometra
            );

            if(!seCreoOptometra){
                view.mostrarMensaje("El optómetra ya existe");
            } else {
                // Enviar correo en segundo plano
                view.mostrarMensaje("Creando optómetra...");

                emailService.enviarCorreoOptometra(correo_optometra, nombre_optometra, contrasenia,
                        new EmailService.EmailCallback() {
                            @Override
                            public void onSuccess() {
                                view.requireActivity().runOnUiThread(() -> {
                                    view.mostrarMensaje("Optómetra creado y correo enviado exitosamente");
                                    view.irAOptometras();
                                });
                            }

                            @Override
                            public void onError(String error) {
                                view.requireActivity().runOnUiThread(() -> {
                                    view.mostrarMensaje("Optómetra creado pero error enviando correo: " + error);
                                    view.irAOptometras();
                                });
                            }
                        });
            }

        } catch (ValidacionException validacionException) {
            view.mostrarMensaje(validacionException.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            view.mostrarMensaje("Error inesperado: " + e.getMessage());
        }
    }

    public int getDiaNacimiento(String fecha_nacimiento){
        String[] fechaSplit = fecha_nacimiento.split("-");
        return Integer.parseInt(fechaSplit[0]);
    }

    public int getMesNacimiento(String fecha_nacimiento){
        String[] fechaSplit = fecha_nacimiento.split("-");
        return Integer.parseInt(fechaSplit[1]);
    }

    public int getAnioNacimiento(String fecha_nacimiento){
        String[] fechaSplit = fecha_nacimiento.split("-");
        return Integer.parseInt(fechaSplit[2]);
    }
}