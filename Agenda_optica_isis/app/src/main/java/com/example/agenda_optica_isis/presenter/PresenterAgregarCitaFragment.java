package com.example.agenda_optica_isis.presenter;

import android.util.Log;

import com.example.agenda_optica_isis.exceptions.ValidacionException;
import com.example.agenda_optica_isis.exceptions.ValidarDatos;
import com.example.agenda_optica_isis.model.Consultorio;
import com.example.agenda_optica_isis.model.EmailService;
import com.example.agenda_optica_isis.model.Paciente;
import com.example.agenda_optica_isis.model.SistemaReservas;
import com.example.agenda_optica_isis.model.Usuario;
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
            boolean pacienteSeleccionado = !documento_paciente.contains("paciente");

            int anio = Integer.parseInt(fecha[0]);
            int mes = Integer.parseInt(fecha[1]);
            int dia = Integer.parseInt(fecha[2]);
            int hora = Integer.parseInt(hora_completa[0]);
            int minutos = Integer.parseInt(hora_completa[1]);

            ValidarDatos.validarFechaCita(anio,mes,dia,hora,minutos);
            boolean consultorioLibre = sistemaReservas.comprobarHorarioConsultorio(-1,consultorio,anio,mes,dia,hora,minutos);
            boolean optometraLibre = sistemaReservas.comprobarHorarioOptometra(-1,documento_optometra,anio,mes,dia,hora,minutos);

            if(!consultorioLibre){
                view.mostrarMensajeSnackBar("El consultorio no está disponible en esa hora", ModernSnackBar.ERROR);
            }
            if (!optometraLibre){
                view.mostrarMensajeSnackBar("Optómetra no disponible en esa hora", ModernSnackBar.ERROR);
            }
            if(!pacienteSeleccionado){
                view.mostrarMensajeSnackBar("Seleccione un paciente", ModernSnackBar.INFO);
            }

            if(consultorioLibre && optometraLibre && pacienteSeleccionado){
                // Crear la cita
                sistemaReservas.crearCita(documento_optometra,documento_paciente,consultorio,anio,mes,dia,hora,minutos);

                // Enviar recordatorio por correo
                enviarRecordatorioCita(documento_paciente, view.getOptometra(), consultorio,
                        view.getFechaCita(), view.getHoraCita());

                view.mostrarMensajeSnackBar("Cita agendada correctamente", ModernSnackBar.SUCCESS);
                view.irACalendario();
            } else {
                view.mostrarMensajeSnackBar("No se pudo agendar la cita", ModernSnackBar.ERROR);
            }

        } catch (ValidacionException e) {
            view.mostrarMensajeSnackBar(e.getMessage(), ModernSnackBar.ERROR);
        } catch (Exception e) {
            view.mostrarMensajeSnackBar(e.getMessage(), ModernSnackBar.ERROR);
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
                            // No mostrar error al usuario para no interrumpir el flujo
                        }
                    });

        } catch (Exception e) {
            Log.e("Recordatorio", "Error en el proceso de recordatorio: " + e.getMessage());
            // No mostrar error al usuario para no interrumpir el flujo
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
        return fecha; // Devolver original si hay error
    }

    // Método adicional para enviar recordatorios programados (opcional)
    public void enviarRecordatoriosProgramados() {
        // Este método podría ser llamado por un servicio programado
        // para enviar recordatorios 24 horas antes de las citas
        // Implementación opcional para recordatorios automáticos
    }
}