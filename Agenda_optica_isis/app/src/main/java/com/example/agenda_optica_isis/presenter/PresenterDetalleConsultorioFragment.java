package com.example.agenda_optica_isis.presenter;

import com.example.agenda_optica_isis.exceptions.ValidacionException;
import com.example.agenda_optica_isis.exceptions.ValidarDatos;
import com.example.agenda_optica_isis.model.Consultorio;
import com.example.agenda_optica_isis.model.SistemaReservas;
import com.example.agenda_optica_isis.view.DetalleConsultorioFragment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class PresenterDetalleConsultorioFragment {

    private  DetalleConsultorioFragment view;
    private  SistemaReservas sistemaReservas;
    private  String idConsultorio;

    public PresenterDetalleConsultorioFragment(DetalleConsultorioFragment view, String idConsultorio) {
        this.view = view;
        this.sistemaReservas = SistemaReservas.getInstance();
        this.idConsultorio = idConsultorio;
    }

    public void cargarDetalleConsultorio() {
        try {
            Consultorio consultorio = buscarConsultorioPorId(idConsultorio);
            if (consultorio == null) {
                view.mostrarMensaje("Consultorio no encontrado");
                view.irAConsultorios();
                return;
            }

            view.mostrarDetalle(
                    consultorio.getId(),
                    consultorio.getDireccion(),
                    consultorio.getCiudad()
            );

        } catch (Exception ex) {
            view.mostrarMensaje("Error al cargar detalle: " + ex.getMessage());
        }
    }

    public void guardarCambiosConsultorio() {
        try {
            String direccion = view.getDireccionConsultorio();
            String ciudad = view.getCiudadConsultorio();

            // Validar datos
            ValidarDatos.validarTexto("dirección", direccion);
            ValidarDatos.validarTexto("ciudad", ciudad);
            ValidarDatos.validarLongitudTexto("dirección", direccion, 5, 100);
            ValidarDatos.validarLongitudTexto("ciudad", ciudad, 3, 50);

            // Verificar si el consultorio existe
            Consultorio consultorioExistente = buscarConsultorioPorId(idConsultorio);
            if (consultorioExistente == null) {
                view.mostrarMensaje("Consultorio no encontrado");
                view.irAConsultorios();
                return;
            }

            // Actualizar consultorio
            boolean resultado = sistemaReservas.actualizarConsultorio(idConsultorio, direccion, ciudad);
            if (resultado) {
                view.mostrarMensaje("Consultorio actualizado exitosamente");
                view.habilitarEdicion(false);
                // Recargar los datos actualizados
                cargarDetalleConsultorio();
            } else {
                view.mostrarMensaje("Error al actualizar consultorio");
            }

        } catch (ValidacionException validacionException) {
            view.mostrarMensaje(validacionException.getMessage());
        } catch (Exception e) {
            view.mostrarMensaje("Error al actualizar consultorio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void eliminarConsultorio() {
        try {
           idConsultorio = view.getIdConsultorio();
            ArrayList<Integer> citasConsultorio = sistemaReservas.citasConsultorio(idConsultorio);

            if (!citasConsultorio.isEmpty()) {
                view.mostrarDialogoPersonalizado("Eliminar consultorio",
                        "El consultorio tiene " + citasConsultorio.size() + " citas asignadas, desea:",
                        retornarOpciones());
            }

        } catch (Exception e) {
            view.mostrarMensaje("Error al eliminar consultorio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Map<String, Runnable> retornarOpciones(){
        Map<String, Runnable> opciones = new LinkedHashMap<>();

        opciones.put("Eliminar citas", () -> {
            eliminarCitasConsultorio();
        });
        opciones.put("Cambiar consultorio asignado a las citas y eliminar", () -> {
            cambiarConsultorioCitas();
        });


        return opciones;
    }

    public void eliminarCitasConsultorio(){
        sistemaReservas.eliminarCitasConsultorio(idConsultorio);
        boolean seElimino = sistemaReservas.eliminarConsultorio(idConsultorio);
        if(seElimino){
            view.mostrarMensaje("Se eliminó el consultorio exitosamente");
            view.irAConsultorios();
        }else{
            view.mostrarMensaje("No se pudo eliminar el consultorio");
        }
    }

    public void cambiarConsultorioCitas(){
        view.irACambiarConsultorio(view.getIdConsultorio());
    }

    private Consultorio buscarConsultorioPorId(String id) {
        return sistemaReservas.leerConsultorio(id);
    }



    public void validarDatosConsultorio(String direccion, String ciudad) throws ValidacionException {
        ValidarDatos.validarTexto("dirección", direccion);
        ValidarDatos.validarTexto("ciudad", ciudad);
        ValidarDatos.validarLongitudTexto("dirección", direccion, 5, 100);
        ValidarDatos.validarLongitudTexto("ciudad", ciudad, 3, 50);

        // Validar que la dirección tenga formato adecuado
        if (!direccion.matches("^[a-zA-Z0-9\\s#\\-.,]+$")) {
            throw new ValidacionException("La dirección contiene caracteres no válidos");
        }

        // Validar que la ciudad solo contenga letras y espacios
        if (!ciudad.matches("^[a-zA-Z\\s]+$")) {
            throw new ValidacionException("La ciudad solo puede contener letras y espacios");
        }
    }
}