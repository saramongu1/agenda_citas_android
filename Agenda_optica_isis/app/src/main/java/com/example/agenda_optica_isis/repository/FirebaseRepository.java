// FirebaseRepository.java
package com.example.agenda_optica_isis.repository;

import com.google.firebase.database.*;
import com.example.agenda_optica_isis.model.*;

public class FirebaseRepository {
    private DatabaseReference databaseRef;

    public FirebaseRepository() {
        this.databaseRef = FirebaseDatabase.getInstance().getReference();
    }

    // MÉTODOS PARA GUARDAR DATOS (sin duplicados)
    public void guardarUsuario(Usuario usuario, OnDataSavedListener listener) {
        databaseRef.child("usuarios").child(usuario.getNumero_documento()).setValue(usuario)
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    public void guardarOptometra(Optometra optometra, OnDataSavedListener listener) {
        databaseRef.child("optometras").child(optometra.getNumero_documento()).setValue(optometra)
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    public void guardarPaciente(Paciente paciente, OnDataSavedListener listener) {
        databaseRef.child("pacientes").child(paciente.getNumero_documento()).setValue(paciente)
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    public void guardarCita(Cita cita, OnDataSavedListener listener) {
        databaseRef.child("citas").child(String.valueOf(cita.getId())).setValue(cita)
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    public void guardarConsultorio(Consultorio consultorio, OnDataSavedListener listener) {
        databaseRef.child("consultorios").child(consultorio.getId()).setValue(consultorio)
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    // MÉTODOS PARA ELIMINAR
    public void eliminarOptometra(String documento, OnDataSavedListener listener) {
        databaseRef.child("optometras").child(documento).removeValue()
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    public void eliminarPaciente(String documento, OnDataSavedListener listener) {
        databaseRef.child("pacientes").child(documento).removeValue()
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    public void eliminarCita(String id, OnDataSavedListener listener) {
        databaseRef.child("citas").child(id).removeValue()
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    public void eliminarConsultorio(String id, OnDataSavedListener listener) {
        databaseRef.child("consultorios").child(id).removeValue()
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    // MÉTODO PARA CARGAR TODOS LOS DATOS
    public void cargarTodosLosDatos(OnDataLoadedListener listener) {
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onDataLoaded(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }

    public interface OnDataSavedListener {
        void onSuccess();
        void onError(String error);
    }

    public interface OnDataLoadedListener {
        void onDataLoaded(DataSnapshot dataSnapshot);
        void onError(String error);
    }
}