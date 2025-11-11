package com.example.agenda_optica_isis.presenter;

import com.example.agenda_optica_isis.exceptions.ValidacionException;
import com.example.agenda_optica_isis.exceptions.ValidarDatos;
import com.example.agenda_optica_isis.model.Consultorio;
import com.example.agenda_optica_isis.model.SistemaReservas;
import com.example.agenda_optica_isis.view.DetalleConsultorioFragment;

public class PresenterDetalleConsultorioFragment {

    private final DetalleConsultorioFragment view;
    private final SistemaReservas sistemaReservas;
    private final String idConsultorio;

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
            // Verificar si el consultorio existe
            Consultorio consultorio = buscarConsultorioPorId(idConsultorio);
            if (consultorio == null) {
                view.mostrarMensaje("Consultorio no encontrado");
                view.irAConsultorios();
                return;
            }

            // Verificar si el consultorio tiene citas programadas
            if (tieneCitasProgramadas(idConsultorio)) {
                view.mostrarMensaje("No se puede eliminar el consultorio porque tiene citas programadas");
                return;
            }

            // Eliminar consultorio
            boolean resultado = sistemaReservas.eliminarConsultorio(idConsultorio);
            if (resultado) {
                view.mostrarMensaje("Consultorio eliminado exitosamente");
                view.irAConsultorios();
            } else {
                view.mostrarMensaje("Error al eliminar consultorio");
            }

        } catch (Exception e) {
            view.mostrarMensaje("Error al eliminar consultorio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Consultorio buscarConsultorioPorId(String id) {
        return sistemaReservas.leerConsultorio(id);
    }

    private boolean tieneCitasProgramadas(String idConsultorio) {
        // Aquí necesitarías un método en SistemaReservas para verificar si el consultorio tiene citas
        // Por ahora, asumimos que no hay citas para permitir la eliminación
        // Puedes implementar esta lógica más adelante
        return false;
    }

    // Método auxiliar para validaciones específicas de consultorios
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