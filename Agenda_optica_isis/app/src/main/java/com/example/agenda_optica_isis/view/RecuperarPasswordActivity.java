package com.example.agenda_optica_isis.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agenda_optica_isis.R;
import com.example.agenda_optica_isis.presenter.PresenterRecuperarPassword;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RecuperarPasswordActivity extends AppCompatActivity {

    private TextInputEditText etEmail;
    private TextInputEditText etCodigoVerificacion;
    private TextInputEditText etNuevaPassword;
    private TextInputEditText etConfirmarPassword;
    private Button btnEnviarCodigo;
    private Button btnVerificarCodigo;
    private Button btnCambiarPassword;
    private TextView tvTiempoRestante;
    private TextView tvVolverLogin;

    private TextInputLayout layoutCodigoVerificacion;
    private TextInputLayout layoutNuevaPassword;
    private TextInputLayout layoutConfirmarPassword;

    private PresenterRecuperarPassword presenter;
    private CountDownTimer countDownTimer;
    private String currentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_password);

        presenter = new PresenterRecuperarPassword(this);
        enlazarVistas();
        configurarListeners();
        setupUI();
    }

    private void enlazarVistas() {
        etEmail = findViewById(R.id.etEmail);
        etCodigoVerificacion = findViewById(R.id.etCodigoVerificacion);
        etNuevaPassword = findViewById(R.id.etNuevaPassword);
        etConfirmarPassword = findViewById(R.id.etConfirmarPassword);

        btnEnviarCodigo = findViewById(R.id.btnEnviarCodigo);
        btnVerificarCodigo = findViewById(R.id.btnVerificarCodigo);
        btnCambiarPassword = findViewById(R.id.btnCambiarPassword);

        tvTiempoRestante = findViewById(R.id.tvTiempoRestante);
        tvVolverLogin = findViewById(R.id.tvVolverLogin);

        layoutCodigoVerificacion = findViewById(R.id.layoutCodigoVerificacion);
        layoutNuevaPassword = findViewById(R.id.layoutNuevaPassword);
        layoutConfirmarPassword = findViewById(R.id.layoutConfirmarPassword);
    }

    private void configurarListeners() {
        btnEnviarCodigo.setOnClickListener(v -> enviarCodigoVerificacion());
        btnVerificarCodigo.setOnClickListener(v -> verificarCodigo());
        btnCambiarPassword.setOnClickListener(v -> cambiarPassword());
        tvVolverLogin.setOnClickListener(v -> volverALogin());
    }

    private void setupUI() {
        // Ocultar secciones inicialmente
        layoutCodigoVerificacion.setVisibility(android.view.View.GONE);
        layoutNuevaPassword.setVisibility(android.view.View.GONE);
        layoutConfirmarPassword.setVisibility(android.view.View.GONE);
        btnVerificarCodigo.setVisibility(android.view.View.GONE);
        btnCambiarPassword.setVisibility(android.view.View.GONE);
        tvTiempoRestante.setVisibility(android.view.View.GONE);
    }

    private void enviarCodigoVerificacion() {
        String email = etEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Ingresa tu correo electrónico");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Ingresa un correo electrónico válido");
            return;
        }

        currentEmail = email;

        // Deshabilitar botón y iniciar countdown
        btnEnviarCodigo.setEnabled(false);
        btnEnviarCodigo.setAlpha(0.5f);

        // Mostrar progreso
        btnEnviarCodigo.setText("Enviando...");

        presenter.enviarCodigoVerificacion(email);
    }

    private void verificarCodigo() {
        String codigo = etCodigoVerificacion.getText().toString().trim();

        if (TextUtils.isEmpty(codigo)) {
            etCodigoVerificacion.setError("Ingresa el código de verificación");
            return;
        }

        if (codigo.length() != 6) {
            etCodigoVerificacion.setError("El código debe tener 6 dígitos");
            return;
        }

        if (currentEmail == null) {
            Toast.makeText(this, "Primero envía un código de verificación", Toast.LENGTH_SHORT).show();
            return;
        }

        presenter.verificarCodigo(currentEmail, codigo);
    }

    private void cambiarPassword() {
        String nuevaPassword = etNuevaPassword.getText().toString().trim();
        String confirmarPassword = etConfirmarPassword.getText().toString().trim();

        if (TextUtils.isEmpty(nuevaPassword)) {
            etNuevaPassword.setError("Ingresa la nueva contraseña");
            return;
        }

        if (nuevaPassword.length() < 6) {
            etNuevaPassword.setError("La contraseña debe tener al menos 6 caracteres");
            return;
        }

        if (!nuevaPassword.equals(confirmarPassword)) {
            etConfirmarPassword.setError("Las contraseñas no coinciden");
            return;
        }

        if (currentEmail == null) {
            Toast.makeText(this, "Error: email no disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        presenter.cambiarPassword(currentEmail, nuevaPassword);
    }

    private void volverALogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // Métodos públicos para que el Presenter interactúe con la vista
    public void onCodigoEnviado() {
        runOnUiThread(() -> {
            btnEnviarCodigo.setText("Reenviar código");
            btnEnviarCodigo.setEnabled(false);

            // Mostrar sección de código de verificación
            layoutCodigoVerificacion.setVisibility(android.view.View.VISIBLE);
            btnVerificarCodigo.setVisibility(android.view.View.VISIBLE);
            tvTiempoRestante.setVisibility(android.view.View.VISIBLE);

            // Iniciar countdown de 30 segundos para reenvío
            iniciarCountdown();

            Toast.makeText(this, "Código enviado a tu correo", Toast.LENGTH_LONG).show();
        });
    }

    public void onErrorEnvio(String mensaje) {
        runOnUiThread(() -> {
            btnEnviarCodigo.setEnabled(true);
            btnEnviarCodigo.setAlpha(1.0f);
            btnEnviarCodigo.setText("Enviar código");
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
        });
    }

    public void onCodigoVerificado() {
        runOnUiThread(() -> {
            // Mostrar sección de nueva contraseña
            layoutNuevaPassword.setVisibility(android.view.View.VISIBLE);
            layoutConfirmarPassword.setVisibility(android.view.View.VISIBLE);
            btnCambiarPassword.setVisibility(android.view.View.VISIBLE);

            // Ocultar sección de código
            layoutCodigoVerificacion.setVisibility(android.view.View.GONE);
            btnVerificarCodigo.setVisibility(android.view.View.GONE);
            tvTiempoRestante.setVisibility(android.view.View.GONE);

            // Cancelar countdown
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }

            // Habilitar botón de reenvío por si necesita otro código
            btnEnviarCodigo.setEnabled(true);
            btnEnviarCodigo.setAlpha(1.0f);

            Toast.makeText(this, "Código verificado correctamente", Toast.LENGTH_SHORT).show();
        });
    }

    public void onErrorVerificacion(String mensaje) {
        runOnUiThread(() -> {
            etCodigoVerificacion.setError(mensaje);
        });
    }

    public void onPasswordCambiada() {
        runOnUiThread(() -> {
            Toast.makeText(this, "Contraseña cambiada exitosamente", Toast.LENGTH_LONG).show();

            // Volver al login después de un breve delay
            new android.os.Handler().postDelayed(() -> {
                volverALogin();
            }, 1500);
        });
    }

    public void onErrorCambioPassword(String mensaje) {
        runOnUiThread(() -> {
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
        });
    }

    private void iniciarCountdown() {
        countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                long segundos = millisUntilFinished / 1000;
                tvTiempoRestante.setText("Puedes reenviar el código en " + segundos + " segundos");
            }

            public void onFinish() {
                btnEnviarCodigo.setEnabled(true);
                btnEnviarCodigo.setAlpha(1.0f);
                tvTiempoRestante.setText("Puedes reenviar el código ahora");
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}