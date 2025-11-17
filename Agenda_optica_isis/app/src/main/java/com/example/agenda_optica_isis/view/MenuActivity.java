package com.example.agenda_optica_isis.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.agenda_optica_isis.R;
import com.example.agenda_optica_isis.databinding.ActivityMenuBinding;
import com.example.agenda_optica_isis.model.SistemaReservas;
import com.example.agenda_optica_isis.model.Usuario;
import com.example.agenda_optica_isis.utils.AppTourManager;
import com.example.agenda_optica_isis.utils.ModernSnackBar;

public class MenuActivity extends AppCompatActivity {

    private ActivityMenuBinding binding;
    private ActionBarDrawerToggle toggle;
    private Fragment fragmentActivo;
    private String tagActivo;
    private SistemaReservas sistemaReservas;
    private AppTourManager appTourManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sistemaReservas = SistemaReservas.getInstance();

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            setSupportActionBar(binding.toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            binding.toolbar.setTitle("TuAgenda");
            binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
            binding.toolbar.setNavigationIconTint(ContextCompat.getColor(this, R.color.white));
        }

        toggle = new ActionBarDrawerToggle(
                this,
                binding.drawerLayout,
                binding.toolbar,
                R.string.open_drawer,
                R.string.close_drawer
        );
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Inicializar tour manager
        appTourManager = new AppTourManager(this);

        // Actualizar información del usuario en el drawer
        actualizarInformacionUsuario();

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
                cerrarSesion();
            }

            binding.drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Verificar si debe mostrar el tour después de que la UI esté cargada
        new Handler().postDelayed(() -> {
            checkAndShowTour();
        }, 1000);
    }

    // En tu MenuActivity, modifica el método checkAndShowTour:
    private void checkAndShowTour() {
        System.out.println("=== VERIFICANDO SI DEBE MOSTRAR TOUR ===");
        if (AppTourManager.shouldShowTour(this)) {
            System.out.println("=== INICIANDO TOUR DESDE ACTIVITY ===");
            startAppTour();
        } else {
            System.out.println("=== TOUR YA FUE MOSTRADO ===");
        }
    }

    private void startAppTour() {
        appTourManager.setOnTourCompleteListener(new AppTourManager.OnTourCompleteListener() {
            @Override
            public void onTourComplete() {
                // Tour completado
                ModernSnackBar.mostrar(
                        findViewById(android.R.id.content),
                        "¡Tour completado! Ya conoces todas las funciones principales.",
                        ModernSnackBar.SUCCESS
                );
            }
        });

        System.out.println("=== LLAMANDO START TOUR ===");
        appTourManager.startTour();
    }

    private void actualizarInformacionUsuario() {
        // Obtener el header view del NavigationView
        View headerView = binding.navigationView.getHeaderView(0);
        TextView tvNombreUsuario = headerView.findViewById(R.id.tvNombreUsuario);
        TextView tvCorreoUsuario = headerView.findViewById(R.id.tvCorreoUsuario);

        Usuario usuarioActual = sistemaReservas.getUsuarioActual();

        if (usuarioActual != null) {
            tvNombreUsuario.setText(usuarioActual.getNombre());
            tvCorreoUsuario.setText(usuarioActual.getCorreo_electronico());
        } else {
            tvNombreUsuario.setText("Usuario actual");
            tvCorreoUsuario.setText("usuario@ejemplo.com");
        }
    }

    private void cerrarSesion() {
        // Limpiar usuario actual
        sistemaReservas.cerrarSesion();

        Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Actualizar información del usuario cada vez que la actividad se reanude
        actualizarInformacionUsuario();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Limpiar recursos del tour si es necesario
        if (appTourManager != null) {
            // Aquí puedes agregar limpieza si es necesaria
        }
    }

    // ... (los demás métodos se mantienen igual)
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