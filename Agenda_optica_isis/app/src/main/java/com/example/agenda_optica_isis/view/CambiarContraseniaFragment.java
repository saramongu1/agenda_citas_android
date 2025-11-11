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
import com.example.agenda_optica_isis.presenter.PresenterCambiarContraseniaFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class CambiarContraseniaFragment extends Fragment {

    private TextInputEditText etContraseniaActual;
    private TextInputEditText etNuevaContrasenia;
    private TextInputEditText etConfirmarContrasenia;
    private MaterialButton btnCambiarContrasenia;
    private MaterialButton btnCancelarCambio;

    private PresenterCambiarContraseniaFragment presenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cambiar_contrasenia, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        enlazarVistas(view);
        initPresenter();

        btnCambiarContrasenia.setOnClickListener(v -> cambiarContrasenia());
        btnCancelarCambio.setOnClickListener(v -> cancelarCambio());
    }

    private void initPresenter() {
        presenter = new PresenterCambiarContraseniaFragment(this);
    }

    public void enlazarVistas(@NonNull View view) {
        etContraseniaActual = view.findViewById(R.id.inputContraseniaActual);
        etNuevaContrasenia = view.findViewById(R.id.inputNuevaContrasenia);
        etConfirmarContrasenia = view.findViewById(R.id.inputConfirmarContrasenia);
        btnCambiarContrasenia = view.findViewById(R.id.btnCambiarContrasenia);
        btnCancelarCambio = view.findViewById(R.id.btnCancelarCambio);
    }

    public String getContraseniaActual() {
        return etContraseniaActual.getText().toString().trim();
    }

    public String getNuevaContrasenia() {
        return etNuevaContrasenia.getText().toString().trim();
    }

    public String getConfirmarContrasenia() {
        return etConfirmarContrasenia.getText().toString().trim();
    }

    private void cambiarContrasenia() {
        presenter.cambiarContrasenia();
    }

    private void cancelarCambio() {
        // Regresar al fragment de usuario
        if (getActivity() instanceof MenuActivity) {
            ((MenuActivity) getActivity()).replaceFragment(new UsuarioFragment(), "USUARIO");
        }
    }

    public void mostrarMensaje(String mensaje) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public void limpiarCampos() {
        etContraseniaActual.setText("");
        etNuevaContrasenia.setText("");
        etConfirmarContrasenia.setText("");
    }

    public void irAUsuarioFragment() {
        if (getActivity() instanceof MenuActivity) {
            ((MenuActivity) getActivity()).replaceFragment(new UsuarioFragment(), "USUARIO");
        }
    }
}