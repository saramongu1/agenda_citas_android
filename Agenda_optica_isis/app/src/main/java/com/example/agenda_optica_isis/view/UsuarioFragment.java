package com.example.agenda_optica_isis.view;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.agenda_optica_isis.R;
import com.example.agenda_optica_isis.presenter.PresenterUsuarioFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class UsuarioFragment extends Fragment {

    private TextInputEditText etNombreUsuario;
    private TextInputEditText etDocumentoUsuario;
    private Spinner spnTipoDocumentoUsuario;
    private TextInputEditText etCorreoUsuario;
    private TextInputEditText etNumeroCelularUsuario;
    private TextInputEditText etFechaNacimientoUsuario;
    private Spinner spnGeneroUsuario;
    private MaterialButton btnGuardarCambiosUsuario;
    private MaterialButton btnEditarUsuario;
    private MaterialButton btnCambiarContrasenia;

    private PresenterUsuarioFragment presenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_usuario, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        enlazarVistas(view);
        initSpinnerTiposDocumento(view);
        initSpinnerGenero(view);
        deshabilitarCampos();
        initPresenter();
        initInputFecha(view);
        cargarDatosUsuario();

        btnGuardarCambiosUsuario.setOnClickListener(v -> guardarCambios());
        btnEditarUsuario.setOnClickListener(v -> editarUsuario());
        btnCambiarContrasenia.setOnClickListener(v -> cambiarContrasenia());
    }

    public void cambiarContrasenia() {
        // Navegar al fragment de cambiar contraseña
        if (getActivity() instanceof MenuActivity) {
            CambiarContraseniaFragment fragment = new CambiarContraseniaFragment();
            ((MenuActivity) getActivity()).replaceFragment(fragment, "CAMBIAR_CONTRASENIA");
        }
    }

    public void editarUsuario() {
        habilitarCampos();
        btnGuardarCambiosUsuario.setVisibility(VISIBLE);
        btnEditarUsuario.setVisibility(GONE);
    }

    private void initPresenter() {
        presenter = new PresenterUsuarioFragment(this);
    }

    public void enlazarVistas(@NonNull View view) {
        etNombreUsuario = view.findViewById(R.id.inputNombreUsuario);
        etDocumentoUsuario = view.findViewById(R.id.inputDocumentoUsuario);
        etCorreoUsuario = view.findViewById(R.id.inputCorreoUsuario);
        etNumeroCelularUsuario = view.findViewById(R.id.inputCelularUsuario);
        etFechaNacimientoUsuario = view.findViewById(R.id.inputFechaNacimientoUsuario);
        btnGuardarCambiosUsuario = view.findViewById(R.id.btnGuardarCambiosUsuario);
        spnTipoDocumentoUsuario = view.findViewById(R.id.spinnerTipoDocumentoUsuario);
        spnGeneroUsuario = view.findViewById(R.id.spinnerGeneroUsuario);
        btnEditarUsuario = view.findViewById(R.id.btnEditarUsuario);
        btnCambiarContrasenia = view.findViewById(R.id.btnCambiarContrasenia);
    }

    public void initSpinnerTiposDocumento(@NonNull View view) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.tipos_documento,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTipoDocumentoUsuario.setAdapter(adapter);
    }

    public void initSpinnerGenero(@NonNull View view) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.tipos_genero,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGeneroUsuario.setAdapter(adapter);
    }

    public void asignarDetallesUsuario(String nombre, String documento, String tipo_documento, String genero,
                                       String numero_celular, String correo, String fecha_nacimiento) {
        etNombreUsuario.setText(nombre);
        etDocumentoUsuario.setText(documento);
        spnTipoDocumentoUsuario.setSelection(posicionSeleccionTipoDocumento(tipo_documento));
        spnGeneroUsuario.setSelection(posicionSeleccionGenero(genero));
        etNumeroCelularUsuario.setText(numero_celular);
        etCorreoUsuario.setText(correo);
        etFechaNacimientoUsuario.setText(fecha_nacimiento);
    }

    public String getNombreUsuario() {
        return etNombreUsuario.getText().toString().trim();
    }

    public String getDocumentoUsuario() {
        return etDocumentoUsuario.getText().toString().trim();
    }

    public String getTipoDocumentoUsuario() {
        return spnTipoDocumentoUsuario.getSelectedItem().toString();
    }

    public String getCorreoUsuario() {
        return etCorreoUsuario.getText().toString().trim();
    }

    public String getNumeroCelularUsuario() {
        return etNumeroCelularUsuario.getText().toString().trim();
    }

    public String getFechaNacimientoUsuario() {
        return etFechaNacimientoUsuario.getText().toString().trim();
    }

    public String getGeneroUsuario() {
        return spnGeneroUsuario.getSelectedItem().toString();
    }

    private int posicionSeleccionTipoDocumento(String tipo_documento) {
        int posicion = 0;
        if (tipo_documento.equalsIgnoreCase("T.I.")) {
            posicion = 1;
        } else if (tipo_documento.equalsIgnoreCase("C.E.")) {
            posicion = 2;
        } else if (tipo_documento.equalsIgnoreCase("PAS")) {
            posicion = 4;
        }
        return posicion;
    }

    public void deshabilitarCampos() {
        etNombreUsuario.setEnabled(false);
        etDocumentoUsuario.setEnabled(false);
        etCorreoUsuario.setEnabled(false);
        etNumeroCelularUsuario.setEnabled(false);
        etFechaNacimientoUsuario.setEnabled(false);
        spnGeneroUsuario.setEnabled(false);
        spnTipoDocumentoUsuario.setEnabled(false);
    }

    public void habilitarCampos() {
        etNombreUsuario.setEnabled(true);
        etCorreoUsuario.setEnabled(true);
        etNumeroCelularUsuario.setEnabled(true);
        etFechaNacimientoUsuario.setEnabled(true);
        spnGeneroUsuario.setEnabled(true);
        spnTipoDocumentoUsuario.setEnabled(true);
        // El documento no se puede editar porque es el identificador
        etDocumentoUsuario.setEnabled(false);
    }

    private int posicionSeleccionGenero(String genero) {
        int posicion = 0;
        if (genero.equalsIgnoreCase("masculino")) {
            posicion = 1;
        } else if (genero.equalsIgnoreCase("otro")) {
            posicion = 2;
        }
        return posicion;
    }

    private void cargarDatosUsuario() {
        presenter.cargarInformacionUsuario();
    }

    private void guardarCambios() {
        presenter.actualizarUsuario();
    }

    public void mostrarMensaje(String mensaje) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public void initInputFecha(@NonNull View view) {
        EditText inputFecha = view.findViewById(R.id.inputFechaNacimientoUsuario);

        inputFecha.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePicker = new DatePickerDialog(
                    requireContext(),
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        String fechaSeleccionada = String.format("%02d/%02d/%04d",
                                selectedDay, selectedMonth + 1, selectedYear);
                        inputFecha.setText(fechaSeleccionada);
                    },
                    year, month, day
            );
            datePicker.getDatePicker().setMaxDate(calendar.getTimeInMillis());
            datePicker.show();
        });
    }

    public void irAMenu() {
        if (getActivity() instanceof MenuActivity) {
            ((MenuActivity) getActivity()).replaceFragment(new CalendarioFragment());
        }
    }
}