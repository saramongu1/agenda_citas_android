package com.example.agenda_optica_isis.presenter;

import android.util.Log;

import com.example.agenda_optica_isis.exceptions.ValidacionException;
import com.example.agenda_optica_isis.exceptions.ValidarDatos;
import com.example.agenda_optica_isis.model.Consultorio;
import com.example.agenda_optica_isis.model.EmailService;
import com.example.agenda_optica_isis.model.Paciente;
import com.example.agenda_optica_isis.model.SistemaReservas;
import com.example.agenda_optica_isis.utils.ModernSnackBar;
import com.example.agenda_optica_isis.view.AgregarCitaFragment;

public class PresenterAgregarCitaFragment {
    private AgregarCitaFragment view;
    private SistemaReservas sistemaReservas;
    private EmailService emailService;

    public PresenterAgregarCitaFragment(AgregarCitaFragment view) {
        this.view = view;
        this.sistemaReservas = SistemaReservas.getInstance();
        this.emailService = new EmailService();
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

            int anio = Integer.parseInt(fecha[0]);
            int mes = Integer.parseInt(fecha[1]);
            int dia = Integer.parseInt(fecha[2]);
            int hora = Integer.parseInt(hora_completa[0]);
            int minutos = Integer.parseInt(hora_completa[1]);

            ValidarDatos.validarFechaCita(anio,mes,dia,hora,minutos);

            boolean consultorioLibre = sistemaReservas.comprobarHorarioConsultorio(-1,consultorio,anio,mes,dia,hora,minutos);
            boolean optometraLibre = sistemaReservas.comprobarHorarioOptometra(-1,documento_optometra,anio,mes,dia,hora,minutos);
            boolean pacienteSeleccionado = !documento_paciente.contains("paciente") &&
                    !documento_paciente.isEmpty() &&
                    !documento_paciente.equals("Documento del paciente");

            // Validaciones individuales con mensajes específicos
            if(!pacienteSeleccionado){
                view.mostrarMensajeSnackBar("Seleccione un paciente", ModernSnackBar.INFO);
                return;
            }

            if(!consultorioLibre){
                view.mostrarMensajeSnackBar("El consultorio no está disponible en esa hora", ModernSnackBar.ERROR);
                return;
            }

            if (!optometraLibre){
                view.mostrarMensajeSnackBar("Optómetra no disponible en esa hora", ModernSnackBar.ERROR);
                return;
            }

            // Si todas las validaciones pasan, crear la cita
            if(consultorioLibre && optometraLibre && pacienteSeleccionado){
                sistemaReservas.crearCita(documento_optometra,documento_paciente,consultorio,anio,mes,dia,hora,minutos);
                enviarRecordatorioCita(documento_paciente, view.getOptometra(), consultorio,
                        view.getFechaCita(), view.getHoraCita());
                view.limpiarCamposCita();
                view.mostrarMensajeSnackBar("Cita agendada correctamente", ModernSnackBar.SUCCESS);
                view.irACalendario();
            }

        } catch (ValidacionException e) {
            view.mostrarMensajeSnackBar(e.getMessage(), ModernSnackBar.ERROR);
        } catch (Exception e) {
            view.mostrarMensajeSnackBar("Error inesperado: " + e.getMessage(), ModernSnackBar.ERROR);
            Log.e("AgregarCita", "Error al agregar cita", e);
        }
    }

    private void enviarRecordatorioCita(String documentoPaciente, String nombreOptometra,
                                        String consultorio, String fecha, String hora) {
        try {
            // Obtener información del paciente
            Paciente paciente = sistemaReservas.buscarPacientePorDocumentono(documentoPaciente);
            if (paciente == null || paciente.getCorreo_electronico() == null) {
                Log.w("Recordatorio", "No se pudo enviar recordatorio: paciente no encontrado o sin email");
                return;
            }

            String emailPaciente = paciente.getCorreo_electronico();
            String nombrePaciente = paciente.getNombre();
            String fechaFormateada = formatearFechaParaDisplay(fecha);
            Consultorio consultorioObjeto = sistemaReservas.leerConsultorio(consultorio);

            emailService.enviarRecordatorioCita(emailPaciente, nombrePaciente, nombreOptometra,
                    fechaFormateada, hora, consultorio,consultorioObjeto.getDireccion(),consultorioObjeto.getCiudad(),
                    new EmailService.EmailCallback() {
                        @Override
                        public void onSuccess() {
                            Log.i("Recordatorio", "Recordatorio enviado exitosamente a: " + nombrePaciente);
                        }

                        @Override
                        public void onError(String error) {
                            Log.e("Recordatorio", "Error enviando recordatorio: " + error);
                        }
                    });

        } catch (Exception e) {
            Log.e("Recordatorio", "Error en el proceso de recordatorio: " + e.getMessage());
        }
    }

    private String formatearFechaParaDisplay(String fecha) {
        try {
            String[] partes = fecha.split("-");
            if (partes.length == 3) {
                return partes[2] + "/" + partes[1] + "/" + partes[0]; // DD/MM/AAAA
            }
        } catch (Exception e) {
            Log.e("FormateoFecha", "Error formateando fecha: " + e.getMessage());
        }
        return fecha;
    }
}