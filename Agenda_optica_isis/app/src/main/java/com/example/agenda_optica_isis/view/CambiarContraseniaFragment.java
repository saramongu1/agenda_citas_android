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
import com.example.agenda_optica_isis.utils.ModernSnackBar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CambiarContraseniaFragment extends Fragment {

    private TextInputEditText etContraseniaActual;
    private TextInputEditText etNuevaContrasenia;
    private TextInputEditText etConfirmarContrasenia;
    private TextInputLayout layoutContraseniaActual;
    private TextInputLayout layoutNuevaContrasenia;
    private TextInputLayout layoutConfirmarContrasenia;
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
        configurarListeners();
        setupUI();
    }

    private void initPresenter() {
        presenter = new PresenterCambiarContraseniaFragment(this);
    }

    public void enlazarVistas(@NonNull View view) {
        etContraseniaActual = view.findViewById(R.id.inputContraseniaActual);
        etNuevaContrasenia = view.findViewById(R.id.inputNuevaContrasenia);
        etConfirmarContrasenia = view.findViewById(R.id.inputConfirmarContrasenia);
        layoutContraseniaActual = view.findViewById(R.id.layoutContraseniaActual);
        layoutNuevaContrasenia = view.findViewById(R.id.layoutNuevaContrasenia);
        layoutConfirmarContrasenia = view.findViewById(R.id.layoutConfirmarContrasenia);
        btnCambiarContrasenia = view.findViewById(R.id.btnCambiarContrasenia);
        btnCancelarCambio = view.findViewById(R.id.btnCancelarCambio);
    }

    private void configurarListeners() {
        btnCambiarContrasenia.setOnClickListener(v -> cambiarContrasenia());
        btnCancelarCambio.setOnClickListener(v -> cancelarCambio());

        // Limpiar errores cuando el usuario empiece a escribir
        etContraseniaActual.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                layoutContraseniaActual.setError(null);
            }
        });

        etNuevaContrasenia.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                layoutNuevaContrasenia.setError(null);
            }
        });

        etConfirmarContrasenia.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                layoutConfirmarContrasenia.setError(null);
            }
        });
    }

    private void setupUI() {
        // Configurar iconos y hints
        layoutContraseniaActual.setHint("Contraseña Actual");
        layoutNuevaContrasenia.setHint("Nueva Contraseña");
        layoutConfirmarContrasenia.setHint("Confirmar Nueva Contraseña");
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
        // Validaciones básicas antes de llamar al presenter
        if (getContraseniaActual().isEmpty()) {
            layoutContraseniaActual.setError("Ingresa tu contraseña actual");
            return;
        }

        if (getNuevaContrasenia().isEmpty()) {
            layoutNuevaContrasenia.setError("Ingresa la nueva contraseña");
            return;
        }

        if (getConfirmarContrasenia().isEmpty()) {
            layoutConfirmarContrasenia.setError("Confirma la nueva contraseña");
            return;
        }

        if (getNuevaContrasenia().length() < 6) {
            layoutNuevaContrasenia.setError("La contraseña debe tener al menos 6 caracteres");
            return;
        }

        if (!getNuevaContrasenia().equals(getConfirmarContrasenia())) {
            layoutConfirmarContrasenia.setError("Las contraseñas no coinciden");
            return;
        }

        presenter.cambiarContrasenia();
    }

    private void cancelarCambio() {
        // Regresar al fragment de usuario
        if (getActivity() instanceof MenuActivity) {
            ((MenuActivity) getActivity()).replaceFragment(new UsuarioFragment(), "USUARIO");
        }
    }

    public void mostrarMensaje(String mensaje) {
        if (getView() != null) {
            ModernSnackBar.mostrar(getView(), mensaje, ModernSnackBar.SUCCESS);
        }
    }

    public void mostrarError(String mensaje) {
        if (getView() != null) {
            ModernSnackBar.mostrar(getView(), mensaje, ModernSnackBar.ERROR);
        }
    }

    public void limpiarCampos() {
        etContraseniaActual.setText("");
        etNuevaContrasenia.setText("");
        etConfirmarContrasenia.setText("");

        // Limpiar errores
        layoutContraseniaActual.setError(null);
        layoutNuevaContrasenia.setError(null);
        layoutConfirmarContrasenia.setError(null);
    }

    public void irAUsuarioFragment() {
        if (getActivity() instanceof MenuActivity) {
            ((MenuActivity) getActivity()).replaceFragment(new UsuarioFragment(), "USUARIO");
        }
    }

    public void setErrorContraseniaActual(String mensaje) {
        layoutContraseniaActual.setError(mensaje);
    }

    public void setErrorNuevaContrasenia(String mensaje) {
        layoutNuevaContrasenia.setError(mensaje);
    }

    public void setErrorConfirmarContrasenia(String mensaje) {
        layoutConfirmarContrasenia.setError(mensaje);
    }
}