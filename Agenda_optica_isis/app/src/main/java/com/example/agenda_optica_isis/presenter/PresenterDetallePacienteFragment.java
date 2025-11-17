package com.example.agenda_optica_isis.presenter;

import com.example.agenda_optica_isis.exceptions.ValidacionException;
import com.example.agenda_optica_isis.exceptions.ValidarDatos;
import com.example.agenda_optica_isis.model.Paciente;
import com.example.agenda_optica_isis.model.SistemaReservas;
import com.example.agenda_optica_isis.view.DetallePacienteFragment;

public class PresenterDetallePacienteFragment {
    private DetallePacienteFragment view;
    private SistemaReservas sistemaReservas;

    public PresenterDetallePacienteFragment(DetallePacienteFragment view){
        this.view = view;
        this.sistemaReservas = SistemaReservas.getInstance();
    }

    public void AsignarInformacionPaciente(String input_documento_paciente){

        Paciente paciente = sistemaReservas.leerPaciente(input_documento_paciente);


        String nombre_completo_paciente = paciente.getNombre();
        String documento_paciente = paciente.getNumero_documento();
        String tipo_documento = paciente.getTipo_documento();
        String genero = paciente.getGenero();
        String numero_celular_paciente = paciente.getNumero_celular();
        String correo_electronico_paciente = paciente.getCorreo_electronico();
        String fecha_nacimiento = paciente.getFecha_nacimiento();

        view.asignarDetallesPaciente(nombre_completo_paciente, documento_paciente, tipo_documento, genero, numero_celular_paciente,
                                    correo_electronico_paciente, fecha_nacimiento);

    }

    public void actualizarPaciente(){
        try {
        String correo_electronico_paciente = view.getEtCorreoPaciente();
        String nombre_paciente = view.getEtNombrePaciente();
        String numero_documento_paciente = view.getEtDocumentoPaciente().trim();
        String numero_celular_paciente  = view.getEtNumeroCelularPaciente();
        String fecha_nacimiento_paciente = view.getEtFechaNacimientoPaciente();
        int anio = getAnioNacimiento(fecha_nacimiento_paciente);
        int mes = getMesNacimiento(fecha_nacimiento_paciente);
        int dia = getDiaNacimiento(fecha_nacimiento_paciente);
        String tipo_documento = view.getSpnTipoDocumentoPaciente();
        String genero = view.getSpnGeneroPaciente();

            if(!correo_electronico_paciente.isEmpty()){
                ValidarDatos.validarCorreo(correo_electronico_paciente);
            }

            ValidarDatos.validarTexto("nombre", nombre_paciente);
            ValidarDatos.validarTexto("documento", numero_documento_paciente);
            ValidarDatos.validarTexto("tipo de documento", tipo_documento);
            ValidarDatos.validarTexto("fecha de nacimiento",fecha_nacimiento_paciente);
            ValidarDatos.validarTexto("Numero de celular", numero_celular_paciente);
            ValidarDatos.validarLongitud("documento", numero_documento_paciente,10);
            ValidarDatos.validarLongitud("numero de celular", numero_celular_paciente, 10);
            ValidarDatos.validarLongitud("nombre", nombre_paciente, 6);

       boolean seActualizo = sistemaReservas.actualizarPaciente(correo_electronico_paciente,nombre_paciente,numero_documento_paciente,numero_celular_paciente,anio,mes,dia,tipo_documento,genero);
        if(seActualizo){
            view.mostrarMensaje("Se actualizó el paciente exitosamente");
            view.irAPacientes();
        }else{
            view.mostrarMensaje("No se pudo actualizar el paciente");
        }
        } catch (ValidacionException validacionException) {
            view.mostrarMensaje(validacionException.getMessage());
        } catch (Exception e) {
            view.mostrarMensaje(e.getMessage());
        }


    }

    public void eliminarPaciente(){
        boolean seElimino = sistemaReservas.eliminarPaciente(view.getEtDocumentoPaciente());
        if(seElimino){
            view.mostrarMensaje("Se eliminó el paciente exitosamente");
            view.irAPacientes();
        }else{
            view.mostrarMensaje("No se pudo eliminar el paciente");
        }
    }

    public int getDiaNacimiento(String fecha_nacimiento){
        String[]fechaSplit = fecha_nacimiento.split("-");
        return Integer.valueOf(fechaSplit[0]);
    }

    public int getMesNacimiento(String fecha_nacimiento){
        String[]fechaSplit = fecha_nacimiento.split("-");
        return Integer.valueOf(fechaSplit[1]);
    }

    public int getAnioNacimiento(String fecha_nacimiento){
        String[]fechaSplit = fecha_nacimiento.split("-");
        return Integer.valueOf(fechaSplit[2]);
    }


}
