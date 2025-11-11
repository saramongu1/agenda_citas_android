package com.example.agenda_optica_isis.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agenda_optica_isis.R;
import com.example.agenda_optica_isis.presenter.PresenterLoginActivity;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {
    private EditText etMail;
    private EditText etPassword;
    private MaterialButton btnLogin;
    private TextView tvForgotPassword;
    private PresenterLoginActivity presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        enlazarVistas();
        btnLogin.setOnClickListener(v -> iniciarSesion());
        tvForgotPassword.setOnClickListener(v -> recuperarContrasena());
    }

    public void enlazarVistas(){
        etMail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
    }

    public void iniciarSesion(){
        presenter = new PresenterLoginActivity(this);
        presenter.iniciarSesion();
    }

    public void recuperarContrasena() {
        presenter = new PresenterLoginActivity(this);
        presenter.recuperarContrasena();
    }

    public String getMailText(){
        return etMail.getText().toString().trim();
    }

    public String getPasswordText(){
        return etPassword.getText().toString().trim();
    }

    public void irAMenu(){
        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    public void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    public void limpiarCampoContrasena() {
        etPassword.setText("");
    }
}