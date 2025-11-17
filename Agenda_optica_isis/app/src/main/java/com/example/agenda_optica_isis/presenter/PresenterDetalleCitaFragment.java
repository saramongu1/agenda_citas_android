package com.example.agenda_optica_isis.presenter;

import com.example.agenda_optica_isis.exceptions.ValidacionException;
import com.example.agenda_optica_isis.exceptions.ValidarDatos;
import com.example.agenda_optica_isis.model.Cita;
import com.example.agenda_optica_isis.model.SistemaReservas;
import com.example.agenda_optica_isis.view.DetalleCitaFragment;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class PresenterDetalleCitaFragment {

    private final DetalleCitaFragment view;
    private final SistemaReservas sistemaReservas;

    public PresenterDetalleCitaFragment(DetalleCitaFragment view) {
        this.view = view;
        this.sistemaReservas = SistemaReservas.getInstance();
    }

    public void cargarDetalleCita(String idArg) {
        try {
            int id;
            try {
                id = Integer.parseInt(idArg);
            } catch (NumberFormatException e) {
                view.mostrarMensaje("Id de cita inválido: " + idArg);
                return;
            }
            Cita encontrada = buscarCitaPorId(id);
            if (encontrada == null) {
                view.mostrarMensaje("Cita no encontrada");
                return;
            }



            String idStr = String.valueOf(encontrada.getId());
            String nombrePaciente = sistemaReservas.getNombrePaciente(encontrada.getDocumento_paciente());
            String documentoPaciente = encontrada.getDocumento_paciente();
            String nombreOptometra = sistemaReservas.getNombreOptometra(encontrada.getDocumento_optometra());
            String fecha = encontrada.getFecha();
            String hora = encontrada.getHora();
            String estado = encontrada.getEstadoCita();
            int posicionOptometra = getPosicionNombreOptometra(nombreOptometra);
            int posicionConsultorio = getPosicionConsultorio(encontrada.getId_consultorio());

            view.mostrarDetalle(idStr, nombrePaciente, documentoPaciente, posicionOptometra, fecha, hora, estado, posicionConsultorio);

        } catch (Exception ex) {
            view.mostrarMensaje("Error al cargar detalle: " + ex.getMessage());
        }
    }


    public void guardarCambiosEditarCita(){
        try {
        int idCita = Integer.parseInt(view.getIdCita());
        String fecha = view.getFechaCita();
        String hora_completa = view.getHoraCita();
        String documento_optometra = sistemaReservas.getDocumentoOptometra(view.getOptometra());
        String consultorio = view.getConsultorio();
        String []arreglo_fecha  = fecha.split("-");
        String []arreglo_hora = hora_completa.split(":");

        ValidarDatos.validarTexto("fecha", fecha);
        ValidarDatos.validarTexto("hora", hora_completa);


        int dia = Integer.parseInt(arreglo_fecha[0]);
        int mes = Integer.parseInt(arreglo_fecha[1]);
        int anio = Integer.parseInt(arreglo_fecha[2]);
        int hora = Integer.parseInt(arreglo_hora[0]);
        int minutos = Integer.parseInt(arreglo_hora[1]);

        ValidarDatos.validarFechaCita(anio,mes,dia,hora,minutos);



        boolean consultorio_libre = sistemaReservas.comprobarHorarioConsultorio(idCita,consultorio,anio,mes,dia,hora,minutos);
        boolean optometra_libre = sistemaReservas.comprobarHorarioOptometra(idCita,documento_optometra, anio, mes,dia,hora,minutos);

        if(!consultorio_libre){
            view.mostrarMensaje("El consultorio no está disponible en esa hora");
        }if (!optometra_libre){
            view.mostrarMensaje("Optómetra no disponible en esa hora");
            }

        if(!consultorio_libre || !optometra_libre){
            view.mostrarMensaje("No se pudo actualizar la cita");
        }else{
            sistemaReservas.actualizarCita(idCita,anio,mes,dia,hora,minutos,documento_optometra, consultorio);
            view.mostrarMensaje("Se acualizó la cita exitosamente");
            view.irAAgendaCitas();
        }

        } catch (ValidacionException validacionException) {
            view.mostrarMensaje(validacionException.getMessage());
        }catch (Exception e){
            view.mostrarMensaje(e.getMessage());
            e.printStackTrace();
        }
    }

    public void cancelarCita(){
        int id_cita = Integer.parseInt(view.getIdCita());
        sistemaReservas.eliminarCita(id_cita);
        view.mostrarMensaje("Cita cancelada exitosamente");
        view.irAAgendaCitas();

    }


    public int getPosicionConsultorio(String id_consultorio){
        int posicion = 0;
        String []listaConsultorios = sistemaReservas.obtenerNombresOptometras();
        for (int i = 0; i < listaConsultorios.length; i++){
            if(id_consultorio.equalsIgnoreCase(listaConsultorios[i])){
                posicion = i;
            }
        }
        return posicion;
    }
    public int getPosicionNombreOptometra(String nombre_optometra){
        int posicion = 0;
        String []listaOptometras = sistemaReservas.obtenerNombresOptometras();
        for (int i = 0; i < listaOptometras.length; i++){
            if(nombre_optometra.equalsIgnoreCase(listaOptometras[i])){
                posicion = i;
            }
        }
        return posicion;
    }

    private Cita buscarCitaPorId(int id) {
        List<Cita> citas = sistemaReservas.consultarCitas();
        for (Cita c : citas) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    public String[] listaOptometras(){
        return sistemaReservas.obtenerNombresOptometras();
    }

    public String[] listaConsultorios(){
        return sistemaReservas.obtenerIdsConsultorios();
    }
}
