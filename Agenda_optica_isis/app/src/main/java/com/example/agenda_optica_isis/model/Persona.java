package com.example.agenda_optica_isis.model;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Persona {
    private String nombre;
    private String numero_documento;
    private String numero_celular;
    private LocalDate fecha_nacimiento;
    private TipoDocumento tipo_documento;
    private String correo_electronico;
    private Genero genero;

    public Persona() {
        nombre = "";
        numero_documento = "";
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumero_documento() {
        return numero_documento;
    }

    public void setNumero_documento(String numero_documento) {
        this.numero_documento = numero_documento;
    }

    public String getNumero_celular() {
        return numero_celular;
    }

    public void setNumero_celular(String numero_celular) {
        this.numero_celular = numero_celular;
    }

    public String getFecha_nacimiento() {
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return fecha_nacimiento.format(formateador);
    }

    public LocalDate getFecha_nacimientoFormat() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(LocalDate fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getTipo_documento() {
        return tipo_documento.toString();
    }

    public void setTipo_documento(TipoDocumento tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    public String getCorreo_electronico() {
        return correo_electronico;
    }

    public void setCorreo_electronico(String correo_electronico) {
        this.correo_electronico = correo_electronico;
    }

    public String getGenero() {
        return genero.toString();
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public void asignarTipoDocumento(String input_tipo_documento){
        if (input_tipo_documento.equalsIgnoreCase("R.C.")){
            setTipo_documento(TipoDocumento.RC);
        }else if (input_tipo_documento.equalsIgnoreCase("T.I.")){
            setTipo_documento(TipoDocumento.TI);
        }else if (input_tipo_documento.equalsIgnoreCase("C.C.")){
            setTipo_documento(TipoDocumento.CC);
        }else if (input_tipo_documento.equalsIgnoreCase("C.E.")){
            setTipo_documento(TipoDocumento.CE);
        }else if (input_tipo_documento.equalsIgnoreCase("PAS")){
            setTipo_documento(TipoDocumento.PP);
        }
    }
    
    public void asignarGenero(String inputGenero){
        if(inputGenero.equalsIgnoreCase("masculino")){
            setGenero(Genero.MASCULINO);
        } else if (inputGenero.equalsIgnoreCase("femenino")) {
            setGenero(Genero.FEMENINO);
        } else if (inputGenero.equalsIgnoreCase("otro")) {
            setGenero(Genero.OTRO);
        }
    }

}
