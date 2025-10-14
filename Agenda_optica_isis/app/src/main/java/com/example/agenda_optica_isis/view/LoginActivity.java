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

public class LoginActivity extends AppCompatActivity {
    private EditText etMail;
    private  EditText etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword;
    private PresenterLoginActivity presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etMail = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        btnLogin.setOnClickListener(v -> iniciarSesion());
    }

    public void iniciarSesion(){
        presenter = new PresenterLoginActivity(this);
        presenter.iniciarSesion();
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



}
