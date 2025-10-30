package com.example.agenda_optica_isis.view;

public class CitaUI {
        private String id;
        private String nombrePaciente;
        private String documentoPaciente;
        private String nombreOptometra;
        private String fechaHora;
        private String estado;

        public CitaUI(String id, String nombrePaciente, String documentoPaciente,
                      String nombreOptometra, String fechaHora, String estado) {
            this.id = id;
            this.nombrePaciente = nombrePaciente;
            this.documentoPaciente = documentoPaciente;
            this.nombreOptometra = nombreOptometra;
            this.fechaHora = fechaHora;
            this.estado = estado;
        }

        public String getId() { return id; }
        public String getNombrePaciente() { return nombrePaciente; }
        public String getDocumentoPaciente() { return documentoPaciente; }
        public String getNombreOptometra() { return nombreOptometra; }
        public String getFechaHora() { return fechaHora; }
        public String getEstado() { return estado; }
}
