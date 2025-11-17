package com.example.agenda_optica_isis.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.agenda_optica_isis.R;
import com.google.android.material.button.MaterialButton;

public class ConfiguracionFragment extends Fragment {

    private static final String LINK_MANUAL_USUARIO = "https://www.youtube.com/watch?v=H13rI_6FypA&list=RDH13rI_6FypA&start_radio=1";

    public ConfiguracionFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_configuracion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialButton btnManual = view.findViewById(R.id.btnManualUsuario);

        btnManual.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(LINK_MANUAL_USUARIO));
            startActivity(intent);
        });
    }
}