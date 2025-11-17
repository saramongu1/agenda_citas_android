package com.example.agenda_optica_isis.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agenda_optica_isis.R;
import com.example.agenda_optica_isis.presenter.PresenterLoginActivity;
import com.example.agenda_optica_isis.utils.ModernSnackBar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout layoutEmail;
    private TextInputLayout layoutPassword;
    private MaterialButton btnLogin;
    private TextView tvForgotPassword;
    private PresenterLoginActivity presenter;
    private CountDownTimer countDownTimer;
    private boolean isForgotPasswordEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        enlazarVistas();
        btnLogin.setOnClickListener(v -> iniciarSesion());
        tvForgotPassword.setOnClickListener(v -> recuperarContrasena());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public void enlazarVistas(){
        layoutEmail = findViewById(R.id.layoutEmail);
        layoutPassword = findViewById(R.id.layoutPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
    }

    public void iniciarSesion(){
        layoutEmail.setError(null);
        layoutPassword.setError(null);

        // Validaciones básicas
        if (getMailText().isEmpty()) {
            layoutEmail.setError("Campo obligatorio");
            return;
        }

        if (getPasswordText().isEmpty()) {
            layoutPassword.setError("Campo obligatorio");
            return;
        }

        presenter = new PresenterLoginActivity(this);
        presenter.iniciarSesion();
    }

    public void recuperarContrasena() {
        if (!isForgotPasswordEnabled) {
            return;
        }

        // Abrir activity de recuperación de contraseña
        Intent intent = new Intent(LoginActivity.this, RecuperarPasswordActivity.class);
        startActivity(intent);

        // Deshabilitar temporalmente (opcional, ya que cambiamos de activity)
        // disableForgotPasswordFor30Seconds();
    }

    private void disableForgotPasswordFor30Seconds() {
        isForgotPasswordEnabled = false;
        tvForgotPassword.setEnabled(false);
        tvForgotPassword.setAlpha(0.5f);

        countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                tvForgotPassword.setText(getString(R.string.forgot_password_wait, secondsRemaining));
            }

            public void onFinish() {
                enableForgotPassword();
            }
        }.start();
    }

    private void enableForgotPassword() {
        isForgotPasswordEnabled = true;
        tvForgotPassword.setEnabled(true);
        tvForgotPassword.setAlpha(1.0f);
        tvForgotPassword.setText(R.string.forgot_password);
    }

    public String getMailText(){
        return layoutEmail.getEditText().getText().toString().trim();
    }

    public String getPasswordText(){
        return layoutPassword.getEditText().getText().toString().trim();
    }

    public void irAMenu(){
        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    public void mostrarMensaje(String mensaje, int tipoMensaje) {
        View rootView = findViewById(android.R.id.content);
        if (rootView != null) {
            ModernSnackBar.mostrar(rootView, mensaje, tipoMensaje);
        }
    }

    public void limpiarCampoContrasena() {
        layoutPassword.getEditText().setText("");
    }
}