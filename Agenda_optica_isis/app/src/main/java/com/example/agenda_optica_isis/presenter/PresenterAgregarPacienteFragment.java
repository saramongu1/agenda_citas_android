package com.example.agenda_optica_isis.presenter;

import com.example.agenda_optica_isis.exceptions.ValidacionException;
import com.example.agenda_optica_isis.exceptions.ValidarDatos;
import com.example.agenda_optica_isis.model.SistemaReservas;
import com.example.agenda_optica_isis.view.AgregarPacienteFragment;
import com.example.agenda_optica_isis.view.LoginActivity;

public class PresenterAgregarPacienteFragment {

    private AgregarPacienteFragment view;
    private SistemaReservas sistemaReservas;

    public PresenterAgregarPacienteFragment(AgregarPacienteFragment view) {
        this.view = view;
        this.sistemaReservas = SistemaReservas.getInstance();
    }


    public void crearPaciente(){
        try{
            String nombre_paciente = view.getNombrePaciente();
            String documento_paciente = view.getDocumentoPaciente();
            String tipo_documento = view.getTipoDocumentoPaciente();
            String genero_paciente = view.getGeneroPaciente();
            String numero_celular_paciente = view.getNumeroCelularPaciente();
            String fecha_nacimiento_paciente = view.getFechaNacimientoPaciente();
            String correo_paciente = view.getCorreoPaciente();


            ValidarDatos.validarTexto("nombre", nombre_paciente);
            ValidarDatos.validarTexto("documento", documento_paciente);
            ValidarDatos.validarTexto("tipo de documento", tipo_documento);
            ValidarDatos.validarTexto("fecha de nacimiento",fecha_nacimiento_paciente);
            ValidarDatos.validarTexto("Numero de celular", numero_celular_paciente);
            ValidarDatos.validarLongitud("documento", documento_paciente,7);
            ValidarDatos.validarLongitud("numero de celular", numero_celular_paciente, 10);
            ValidarDatos.validarLongitud("nombre", nombre_paciente, 6);

            if(!correo_paciente.isEmpty()){
                ValidarDatos.validarCorreo(correo_paciente);
            }


               boolean seCreoPaciente = sistemaReservas.crearPaciente(correo_paciente,nombre_paciente,documento_paciente,
                        numero_celular_paciente,getAnioNacimiento(fecha_nacimiento_paciente),
                        getMesNacimiento(fecha_nacimiento_paciente), getDiaNacimiento(fecha_nacimiento_paciente),
                        tipo_documento, genero_paciente);
            if(!seCreoPaciente){
                view.mostrarMensaje("El paciente ya existe");
            }else{
                view.mostrarMensaje("Se creo el paciente exitosamente");
                view.irAPacientes();

            }


        }catch (ValidacionException validacionException){
            view.mostrarMensaje(validacionException.getMessage());
        } catch (Exception e) {
            view.mostrarMensaje("Error inesperado");
        }
    }

    public int getDiaNacimiento(String fecha_nacimiento){
        String[]fechaSplit = fecha_nacimiento.split("/");
        return Integer.valueOf(fechaSplit[0]);
    }

    public int getMesNacimiento(String fecha_nacimiento){
        String[]fechaSplit = fecha_nacimiento.split("/");
        return Integer.valueOf(fechaSplit[1]);
    }

    public int getAnioNacimiento(String fecha_nacimiento){
        String[]fechaSplit = fecha_nacimiento.split("/");
        return Integer.valueOf(fechaSplit[2]);
    }
}
