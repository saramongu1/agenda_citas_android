package com.example.agenda_optica_isis.presenter;

import com.example.agenda_optica_isis.exceptions.ValidacionException;
import com.example.agenda_optica_isis.exceptions.ValidarDatos;
import com.example.agenda_optica_isis.model.SistemaReservas;
import com.example.agenda_optica_isis.view.AgregarConsultorioFragment;

public class PresenterAgregarConsultorioFragment {

    private final AgregarConsultorioFragment view;
    private final SistemaReservas sistemaReservas;

    public PresenterAgregarConsultorioFragment(AgregarConsultorioFragment view) {
        this.view = view;
        this.sistemaReservas = SistemaReservas.getInstance();
    }

    public void crearConsultorio() {
        try {
            String id = view.getIdConsultorio();
            String direccion = view.getDireccionConsultorio();
            String ciudad = view.getCiudadConsultorio();

            // Validar datos (similar a AgregarCitaFragment)
            ValidarDatos.validarTexto("ID del consultorio", id);
            ValidarDatos.validarTexto("dirección", direccion);
            ValidarDatos.validarTexto("ciudad", ciudad);

            // Validaciones específicas para consultorios
            ValidarDatos.validarLongitudTexto("ID del consultorio", id, 3, 20);
            ValidarDatos.validarLongitudTexto("dirección", direccion, 5, 100);
            ValidarDatos.validarLongitudTexto("ciudad", ciudad, 3, 50);

            // Validar formato del ID (solo letras, números y guiones)
            if (!id.matches("^[a-zA-Z0-9\\-]+$")) {
                throw new ValidacionException("El ID del consultorio solo puede contener letras, números y guiones");
            }

            // Validar formato de la dirección
            if (!direccion.matches("^[a-zA-Z0-9\\s#\\-.,]+$")) {
                throw new ValidacionException("La dirección contiene caracteres no válidos");
            }

            // Validar formato de la ciudad (solo letras y espacios)
            if (!ciudad.matches("^[a-zA-Z\\s]+$")) {
                throw new ValidacionException("La ciudad solo puede contener letras y espacios");
            }

            // Crear consultorio en el sistema
            boolean resultado = sistemaReservas.crearConsultorio(id, direccion, ciudad);

            if (resultado) {
                view.mostrarMensaje("Consultorio creado exitosamente");
                view.limpiarCampos();
                view.irAConsultorios();
            } else {
                view.mostrarMensaje("Error: Ya existe un consultorio con ese ID");
            }

        } catch (ValidacionException validacionException) {
            view.mostrarMensaje(validacionException.getMessage());
        } catch (Exception e) {
            view.mostrarMensaje("Error al crear consultorio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para validaciones adicionales si es necesario
    public void validarDatosConsultorio(String id, String direccion, String ciudad) throws ValidacionException {
        ValidarDatos.validarTexto("ID del consultorio", id);
        ValidarDatos.validarTexto("dirección", direccion);
        ValidarDatos.validarTexto("ciudad", ciudad);
        ValidarDatos.validarLongitudTexto("ID del consultorio", id, 3, 20);
        ValidarDatos.validarLongitudTexto("dirección", direccion, 5, 100);
        ValidarDatos.validarLongitudTexto("ciudad", ciudad, 3, 50);

        if (!id.matches("^[a-zA-Z0-9\\-]+$")) {
            throw new ValidacionException("El ID del consultorio solo puede contener letras, números y guiones");
        }

        if (!direccion.matches("^[a-zA-Z0-9\\s#\\-.,]+$")) {
            throw new ValidacionException("La dirección contiene caracteres no válidos");
        }

        if (!ciudad.matches("^[a-zA-Z\\s]+$")) {
            throw new ValidacionException("La ciudad solo puede contener letras y espacios");
        }
    }
}