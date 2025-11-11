package com.example.agenda_optica_isis.presenter;

import com.example.agenda_optica_isis.model.Optometra;
import com.example.agenda_optica_isis.model.SistemaReservas;
import com.example.agenda_optica_isis.view.DetalleOptometraFragment;

public class PresenterDetalleOptometraFragment {
    private DetalleOptometraFragment view;
    private SistemaReservas sistemaReservas;

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
}
