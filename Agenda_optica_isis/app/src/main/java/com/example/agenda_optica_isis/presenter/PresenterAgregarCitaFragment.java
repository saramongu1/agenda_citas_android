package com.example.agenda_optica_isis.presenter;

import com.example.agenda_optica_isis.exceptions.ValidacionException;
import com.example.agenda_optica_isis.exceptions.ValidarDatos;
import com.example.agenda_optica_isis.model.SistemaReservas;
import com.example.agenda_optica_isis.utils.ModernSnackBar;
import com.example.agenda_optica_isis.view.AgregarCitaFragment;

public class PresenterAgregarCitaFragment {
    private AgregarCitaFragment view;
    private SistemaReservas sistemaReservas;

    public PresenterAgregarCitaFragment(AgregarCitaFragment view) {
        this.view = view;
        this.sistemaReservas = SistemaReservas.getInstance();
    }

    public String[] listaOptometras(){
        return sistemaReservas.obtenerNombresOptometras();
    }

    public String[] listaConsultorios(){
        return sistemaReservas.obtenerIdsConsultorios();
    }

    public void agregarCita(){
        try {
        String documento_optometra = sistemaReservas.getDocumentoOptometra(view.getOptometra());
        String documento_paciente = view.getDocumentoPaciente();
        String consultorio = view.getConsultorio();
        String []fecha = view.getFechaCita().split("-");
        String []hora_completa = view.getHoraCita().split(":");



        ValidarDatos.validarTexto("fecha", view.getFechaCita());
        ValidarDatos.validarTexto("hora", view.getHoraCita());
        boolean pacienteSeleccionado = !documento_paciente.contains("paciente");

            int anio = Integer.parseInt(fecha[0]);
            int mes = Integer.parseInt(fecha[1]);
            int dia = Integer.parseInt(fecha[2]);
            int hora = Integer.parseInt(hora_completa[0]);
            int minutos = Integer.parseInt(hora_completa[1]);
            boolean consultorioLibre = sistemaReservas.comprobarHorarioConsultorio(-1,consultorio,anio,mes,dia,hora,minutos);
            boolean optometraLibre = sistemaReservas.comprobarHorarioOptometra(-1,documento_optometra,anio,mes,dia,hora,minutos);

            if(!consultorioLibre){
                view.mostrarMensajeSnackBar("El consultorio no está disponible en esa hora", ModernSnackBar.ERROR);
            }if (!optometraLibre){
                view.mostrarMensajeSnackBar("Optómetra no disponible en esa hora", ModernSnackBar.ERROR);
            }if(!pacienteSeleccionado){
                view.mostrarMensajeSnackBar("Seleccione un paciente", ModernSnackBar.INFO);
            }

            if(consultorioLibre && optometraLibre && pacienteSeleccionado){
                sistemaReservas.crearCita(documento_optometra,documento_paciente,consultorio,anio,mes,dia,hora,minutos);
                view.mostrarMensajeSnackBar("Cita agendada correctamente", ModernSnackBar.SUCCESS);
                view.irACalendario();
            }else{
                view.mostrarMensajeSnackBar("No se pudo agendar la cita",ModernSnackBar.ERROR);
            }

        } catch (ValidacionException e) {
            view.mostrarMensajeSnackBar(e.getMessage(), ModernSnackBar.ERROR);
        } catch (Exception e) {
            view.mostrarMensajeSnackBar(e.getMessage(), ModernSnackBar.ERROR);
        }
    }



}
