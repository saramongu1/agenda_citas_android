package com.example.agenda_optica_isis.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.agenda_optica_isis.R;
import com.example.agenda_optica_isis.presenter.PresenterAgregarConsultorioFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AgregarConsultorioFragment extends Fragment {

    private TextInputEditText etIdConsultorio;
    private TextInputEditText etDireccionConsultorio;
    private TextInputEditText etCiudadConsultorio;
    private MaterialButton btnCrearConsultorio;
    private PresenterAgregarConsultorioFragment presenter;

    // Variables para guardar estado (similar a AgregarCitaFragment)
    private static String idConsultorioGuardado;
    private static String direccionGuardada;
    private static String ciudadGuardada;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_agregar_consultorio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        enlazarVistas(view);
        iniciarPresenter();
        btnCrearConsultorio.setOnClickListener(v -> crearConsultorio());

        // Restaurar estado guardado
        restaurarEstado();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Guardar estado al pausar
        guardarEstado();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Restaurar estado al resumir
        restaurarEstado();
    }

    private void guardarEstado() {
        idConsultorioGuardado = getIdConsultorio();
        direccionGuardada = getDireccionConsultorio();
        ciudadGuardada = getCiudadConsultorio();
    }

    private void restaurarEstado() {
        if (idConsultorioGuardado != null && !idConsultorioGuardado.isEmpty()) {
            etIdConsultorio.setText(idConsultorioGuardado);
        }
        if (direccionGuardada != null && !direccionGuardada.isEmpty()) {
            etDireccionConsultorio.setText(direccionGuardada);
        }
        if (ciudadGuardada != null && !ciudadGuardada.isEmpty()) {
            etCiudadConsultorio.setText(ciudadGuardada);
        }
    }

    public void crearConsultorio(){
        presenter.crearConsultorio();
    }

    public void irAConsultorios(){
        if (getActivity() instanceof MenuActivity) {
            ((MenuActivity) getActivity()).replaceFragment(new ConsultoriosFragment());
        }
    }

    public void iniciarPresenter(){
        presenter = new PresenterAgregarConsultorioFragment(this);
    }

    private void enlazarVistas(@NonNull View view){
        etIdConsultorio = view.findViewById(R.id.inputIdConsultorio);
        etDireccionConsultorio = view.findViewById(R.id.inputDireccionConsultorio);
        etCiudadConsultorio = view.findViewById(R.id.inputCiudadConsultorio);
        btnCrearConsultorio = view.findViewById(R.id.btnCrearConsultorio);
    }

    public String getIdConsultorio(){
        return etIdConsultorio.getText() != null ? etIdConsultorio.getText().toString().trim() : "";
    }

    public String getDireccionConsultorio(){
        return etDireccionConsultorio.getText() != null ? etDireccionConsultorio.getText().toString().trim() : "";
    }

    public String getCiudadConsultorio(){
        return etCiudadConsultorio.getText() != null ? etCiudadConsultorio.getText().toString().trim() : "";
    }

    public void limpiarCampos() {
        etIdConsultorio.setText("");
        etDireccionConsultorio.setText("");
        etCiudadConsultorio.setText("");

        // Limpiar también el estado guardado
        idConsultorioGuardado = "";
        direccionGuardada = "";
        ciudadGuardada = "";
    }

    public void mostrarMensaje(String mensaje) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
    }
}