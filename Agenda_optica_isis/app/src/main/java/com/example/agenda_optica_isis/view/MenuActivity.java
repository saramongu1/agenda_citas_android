package com.example.agenda_optica_isis.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.agenda_optica_isis.R;
import com.example.agenda_optica_isis.databinding.ActivityMenuBinding;

public class MenuActivity extends AppCompatActivity {

    private ActivityMenuBinding binding;
    private ActionBarDrawerToggle toggle;
    private Fragment fragmentActivo;
    private String tagActivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        toggle = new ActionBarDrawerToggle(
                this,
                binding.drawerLayout,
                binding.toolbar,
                R.string.open_drawer,
                R.string.close_drawer
        );
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        replaceFragment(new CalendarioFragment(), "CALENDARIO");

        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.btnCalendario) {
                replaceFragment(new CalendarioFragment(), "CALENDARIO");
            } else if (id == R.id.btnAgenda) {
                replaceFragment(new AgendaFragment(), "AGENDA");
            } else if (id == R.id.btnPacientes) {
                replaceFragment(new PacientesFragment(), "PACIENTES");
            } else if (id == R.id.btnOptometras) {
                replaceFragment(new OptometrasFragment(), "OPTOMETRAS");
            }
            return true;
        });

        binding.btnAgregarCita.setOnClickListener(v -> replaceFragment(new AgregarCitaFragment(), "AGREGAR_CITA"));

        binding.navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_configuracion) {
                replaceFragment(new ConfiguracionFragment(), "CONFIGURACION");
            } else if (id == R.id.nav_perfil) {
                replaceFragment(new UsuarioFragment(), "USUARIO");
            } else if (id == R.id.nav_consultorio) {
                replaceFragment(new ConsultoriosFragment(), "CONSULTORIO");
            } else if (id == R.id.nav_logout) {
                Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            binding.drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    public void replaceFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();

        fragmentActivo = fragment;
        tagActivo = tag;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    public void mostrarFragmentConDatos(Fragment fragment, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        // Siempre reemplaza el fragment para garantizar que los nuevos argumentos se apliquen
        transaction.replace(R.id.frame_layout, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();

        fragmentActivo = fragment;
        tagActivo = tag;
    }

    public void guardarFragmentActivo(Fragment fragment, String tag) {
        this.fragmentActivo = fragment;
        this.tagActivo = tag;
    }

    public Fragment getFragmentActivo() {
        return fragmentActivo;
    }

    public String getTagActivo() {
        return tagActivo;
    }
}
