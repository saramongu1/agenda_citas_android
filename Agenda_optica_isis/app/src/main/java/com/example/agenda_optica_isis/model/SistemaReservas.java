package com.example.agenda_optica_isis.model;
import com.example.agenda_optica_isis.repository.FirebaseRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class SistemaReservas {
    private HashMap<String, Optometra> optometras;
    private HashMap<String, Paciente> pacientes;
    private HashMap<Integer, Cita> citas;
    private HashMap<String, Consultorio> consultorios;
    private HashMap<String,Usuario> usuarios;
    private static SistemaReservas instancia;
    private Usuario usuarioActual;
    private FirebaseRepository firebaseRepository;
    private boolean datosCargados = false;

    // Constructor privado
    private SistemaReservas() {
        this.optometras = new HashMap<>();
        this.pacientes = new HashMap<>();
        this.citas = new HashMap<>();
        this.consultorios = new HashMap<>();
        this.usuarios = new HashMap<>();
        this.firebaseRepository = new FirebaseRepository();

        // Cargar datos desde Firebase
        cargarDatosDesdeFirebase();
    }

    public static synchronized SistemaReservas getInstance() {
        if (instancia == null) {
            instancia = new SistemaReservas();
        }
        return instancia;
    }

    // NUEVO MÉTODO: Cargar datos desde Firebase
    private void cargarDatosDesdeFirebase() {
        firebaseRepository.cargarTodosLosDatos(new FirebaseRepository.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(DataSnapshot dataSnapshot) {
                // Limpiar datos existentes
                usuarios.clear();
                optometras.clear();
                pacientes.clear();
                citas.clear();
                consultorios.clear();

                // Cargar usuarios
                if (dataSnapshot.child("usuarios").exists()) {
                    for (DataSnapshot usuarioSnapshot : dataSnapshot.child("usuarios").getChildren()) {
                        Usuario usuario = usuarioSnapshot.getValue(Usuario.class);
                        if (usuario != null) {
                            usuarios.put(usuario.getNumero_documento(), usuario);
                        }
                    }
                }

                // Cargar optometras
                if (dataSnapshot.child("optometras").exists()) {
                    for (DataSnapshot optometraSnapshot : dataSnapshot.child("optometras").getChildren()) {
                        Optometra optometra = optometraSnapshot.getValue(Optometra.class);
                        if (optometra != null) {
                            optometras.put(optometra.getNumero_documento(), optometra);
                            // También agregar a usuarios
                            usuarios.put(optometra.getNumero_documento(), optometra);
                        }
                    }
                }

                // Cargar pacientes
                if (dataSnapshot.child("pacientes").exists()) {
                    for (DataSnapshot pacienteSnapshot : dataSnapshot.child("pacientes").getChildren()) {
                        Paciente paciente = pacienteSnapshot.getValue(Paciente.class);
                        if (paciente != null) {
                            pacientes.put(paciente.getNumero_documento(), paciente);
                        }
                    }
                }

                // Cargar citas
                if (dataSnapshot.child("citas").exists()) {
                    for (DataSnapshot citaSnapshot : dataSnapshot.child("citas").getChildren()) {
                        Cita cita = citaSnapshot.getValue(Cita.class);
                        if (cita != null) {
                            citas.put(cita.getId(), cita);
                            // Actualizar contador si es necesario
                            if (cita.getId() >= Cita.getContador()) {
                                Cita.setContador(cita.getId() + 1);
                            }
                        }
                    }
                }

                // Cargar consultorios
                if (dataSnapshot.child("consultorios").exists()) {
                    for (DataSnapshot consultorioSnapshot : dataSnapshot.child("consultorios").getChildren()) {
                        Consultorio consultorio = consultorioSnapshot.getValue(Consultorio.class);
                        if (consultorio != null) {
                            consultorios.put(consultorio.getId(), consultorio);
                        }
                    }
                }

                datosCargados = true;
                System.out.println("Datos cargados correctamente desde Firebase");

                // Si no hay datos, quemar datos iniciales
                if (usuarios.isEmpty()) {
                    quemarDatos();
                }
            }

            @Override
            public void onError(String error) {
                System.out.println("Error cargando datos desde Firebase: " + error);
                // En caso de error, usar datos locales
                quemarDatos();
            }
        });
    }

    public void quemarDatos(){
        // Solo crear admin si no existe
        if (usuarios.isEmpty()) {
            crearUsuario("admin@admin.com","12345678",true,"Administradora","1057577987","3213055412",2005,7,13, "C.C.", "femenino");
        }
    }

    // ================================
    // MÉTODOS DE BÚSQUEDA
    // ================================

    public HashMap<String, String> buscarPaciente(String criterio, String pacienteBuscar){
        if(criterio.equals("Documento")){
            return buscarPacientePorDocumento(pacienteBuscar);
        }
        return buscarPacientePorNombre(pacienteBuscar);
    }

    public HashMap<String, String> buscarOptometra(String criterio, String optometraBuscar){
        if(criterio.equals("Documento")){
            return buscarOptometraPorDocumento(optometraBuscar);
        }
        return buscarOptometraPorNombre(optometraBuscar);
    }

    public HashMap<String, String> buscarOptometraPorNombre(String nombre){
        HashMap<String, String> listaOptometras = new HashMap<>();
        for (Optometra optometra : optometras.values()) {
            if(optometra.getNombre().toLowerCase().contains(nombre.toLowerCase())){
                listaOptometras.put(optometra.getNumero_documento(), optometra.getNombre());
            }
        }
        return listaOptometras;
    }

    public HashMap<String, String> buscarOptometraPorDocumento(String numero_documento){
        HashMap<String, String> listaOptometras = new HashMap<>();
        for (Optometra optometra : optometras.values()) {
            if(optometra.getNumero_documento().contains(numero_documento)){
                listaOptometras.put(optometra.getNumero_documento(), optometra.getNombre());
            }
        }
        return listaOptometras;
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

    public Paciente buscarPacientePorDocumentono(String documento) {
        for (Paciente paciente : pacientes.values()) {
            if (paciente.getNumero_documento().equals(documento)) {
                return paciente;
            }
        }
        return null;
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

    // ================================
    // CRUD USUARIOS
    // ================================

    public boolean crearUsuario(String correo_electronico,String contrasenia ,boolean isAdmin,String nombre, String numero_documento,
                                String numero_celular, int anio, int mes, int dia, String tipo_documento, String genero) {
        if (usuarios.containsKey(numero_documento)) return false;

        Usuario nuevoUsuario = new Usuario(correo_electronico, contrasenia, isAdmin, nombre,
                numero_documento, numero_celular, anio, mes, dia,
                tipo_documento, genero);
        usuarios.put(numero_documento, nuevoUsuario);

        // Guardar en Firebase
        firebaseRepository.guardarUsuario(nuevoUsuario, new FirebaseRepository.OnDataSavedListener() {
            @Override
            public void onSuccess() {
                System.out.println("Usuario guardado en Firebase: " + numero_documento);
            }

            @Override
            public void onError(String error) {
                System.out.println("Error guardando usuario en Firebase: " + error);
            }
        });

        return true;
    }

    public boolean actualizarContrasena(String correo, String nuevaContrasena) {
        for (Usuario usuario : usuarios.values()) {
            if (usuario.getCorreo_electronico().equalsIgnoreCase(correo)) {
                usuario.setContrasenia(nuevaContrasena);

                // Actualizar en Firebase
                firebaseRepository.guardarUsuario(usuario, new FirebaseRepository.OnDataSavedListener() {
                    @Override
                    public void onSuccess() {
                        System.out.println("Contraseña actualizada en Firebase");
                    }

                    @Override
                    public void onError(String error) {
                        System.out.println("Error actualizando contraseña en Firebase: " + error);
                    }
                });

                return true;
            }
        }
        for (Optometra optometra : optometras.values()) {
            if (optometra.getCorreo_electronico().equalsIgnoreCase(correo)) {
                optometra.setContrasenia(nuevaContrasena);

                // Actualizar en Firebase
                firebaseRepository.guardarOptometra(optometra, new FirebaseRepository.OnDataSavedListener() {
                    @Override
                    public void onSuccess() {
                        System.out.println("Contraseña de optometra actualizada en Firebase");
                    }

                    @Override
                    public void onError(String error) {
                        System.out.println("Error actualizando contraseña de optometra en Firebase: " + error);
                    }
                });

                return true;
            }
        }
        return false;
    }

    public HashMap<String, Usuario> getTodosUsuarios() {
        HashMap<String, Usuario> todosUsuarios = new HashMap<>();
        for (Usuario usuario : usuarios.values()) {
            todosUsuarios.put(usuario.getNumero_documento(), usuario);
        }
        for (Optometra optometra : optometras.values()) {
            todosUsuarios.put(optometra.getNumero_documento(), optometra);
        }

        return todosUsuarios;
    }

    public void cerrarSesion() {
        this.usuarioActual = null;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    public Usuario leerUsuario(String numero_documento) {
        return usuarios.get(numero_documento);
    }

    public boolean actualizarUsuario(String numero_documento, String correo_electronico, String nombre,
                                     String numero_celular, int anio, int mes, int dia,
                                     String tipo_documento, String genero) {
        try {
            Usuario usuario = usuarios.get(numero_documento);
            if (usuario != null) {
                usuario.setCorreo_electronico(correo_electronico);
                usuario.setNombre(nombre);
                usuario.setNumero_celular(numero_celular);
                usuario.setFechaNacimientoFromComponents(anio, mes, dia);
                usuario.asignarTipoDocumento(tipo_documento);
                usuario.asignarGenero(genero);

                if (usuarioActual != null && usuarioActual.getNumero_documento().equals(numero_documento)) {
                    usuarioActual = usuario;
                }

                // Actualizar en Firebase
                firebaseRepository.guardarUsuario(usuario, new FirebaseRepository.OnDataSavedListener() {
                    @Override
                    public void onSuccess() {
                        System.out.println("Usuario actualizado en Firebase: " + numero_documento);
                    }

                    @Override
                    public void onError(String error) {
                        System.out.println("Error actualizando usuario en Firebase: " + error);
                    }
                });

                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean cambiarContraseniaUsuario(String numeroDocumento, String nuevaContrasenia) {
        try {
            Usuario usuario = usuarios.get(numeroDocumento);
            if (usuario != null) {
                usuario.setContrasenia(nuevaContrasenia);

                // Actualizar también el usuario actual si es el mismo
                if (usuarioActual != null && usuarioActual.getNumero_documento().equals(numeroDocumento)) {
                    usuarioActual.setContrasenia(nuevaContrasenia);
                }

                // Actualizar en Firebase
                firebaseRepository.guardarUsuario(usuario, new FirebaseRepository.OnDataSavedListener() {
                    @Override
                    public void onSuccess() {
                        System.out.println("Contraseña actualizada en Firebase");
                    }

                    @Override
                    public void onError(String error) {
                        System.out.println("Error actualizando contraseña en Firebase: " + error);
                    }
                });

                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
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

    public String getDocumentoOptometra(String nombre_optometra){
        for (Optometra o : optometras.values()) {
            if(o.getNombre().equalsIgnoreCase(nombre_optometra)){
                return o.getNumero_documento();
            }
        }
        return "";
    }

    public String getNombreOptometra(String numero_documento){
        for (Optometra o : optometras.values()) {
            if(o.getNumero_documento().equalsIgnoreCase(numero_documento)){
                return o.getNombre();
            }
        }
        return "";
    }

    public String getNombrePaciente(String numero_documento){
        for (Paciente o : pacientes.values()) {
            if(o.getNumero_documento().equalsIgnoreCase(numero_documento)){
                return o.getNombre();
            }
        }
        return "";
    }

    public String[] obtenerNombresOptometras(){
        String[]listaOptometras = new String[optometras.size()];
        int i = 0;
        for (Optometra o : optometras.values()) {
            listaOptometras [i] = o.getNombre();
            i++;
        }
        return listaOptometras;
    }

    public HashMap<String, String> obtenerListaOptometras() {
        HashMap<String, String> lista = new HashMap<>();
        for (Optometra o : optometras.values()) {
            lista.put(o.getNumero_documento(), o.getNombre());
        }
        return lista;
    }

    public boolean crearOptometra(String correo_electronico,String contrasenia ,String nombre, String numero_documento,
                                  String numero_celular, int anio, int mes, int dia, String tipo_documento, String genero) {
        if (optometras.containsKey(numero_documento)) return false;

        Optometra nuevoOptometra = new Optometra(correo_electronico, contrasenia, true, nombre,
                numero_documento, numero_celular, anio, mes, dia,
                tipo_documento, genero);
        optometras.put(numero_documento, nuevoOptometra);
        usuarios.put(numero_documento, nuevoOptometra); // También agregar a usuarios

        // Guardar en Firebase
        firebaseRepository.guardarOptometra(nuevoOptometra, new FirebaseRepository.OnDataSavedListener() {
            @Override
            public void onSuccess() {
                System.out.println("Optometra guardado en Firebase: " + numero_documento);
            }

            @Override
            public void onError(String error) {
                System.out.println("Error guardando optometra en Firebase: " + error);
            }
        });

        return true;
    }

    public String generarContrasena() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder contrasena = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            contrasena.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return contrasena.toString();
    }

    public Optometra leerOptometra(String numero_documento) {
        return optometras.get(numero_documento);
    }

    public boolean actualizarOptometra(String correo_electronico, String nombre, String numero_documento,
                                       String numero_celular, int anio, int mes, int dia,
                                       String tipo_documento, String genero) {
        Optometra optometra = optometras.get(numero_documento);
        if (optometra != null) {
            optometra.setCorreo_electronico(correo_electronico);
            optometra.setNombre(nombre);
            optometra.setNumero_celular(numero_celular);
            optometra.setFechaNacimientoFromComponents(anio, mes, dia);
            optometra.asignarTipoDocumento(tipo_documento);
            optometra.asignarGenero(genero);

            // Actualizar en Firebase
            firebaseRepository.guardarOptometra(optometra, new FirebaseRepository.OnDataSavedListener() {
                @Override
                public void onSuccess() {
                    System.out.println("Optometra actualizado en Firebase: " + numero_documento);
                }

                @Override
                public void onError(String error) {
                    System.out.println("Error actualizando optometra en Firebase: " + error);
                }
            });

            return true;
        }
        return false;
    }

    public boolean eliminarOptometra(String numero_documento) {
        if (optometras.remove(numero_documento) != null) {
            usuarios.remove(numero_documento);

            // Eliminar de Firebase
            firebaseRepository.eliminarOptometra(numero_documento, new FirebaseRepository.OnDataSavedListener() {
                @Override
                public void onSuccess() {
                    System.out.println("Optometra eliminado de Firebase: " + numero_documento);
                }

                @Override
                public void onError(String error) {
                    System.out.println("Error eliminando optometra de Firebase: " + error);
                }
            });

            return true;
        }
        return false;
    }

    public ArrayList<Integer> citasConsultorio(String id_consultorio){
        ArrayList<Integer>idsCitas = new ArrayList<>();

        for(Cita cita:citas.values()){
            if(cita.getId_consultorio().equalsIgnoreCase(id_consultorio)){
                idsCitas.add(cita.getId());
            }
        }
        return idsCitas;
    }

    public void eliminarCitasConsultorio(String id_consultorio){
        ArrayList<Integer>idsCitas = citasConsultorio(id_consultorio);
        for (Integer idCita : idsCitas) {
            citas.remove(idCita);
        }
    }

    public void cambiarCitasConsultorio(String anterior_consultorio, String nuevo_consultorio){
        ArrayList<Integer>idsCitas = citasConsultorio(anterior_consultorio);
        for (Integer idCita : idsCitas) {
            citas.get(idCita).setId_consultorio(nuevo_consultorio);
        }
    }

    public ArrayList<Integer> citasOptometra(String numero_documento){
        ArrayList<Integer>idsCitas = new ArrayList<>();

        for(Cita cita:citas.values()){
            if(cita.getDocumento_optometra().equalsIgnoreCase(numero_documento)){
                idsCitas.add(cita.getId());
            }
        }
        return idsCitas;
    }

    public void eliminarCitasOptometra(String numero_documento){
        ArrayList<Integer>idsCitas = citasOptometra(numero_documento);
        for (Integer idCita : idsCitas) {
            citas.remove(idCita);
        }
    }

    public void cambiarCitasOptometra(String anterior_optometra, String nuevo_optometra){
        ArrayList<Integer>idsCitas = citasOptometra(anterior_optometra);
        for (Integer idCita : idsCitas) {
            citas.get(idCita).setDocumento_optometra(nuevo_optometra);
        }
    }

    // ================================
    // CRUD PACIENTES
    // ================================

    public boolean crearPaciente(String correo_electronico,String nombre, String numero_documento,
                                 String numero_celular, int anio, int mes, int dia, String tipo_documento, String genero) {
        if (pacientes.containsKey(numero_documento)) return false;

        Paciente nuevoPaciente = new Paciente(correo_electronico, nombre, numero_documento,
                numero_celular, anio, mes, dia, tipo_documento, genero);
        pacientes.put(numero_documento, nuevoPaciente);

        // Guardar en Firebase
        firebaseRepository.guardarPaciente(nuevoPaciente, new FirebaseRepository.OnDataSavedListener() {
            @Override
            public void onSuccess() {
                System.out.println("Paciente guardado en Firebase: " + numero_documento);
            }

            @Override
            public void onError(String error) {
                System.out.println("Error guardando paciente en Firebase: " + error);
            }
        });

        return true;
    }

    public Paciente leerPaciente(String numero_documento) {
        return pacientes.get(numero_documento);
    }

    public boolean existePaciente(String numero_documento){
        return pacientes.containsKey(numero_documento);
    }

    public boolean actualizarPaciente(String correo_electronico,String nombre, String numero_documento,
                                      String numero_celular, int anio, int mes, int dia, String tipo_documento, String genero) {
        Paciente paciente = pacientes.get(numero_documento);
        if (paciente != null) {
            paciente.setCorreo_electronico(correo_electronico);
            paciente.setNombre(nombre);
            paciente.setNumero_celular(numero_celular);
            paciente.setFechaNacimientoFromComponents(anio, mes, dia);
            paciente.asignarTipoDocumento(tipo_documento);
            paciente.asignarGenero(genero);

            // Actualizar en Firebase
            firebaseRepository.guardarPaciente(paciente, new FirebaseRepository.OnDataSavedListener() {
                @Override
                public void onSuccess() {
                    System.out.println("Paciente actualizado en Firebase: " + numero_documento);
                }

                @Override
                public void onError(String error) {
                    System.out.println("Error actualizando paciente en Firebase: " + error);
                }
            });

            return true;
        }
        return false;
    }

    public boolean eliminarPaciente(String numero_documento) {
        if (pacientes.remove(numero_documento) != null) {
            // Eliminar de Firebase
            firebaseRepository.eliminarPaciente(numero_documento, new FirebaseRepository.OnDataSavedListener() {
                @Override
                public void onSuccess() {
                    System.out.println("Paciente eliminado de Firebase: " + numero_documento);
                }

                @Override
                public void onError(String error) {
                    System.out.println("Error eliminando paciente de Firebase: " + error);
                }
            });

            return true;
        }
        return false;
    }

    // ========================================
    // CRUD CITAS
    // ========================================

    public boolean crearCita(String docOptometra, String docPaciente, String idConsultorio,
                             int anio, int mes, int dia, int hora, int minutos) {
        int id = Cita.getContador();
        if (citas.containsKey(id)) return false;

        Cita nuevaCita = new Cita(docOptometra, docPaciente, idConsultorio, anio, mes, dia, hora, minutos);
        citas.put(nuevaCita.getId(), nuevaCita);

        // Actualizar contador
        Cita.setContador(id + 1);

        // Guardar en Firebase
        firebaseRepository.guardarCita(nuevaCita, new FirebaseRepository.OnDataSavedListener() {
            @Override
            public void onSuccess() {
                System.out.println("Cita guardada en Firebase: " + nuevaCita.getId());
            }

            @Override
            public void onError(String error) {
                System.out.println("Error guardando cita en Firebase: " + error);
            }
        });

        return true;
    }

    public boolean comprobarHorarioOptometra(int id_cita, String documento_optometra, int anio, int mes, int dia, int hora, int minutos) {
        String fechaBuscar = String.format("%04d-%02d-%02d", anio, mes, dia);
        String horaBuscar = String.format("%02d:%02d", hora, minutos);

        HashMap<Integer, Cita> lista_citas = new HashMap<>(citas);
        if (id_cita != -1) lista_citas.remove(id_cita);

        for (Cita cita : lista_citas.values()) {
            if (cita.getFecha().equals(fechaBuscar) &&
                    cita.getDocumento_optometra().equalsIgnoreCase(documento_optometra)) {

                // Comparar horarios como strings
                if (cita.getHora().equals(horaBuscar)) {
                    return false; // Hay conflicto de horario
                }
            }
        }
        return true;
    }

    public boolean comprobarHorarioConsultorio(int id_cita, String idConsultorio, int anio, int mes, int dia, int hora, int minutos) {
        String fechaBuscar = String.format("%04d-%02d-%02d", anio, mes, dia);
        String horaBuscar = String.format("%02d:%02d", hora, minutos);

        HashMap<Integer, Cita> lista_citas = new HashMap<>(citas);
        if (id_cita != -1) lista_citas.remove(id_cita);

        for (Cita cita : lista_citas.values()) {
            if (cita.getFecha().equals(fechaBuscar) &&
                    cita.getId_consultorio().equals(idConsultorio)) {

                // Comparar horarios como strings
                if (cita.getHora().equals(horaBuscar)) {
                    return false; // Hay conflicto de horario
                }
            }
        }
        return true;
    }

    public void actualizarCita(int id_cita, int anio, int mes, int dia,int hora, int minutos,
                               String documento_optometra, String id_consultorio ){
        Cita cita = citas.get(id_cita);
        if (cita != null) {
            cita.setFechaFromComponents(anio, mes, dia);
            cita.setHoraFromComponents(hora, minutos);
            cita.setDocumento_optometra(documento_optometra);
            cita.setId_consultorio(id_consultorio);

            // Actualizar en Firebase
            firebaseRepository.guardarCita(cita, new FirebaseRepository.OnDataSavedListener() {
                @Override
                public void onSuccess() {
                    System.out.println("Cita actualizada en Firebase: " + id_cita);
                }

                @Override
                public void onError(String error) {
                    System.out.println("Error actualizando cita en Firebase: " + error);
                }
            });
        }
    }

    public boolean eliminarCita(int id) {
        if (citas.remove(id) != null) {
            // Eliminar de Firebase
            firebaseRepository.eliminarCita(String.valueOf(id), new FirebaseRepository.OnDataSavedListener() {
                @Override
                public void onSuccess() {
                    System.out.println("Cita eliminada de Firebase: " + id);
                }

                @Override
                public void onError(String error) {
                    System.out.println("Error eliminando cita de Firebase: " + error);
                }
            });

            return true;
        }
        return false;
    }

    // ================================
    // CRUD CONSULTORIOS
    // ================================

    public String[] obtenerIdsConsultorios(){
        return consultorios.keySet().toArray(new String[0]);
    }

    public boolean crearConsultorio(String id, String direccion, String ciudad) {
        if (consultorios.containsKey(id)) return false;

        Consultorio nuevoConsultorio = new Consultorio(id, direccion, ciudad);
        consultorios.put(id, nuevoConsultorio);

        // Guardar en Firebase
        firebaseRepository.guardarConsultorio(nuevoConsultorio, new FirebaseRepository.OnDataSavedListener() {
            @Override
            public void onSuccess() {
                System.out.println("Consultorio guardado en Firebase: " + id);
            }

            @Override
            public void onError(String error) {
                System.out.println("Error guardando consultorio en Firebase: " + error);
            }
        });

        return true;
    }

    public Consultorio leerConsultorio(String id) {
        return consultorios.get(id);
    }

    public boolean actualizarConsultorio(String id, String direccion, String ciudad) {
        Consultorio consultorio = consultorios.get(id);
        if (consultorio != null) {
            consultorio.setDireccion(direccion);
            consultorio.setCiudad(ciudad);

            // Actualizar en Firebase
            firebaseRepository.guardarConsultorio(consultorio, new FirebaseRepository.OnDataSavedListener() {
                @Override
                public void onSuccess() {
                    System.out.println("Consultorio actualizado en Firebase: " + id);
                }

                @Override
                public void onError(String error) {
                    System.out.println("Error actualizando consultorio en Firebase: " + error);
                }
            });

            return true;
        }
        return false;
    }

    public boolean eliminarConsultorio(String id) {
        if (consultorios.remove(id) != null) {
            // Eliminar de Firebase
            firebaseRepository.eliminarConsultorio(id, new FirebaseRepository.OnDataSavedListener() {
                @Override
                public void onSuccess() {
                    System.out.println("Consultorio eliminado de Firebase: " + id);
                }

                @Override
                public void onError(String error) {
                    System.out.println("Error eliminando consultorio de Firebase: " + error);
                }
            });

            return true;
        }
        return false;
    }

    // ================================
    // CONSULTA CITAS POR CRITERIO
    // ================================

    public List<Cita> consultarCitasDia(int dia, int mes, int anio) {
        List<Cita> citasDia = new ArrayList<>();
        String fechaBuscar = String.format("%04d-%02d-%02d", anio, mes, dia);

        for (Cita cita : citas.values()) {
            if (cita.getFecha().equals(fechaBuscar)) {
                citasDia.add(cita);
            }
        }
        return citasDia;
    }

    public List<Cita> consultarCitasSemana(int inicioSemana, int finSemana, int mes){
        List<Cita> citasSemana = new ArrayList<>();
        for(Cita cita : citas.values()){
            int diaCita = cita.getDia();
            if(diaCita >= inicioSemana && diaCita <= finSemana &&
                    cita.getMes() == mes){
                citasSemana.add(cita);
            }
        }
        return citasSemana;
    }

    public List<Cita> consultarCitasMes(int mes){
        List<Cita> citasMes = new ArrayList<>();
        String mesBuscar = String.format("-%02d-", mes);

        for(Cita cita: citas.values()){
            if(cita.getFecha().contains(mesBuscar)){
                citasMes.add(cita);
            }
        }
        return citasMes;
    }

    public List<Cita> consultarCitas(){
        return new ArrayList<>(citas.values());
    }

    // MÉTODO PARA SINCRONIZAR MANUALMENTE
    public void sincronizarConFirebase() {
        cargarDatosDesdeFirebase();
    }

    // GETTERS para los HashMap (si los necesitas)
    public HashMap<String, Optometra> getOptometras() {
        return optometras;
    }

    public HashMap<String, Paciente> getPacientes() {
        return pacientes;
    }

    public HashMap<Integer, Cita> getCitas() {
        return citas;
    }

    public HashMap<String, Consultorio> getConsultorios() {
        return consultorios;
    }

    public HashMap<String, Usuario> getUsuarios() {
        return usuarios;
    }

    public boolean isDatosCargados() {
        return datosCargados;
    }
}