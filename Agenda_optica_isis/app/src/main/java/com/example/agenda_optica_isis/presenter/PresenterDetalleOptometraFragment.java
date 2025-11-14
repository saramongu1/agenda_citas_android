package com.example.agenda_optica_isis.presenter;

import com.example.agenda_optica_isis.exceptions.ValidacionException;
import com.example.agenda_optica_isis.exceptions.ValidarDatos;
import com.example.agenda_optica_isis.model.Optometra;
import com.example.agenda_optica_isis.model.SistemaReservas;
import com.example.agenda_optica_isis.view.DetalleOptometraFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PresenterDetalleOptometraFragment {
    private DetalleOptometraFragment view;
    private SistemaReservas sistemaReservas;
    private String documento_optometra;

    public PresenterDetalleOptometraFragment(DetalleOptometraFragment view) {
        this.view = view;
        this.sistemaReservas = SistemaReservas.getInstance();
    }

    public void asignarInformacionOptometra(String documentoOptometra) {
        Optometra optometra = sistemaReservas.leerOptometra(documentoOptometra);

        if (optometra != null) {
            String nombre = optometra.getNombre();
            String documento = optometra.getNumero_documento();
            String tipoDocumento = optometra.getTipo_documento();
            String genero = optometra.getGenero();
            String numeroCelular = optometra.getNumero_celular();
            String correo = optometra.getCorreo_electronico();
            String fecha_nacimiento = optometra.getFecha_nacimiento();


            view.asignarDetallesOptometra(nombre, documento, tipoDocumento, genero, numeroCelular, correo, fecha_nacimiento);
        } else {
            view.mostrarMensaje("No se encontró la información del optómetra.");
        }
    }

    public void actualizarOptometra() {
        try {
            String correo_electronico_optometra = view.getEtCorreoOptometra();
            String nombre_optometra = view.getEtNombreOptometra();
            String numero_documento_optometra = view.getEtDocumentoOptometra().trim();
            String numero_celular_optometra = view.getEtNumeroCelularOptometra();
            String fecha_nacimiento_optometra = view.getEtFechaNacimientoOptometra();
            int anio = getAnioNacimiento(fecha_nacimiento_optometra);
            int mes = getMesNacimiento(fecha_nacimiento_optometra);
            int dia = getDiaNacimiento(fecha_nacimiento_optometra);
            String tipo_documento = view.getSpnTipoDocumentoOptometra();
            String genero = view.getSpnGeneroOptometra();

            if (!correo_electronico_optometra.isEmpty()) {
                ValidarDatos.validarCorreo(correo_electronico_optometra);
            }

            ValidarDatos.validarTexto("nombre", nombre_optometra);
            ValidarDatos.validarTexto("documento", numero_documento_optometra);
            ValidarDatos.validarTexto("tipo de documento", tipo_documento);
            ValidarDatos.validarTexto("fecha de nacimiento", fecha_nacimiento_optometra);
            ValidarDatos.validarTexto("número de celular", numero_celular_optometra);
            ValidarDatos.validarLongitud("documento", numero_documento_optometra, 10);
            ValidarDatos.validarLongitud("número de celular", numero_celular_optometra, 10);
            ValidarDatos.validarLongitud("nombre", nombre_optometra, 6);

            boolean seActualizo = sistemaReservas.actualizarOptometra(
                    correo_electronico_optometra,
                    nombre_optometra,
                    numero_documento_optometra,
                    numero_celular_optometra,
                    anio,
                    mes,
                    dia,
                    tipo_documento,
                    genero
            );

            if (seActualizo) {
                view.mostrarMensaje("Se actualizó el optómetra exitosamente");
                view.irAOptometras();
            } else {
                view.mostrarMensaje("No se pudo actualizar el optómetra");
            }
        } catch (ValidacionException validacionException) {
            view.mostrarMensaje(validacionException.getMessage());
        } catch (Exception e) {
            view.mostrarMensaje(e.getMessage());
        }
    }

    public void eliminarOptometra(){
        try{
            documento_optometra = view.getEtDocumentoOptometra();
            ArrayList<Integer> citasOptometra = sistemaReservas.citasOptometra(documento_optometra);

            if(!citasOptometra.isEmpty()){
                view.mostrarDialogoPersonalizado("Eliminar optometra",
                        "El optometra tiene "+citasOptometra.size()+ " citas asignadas, desea: ",
                        retornarOpciones());
            }


        } catch (Exception e) {
            view.mostrarMensaje(e.getMessage());
            e.printStackTrace();
        }
    }

    public Map<String, Runnable> retornarOpciones(){
        Map<String, Runnable> opciones = new LinkedHashMap<>();

        opciones.put("Eliminar citas", () -> {
            eliminarCitasOptometra();
        });
        opciones.put("Cambiar optometra y eliminar", () -> {
            cambiarOptometraCitas();
        });


        return opciones;
    }

    public void eliminarCitasOptometra(){
        sistemaReservas.eliminarCitasOptometra(documento_optometra);
        boolean seElimino = sistemaReservas.eliminarOptometra(documento_optometra);
        if(seElimino){
            view.mostrarMensaje("Se eliminó el optometra exitosamente");
            view.irAOptometras();
        }else{
            view.mostrarMensaje("No se pudo eliminar el optometra");
        }
    }

    public void cambiarOptometraCitas(){
        view.irACambiarOptometra(view.getEtNombreOptometra(), view.getEtDocumentoOptometra());
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
