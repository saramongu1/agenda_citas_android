package com.example.agenda_optica_isis.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.agenda_optica_isis.R;
import com.example.agenda_optica_isis.presenter.PresenterDetalleCitaFragment;

public class DetalleCitaFragment extends Fragment {

    private static final String ARG_ID = "id_cita";
    private TextView tvId, tvPaciente, tvDocumento, tvOptometra, tvFecha, tvHora, tvEstado, tvConsultorio;
    private Button btnVolver;
    private PresenterDetalleCitaFragment presenter;

    public static DetalleCitaFragment newInstance(int citaId) {
        DetalleCitaFragment f = new DetalleCitaFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_ID, citaId);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalle_cita, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvId = view.findViewById(R.id.tvDetalleId);
        tvPaciente = view.findViewById(R.id.tvDetallePacienteNombre);
        tvDocumento = view.findViewById(R.id.tvDetallePacienteDocumento);
        tvOptometra = view.findViewById(R.id.tvDetalleOptometra);
        tvFecha = view.findViewById(R.id.tvDetalleFecha);
        tvHora = view.findViewById(R.id.tvDetalleHora);
        tvEstado = view.findViewById(R.id.tvDetalleEstado);
        tvConsultorio = view.findViewById(R.id.tvDetalleConsultorio);
        btnVolver = view.findViewById(R.id.btnDetalleVolver);

        presenter = new PresenterDetalleCitaFragment(this);

        int id = getArguments() != null ? getArguments().getInt(ARG_ID, -1) : -1;
        presenter.cargarDetalle(id);

        btnVolver.setOnClickListener(v -> {
            if (getActivity() instanceof MenuActivity) {
                ((MenuActivity) getActivity()).replaceFragment(new AgendaFragment());
            }
        });
    }

    // métodos para rellenar desde el presenter:
    public void mostrarId(String id) { tvId.setText(id); }
    public void mostrarPaciente(String nombre) { tvPaciente.setText(nombre); }
    public void mostrarDocumento(String doc) { tvDocumento.setText(doc); }
    public void mostrarOptometra(String nombre) { tvOptometra.setText(nombre); }
    public void mostrarFecha(String fecha) { tvFecha.setText(fecha); }
    public void mostrarHora(String hora) { tvHora.setText(hora); }
    public void mostrarEstado(String estado) { tvEstado.setText(estado); }
    public void mostrarConsultorio(String c) { tvConsultorio.setText(c); }

    public void mostrarMensaje(String mensaje) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
    }
}
