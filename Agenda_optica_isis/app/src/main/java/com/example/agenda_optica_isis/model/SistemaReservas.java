package com.example.agenda_optica_isis.model;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SistemaReservas {
    private HashMap<String, Optometra> optometras;
    private HashMap<String, Paciente> pacientes;
    private HashMap<Integer, Cita> citas;
    private HashMap<String, Consultorio> consultorios;
    private HashMap<String,Usuario> usuarios;

    public SistemaReservas() {
        this.optometras = new HashMap<>();
        this.pacientes = new HashMap<>();
        this.citas = new HashMap<>();
        this.consultorios = new HashMap<>();
        this.usuarios = new HashMap<>();
        quemarDatos();
    }

    public void quemarDatos(){

        crearUsuario("admin@admin.com","12345678",true,"Administradora","1057574987","3213055412",2005,7,13, "cédula de ciudadanía");
        crearOptometra("diana@optometra.com","12345678",true,"Diana yineth González Martínez","1057571987","3131234546",1990,9,13, "cédula de ciudadanía");
        crearPaciente("maria.rodriguez@email.com", "María Elena Rodríguez", "1023456789", "3101234567", 1985, 3, 15, "cédula de ciudadanía");
        crearPaciente("carlos.lopez@email.com", "Carlos Andrés López", "987654321", "3152345678", 1990, 7, 22, "cédula de ciudadanía");
        crearPaciente("ana.martinez@email.com", "Ana Isabel Martínez", "456789123", "3203456789", 1978, 11, 5, "cédula de ciudadanía");
        crearPaciente("jorge.silva@email.com", "Jorge Eduardo Silva", "789123456", "3004567890", 1988, 1, 30, "cédula de ciudadanía");
        crearPaciente("laura.diaz@email.com", "Laura Patricia Díaz", "321654987", "3015678901", 1995, 9, 12, "cédula de ciudadanía");
        crearPaciente("roberto.garcia@email.com", "Roberto Antonio García", "654987321", "3026789012", 1982, 4, 18, "cédula de ciudadanía");
        crearPaciente("sofia.perez@email.com", "Sofia Camila Pérez", "147258369", "3037890123", 2000, 8, 25, "cédula de ciudadanía");
        crearPaciente("miguel.torres@email.com", "Miguel Ángel Torres", "258369147", "3048901234", 1975, 12, 3, "cédula de ciudadanía");
        crearPaciente("elena.castro@email.com", "Elena Margarita Castro", "369147258", "3059012345", 1992, 6, 8, "cédula de ciudadanía");
        crearPaciente("fernando.ramirez@email.com", "Fernando José Ramírez", "951753852", "3120123456", 1987, 2, 14, "cédula de ciudadanía");
        crearPaciente("carmen.herrera@email.com", "Carmen Rosa Herrera", "753159486", "3131234567", 1965, 10, 17, "cédula de ciudadanía");
        crearPaciente("diego.mendoza@email.com", "Diego Alejandro Mendoza", "852741963", "3142345678", 2010, 7, 29, "Targeta de identidad");
        crearPaciente("patricia.rojas@email.com", "Patricia Alejandra Rojas", "963852741", "3173456789", 2018, 3, 21, "registro civil");
        crearPaciente("ricardo.vargas@email.com", "Ricardo Manuel Vargas", "CE741852963", "3184567890", 1970, 11, 9, "cédula de extranjería");
        crearPaciente("isabel.nunez@email.com", "Isabel Cristina Núñez", "PP159487263", "3195678901", 1993, 5, 6, "pasaporte");
        crearConsultorio("COI-01","calle 15 # 13-9", "Sogamoso");
    }


    public HashMap<String, String> buscarPaciente(String criterio, String pacienteBuscar){
        if(criterio.equals("Documento")){
            return buscarPacientePorDocumento(pacienteBuscar);
        }
        return buscarPacientePorNombre(pacienteBuscar);
    }

    public HashMap<String, String> buscarPacientePorDocumento(String numero_documento){
        HashMap<String, String> listaPacientes = new HashMap<>();
        for (Paciente paciente : pacientes.values()) {
            if(paciente.getNumero_documento().contains(numero_documento)){
                listaPacientes.put(paciente.getNumero_documento(), paciente.getNombre());
            }
        }
        return listaPacientes;
    }

    public HashMap<String, String> buscarPacientePorNombre(String nombre){
        HashMap<String, String> listaPacientes = new HashMap<>();
        for (Paciente paciente : pacientes.values()) {
            if(paciente.getNombre().toLowerCase().contains(nombre.toLowerCase())){
                listaPacientes.put(paciente.getNumero_documento(), paciente.getNombre());
            }
        }
        return listaPacientes;
    }

    public HashMap<String, String> retornatNombreDocPacientes(){
        HashMap<String, String> listaPacientes = new HashMap<>();
            for (Paciente paciente : pacientes.values()) {
                listaPacientes.put(paciente.getNumero_documento(), paciente.getNombre());
            }
            return listaPacientes;
    }

    //
     //CRUD USUARIOS
     //
    public boolean crearUsuario(String correo_electronico,String contrasenia ,boolean isAdmin,String nombre, String numero_documento,
                                String numero_celular, int anio, int mes, int dia, String tipo_contrasenia) {
        if (usuarios.containsKey(numero_documento)) return false;
        usuarios.put(numero_documento,new Usuario(correo_electronico,contrasenia,isAdmin,nombre,numero_documento,numero_celular,anio,mes,dia, tipo_contrasenia));
        return true;
    }

    public Usuario leerUsuario(String numero_documento) {
        return usuarios.get(numero_documento);
    }

    public boolean actualizarUsuario(String contrasenia ,boolean isAdmin,String nombre, String numero_documento,
                                     String numero_celular, int anio, int mes, int dia) {
        Usuario usuario = usuarios.get(numero_documento);
        LocalDate fecha_nueva = LocalDate.of(anio,mes,dia);
        if (usuario != null) {
            usuario.setNombre(nombre);
            usuario.setNumero_celular(numero_celular);
            usuario.setContrasenia(contrasenia);
            usuario.setAdmin(isAdmin);
            usuario.setFecha_nacimiento(fecha_nueva);
            return true;
        }
        return false;
    }

    public boolean eliminarUsuario(String numero_documento) {
        return usuarios.remove(numero_documento) != null;
    }


    public boolean iniciarSesion(String mail, String contrasenia){
        HashMap<String, Usuario> todosUsuarios = new HashMap<>();
        todosUsuarios.putAll(usuarios);
        todosUsuarios.putAll(optometras);

        for (Usuario usuario : todosUsuarios.values()) {
            if (usuario.getCorreo_electronico().equals(mail)) {
                if(usuario.getContrasenia().equals(contrasenia)){
                    return true;
                }
            }
        }
        return false;
    }


    // ================================
    // CRUD OPTOMETRAS
    // ================================
    public boolean crearOptometra(String correo_electronico,String contrasenia ,boolean isAdmin,String nombre, String numero_documento,
                                  String numero_celular, int anio, int mes, int dia, String tipo_contrasenia) {
        if (optometras.containsKey(numero_documento)) return false;
        optometras.put(numero_documento, new Optometra(correo_electronico, contrasenia , isAdmin, nombre,  numero_documento,numero_celular,  anio,  mes,  dia, tipo_contrasenia));
        return true;
    }

    public Optometra leerOptometra(String numero_documento) {
        return optometras.get(numero_documento);
    }

    public boolean actualizarOptometra(String nombre, String numero_documento,
                                       String numero_celular, int anio, int mes, int dia) {
        LocalDate fecha_nueva = LocalDate.of(anio, mes, dia);
        Period edad = Period.between(fecha_nueva, LocalDate.now());
        if(edad.getYears() < 18) return false;
        Optometra optometra = optometras.get(numero_documento);
        if (optometra != null) {
            optometra.setNombre(nombre);
            optometra.setNumero_celular(numero_celular);
            optometra.setFecha_nacimiento(fecha_nueva);
            return true;
        }
        return false;
    }

    public boolean eliminarOptometra(String numero_documento) {
        return optometras.remove(numero_documento) != null;
    }

    // ================================
    // CRUD PACIENTES
    // ================================
    public boolean crearPaciente(String correo_electronico,String nombre, String numero_documento,
                                 String numero_celular, int anio, int mes, int dia, String tipo_documento) {
        if (pacientes.containsKey(numero_documento)) return false;
        pacientes.put(numero_documento, new Paciente(correo_electronico,nombre, numero_documento,numero_celular, anio, mes, dia, tipo_documento));
        return true;
    }

    public Paciente leerPaciente(String numero_documento) {
        return pacientes.get(numero_documento);
    }

    public boolean existePaciente(String numero_documento){
        return pacientes.containsKey(numero_documento);
    }

    public boolean actualizarPaciente(String nombre, String numero_documento,
                                      String numero_celular, int anio, int mes, int dia) {
        Paciente paciente = pacientes.get(numero_documento);
        if (paciente != null) {
            paciente.setNombre(nombre);
            paciente.setNumero_celular(numero_celular);
            paciente.setFecha_nacimiento(LocalDate.of(anio, mes, dia));
            return true;
        }
        return false;
    }

    public boolean eliminarPaciente(String numero_documento) {
        return pacientes.remove(numero_documento) != null;
    }

    // ========================================
    // CRUD CITAS
    // ========================================
    public int crearCita(String docOptometra, String docPaciente, String idConsultorio,
                         int anio, int mes, int dia, int hora, int minutos) {
        int id = Cita.getContador();
        if (citas.containsKey(id)) return -1;

        LocalDate fecha_nueva = LocalDate.of(anio, mes, dia);
        LocalTime hora_nueva = LocalTime.of(hora, minutos);
        LocalTime hora_nueva_fin = hora_nueva.plusMinutes(20);
        for (Cita cita : citas.values()){
            if(cita.getFecha().isEqual(fecha_nueva) &&
                    cita.getDocumento_optometra().equals(docOptometra) &&
                    cita.getId_consultorio().equals(idConsultorio)){
                LocalTime inicio_actual = cita.getHora();
                LocalTime fin_actual = inicio_actual.plusMinutes(20);
                boolean seCruzan = !hora_nueva.isAfter(fin_actual) && !hora_nueva_fin.isBefore(inicio_actual);
                if(seCruzan) return -1;
            }
        }
        Cita nueva = new Cita(docOptometra, docPaciente, idConsultorio, anio, mes, dia, hora, minutos);
        citas.put(nueva.getId(), nueva);
        return id;
    }

    public Cita leerCita(int id) {
        return citas.get(id);
    }

    public boolean actualizarCita(int id, String docOptometra, String docPaciente, String idConsultorio,
                                  int anio, int mes, int dia, int hora, int minutos) {
        Cita c = citas.get(id);
        if (c == null) return false;
        // Validación simple: no permitir actualizar a una fecha pasada
        LocalDate nuevaFecha = LocalDate.of(anio, mes, dia);
        LocalTime nuevaHora = LocalTime.of(hora, minutos);
        if (nuevaFecha.isBefore(LocalDate.now()) ||
                (nuevaFecha.isEqual(LocalDate.now()) && nuevaHora.isBefore(LocalTime.now()))) {
            return false;
        }
        c.setDocumento_optometra(docOptometra);
        c.setDocumento_paciente(docPaciente);
        c.setId_consultorio(idConsultorio);
        c.setFecha(nuevaFecha);
        c.setHora(nuevaHora);
        return true;
    }

    public boolean eliminarCita(int id) {
        return citas.remove(id) != null;
    }

    // ================================
    // CRUD CONSULTORIOS
    // ================================
    public boolean crearConsultorio(String id, String direccion, String ciudad) {
        if (consultorios.containsKey(id)) return false;
        consultorios.put(id, new Consultorio(id, direccion, ciudad));
        return true;
    }

    public Consultorio leerConsultorio(String id) {
        return consultorios.get(id);
    }

    public boolean actualizarConsultorio(String id, String direccion, String ciudad) {
        Consultorio c = consultorios.get(id);
        if (c == null) return false;
        c.setDireccion(direccion);
        c.setCiudad(ciudad);
        return true;
    }

    public boolean eliminarConsultorio(String id) {
        return consultorios.remove(id) != null;
    }

    // ================================
    // CONSULTA CITAS POR CRITERIO
    // ================================
    public List<Cita> consultarCitasDia(int dia, int mes){
        List<Cita> citasDia = new ArrayList<>();
        for(Cita cita: citas.values()){
            if(cita.getFecha().getDayOfMonth() == dia && cita.getFecha().getMonthValue() == mes){
                citasDia.add(cita);
            }
        }
        return citasDia;
    }

    public List<Cita> consultarCitasSemana(int inicioSemana, int finSemana, int mes){
        List<Cita> citasSemana = new ArrayList<>();
        for(Cita cita : citas.values()){
            int diaCita = cita.getFecha().getDayOfMonth();
            if(diaCita >= inicioSemana && diaCita <= finSemana &&
                    cita.getFecha().getMonthValue() == mes){
                citasSemana.add(cita);
            }
        }
        return citasSemana;
    }

    public List<Cita> consultarCitasMes(int mes){
        List<Cita> citasMes = new ArrayList<>();
        for(Cita cita: citas.values()){
            if(cita.getFecha().getMonthValue() == mes){
                citasMes.add(cita);
            }
        }
        return citasMes;
    }
}
