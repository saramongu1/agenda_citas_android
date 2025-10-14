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
        this.sistemaReservas = new SistemaReservas();
    }


    public void crearPaciente(){
        try{
            String nombrePaciente = view.getNombrePaciente();
            String documentoPaciente = view.getDocumentoPaciente();
            String tipoDocumento = view.getTipoDocumentoPaciente();
            String numeroCelularPaciente = view.getNumeroCelularPaciente();
            String fechaNacimientoPaciente = view.getFechaNacimientoPaciente();
            String correoPaciente = view.getCorreoPaciente();

            ValidarDatos.validarTexto("nombre", nombrePaciente);
            ValidarDatos.validarTexto("documento", documentoPaciente);
            ValidarDatos.validarTexto("tipo de documento", tipoDocumento);
            ValidarDatos.validarTexto("fecha de nacimiento",fechaNacimientoPaciente);
            ValidarDatos.validarTexto("Numero de celular", numeroCelularPaciente);
            ValidarDatos.validarLongitud("documento", documentoPaciente,10);
            ValidarDatos.validarLongitud("numero de celular", numeroCelularPaciente, 10);
            ValidarDatos.validarLongitud("nombre", nombrePaciente, 6);
            if(!correoPaciente.isEmpty()){
                ValidarDatos.validarCorreo(correoPaciente);
            }


               boolean seCreoPaciente = sistemaReservas.crearPaciente(correoPaciente,nombrePaciente,documentoPaciente,
                        numeroCelularPaciente,getAnioNacimiento(fechaNacimientoPaciente),
                        getMesNacimiento(fechaNacimientoPaciente), getDiaNacimiento(fechaNacimientoPaciente),
                        tipoDocumento);
            if(!seCreoPaciente){
                view.mostrarMensaje("El paciente ya existe");
            }else{
                view.mostrarMensaje("Se creo el paciente");
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
