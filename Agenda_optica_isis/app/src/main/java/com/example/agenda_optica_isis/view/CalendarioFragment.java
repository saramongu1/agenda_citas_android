package com.example.agenda_optica_isis.view;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.agenda_optica_isis.R;
import com.example.agenda_optica_isis.presenter.PresenterCalendarioFragment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalendarioFragment extends Fragment {
    private static final String TAG = "CalendarioFragment";
    private PresenterCalendarioFragment presenter;
    private LinearLayout contenedorHorasDia;
    private LinearLayout contenedorHorasSemana;
    private ScrollView scrollViewDia;
    private ScrollView scrollViewSemana;
    private TextView tvFechaSeleccionada;
    private Spinner spinnerVista;


    private static final int HORA_INICIO = 6;
    private static final int HORA_FIN = 19;
    private static final int ALTURA_BLOQUE_15MIN_DP = 60;
    private static final int BLOQUES_POR_HORA = 4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendario, container, false);
        presenter = new PresenterCalendarioFragment(this);
        enlazarVistas(view);
        configurarSpinner();
        configurarSelectorFecha();
        actualizarVista();


        return view;
    }

    private void enlazarVistas(View view) {
        contenedorHorasDia = view.findViewById(R.id.contenedorHorasDia);
        contenedorHorasSemana = view.findViewById(R.id.contenedorHorasSemana);
        scrollViewDia = view.findViewById(R.id.scrollViewDia);
        scrollViewSemana = view.findViewById(R.id.scrollViewSemana);
        tvFechaSeleccionada = view.findViewById(R.id.tvFechaSeleccionada);
        spinnerVista = view.findViewById(R.id.spinnerVista);
    }

    private void configurarSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.vistas_calendario,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVista.setAdapter(adapter);

        spinnerVista.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String vistaSeleccionada = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "Vista seleccionada del spinner: " + vistaSeleccionada);
                presenter.cambiarVista(vistaSeleccionada);
                actualizarVista();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void configurarSelectorFecha() {
        tvFechaSeleccionada.setOnClickListener(v -> mostrarDatePicker());
        actualizarFechaDisplay();
    }



    private void mostrarDatePicker() {
        LocalDate fechaActual = presenter.getFechaSeleccionada();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {

                    LocalDate nuevaFecha = LocalDate.of(year, month + 1, dayOfMonth);

                    // Formatear en DD-MM-AAAA
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    String fechaFormateada = nuevaFecha.format(formatter);

                    // Mostrar donde corresponda
                    tvFechaSeleccionada.setText(fechaFormateada);  // Cambia tvFecha por tu TextView real

                    presenter.cambiarFecha(nuevaFecha);
                    actualizarVista();
                },
                fechaActual.getYear(),
                fechaActual.getMonthValue() - 1,
                fechaActual.getDayOfMonth()
        );

        datePickerDialog.show();
    }


    private void actualizarFechaDisplay() {
        LocalDate fecha = presenter.getFechaSeleccionada();
        String tipoVista = presenter.getTipoVista();
        Log.d(TAG, "Actualizando display - Tipo: " + tipoVista + ", Fecha: " + fecha);

        if ("SEMANA".equals(tipoVista)) {
            LocalDate inicioSemana = fecha.minusDays(fecha.getDayOfWeek().getValue() - 1);
            LocalDate finSemana = inicioSemana.plusDays(6);

            DateTimeFormatter semanaFormatter = DateTimeFormatter.ofPattern("dd/MM");
            String fechaStr = inicioSemana.format(semanaFormatter) + " - " + finSemana.format(semanaFormatter);
            tvFechaSeleccionada.setText("Semana: " + fechaStr);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            tvFechaSeleccionada.setText("Día: " + fecha.format(formatter));
        }
    }

    private void actualizarVista() {
        String tipoVistaActual = presenter.getTipoVista();
        Log.d(TAG, "Actualizando vista - Tipo: " + tipoVistaActual +
                ", Fecha: " + presenter.getFechaSeleccionada());

        // Limpiar ambos contenedores
        contenedorHorasDia.removeAllViews();
        contenedorHorasSemana.removeAllViews();

        if ("DIA".equals(tipoVistaActual)) {
            scrollViewDia.setVisibility(View.VISIBLE);
            scrollViewSemana.setVisibility(View.GONE);
            Log.d(TAG, "Inicializando vista DÍA");
            inicializarVistaDia();
        } else if ("SEMANA".equals(tipoVistaActual)) {
            scrollViewDia.setVisibility(View.GONE);
            scrollViewSemana.setVisibility(View.VISIBLE);
            Log.d(TAG, "Inicializando vista SEMANA");
            inicializarVistaSemana();
        } else {
            Log.e(TAG, "Tipo de vista desconocido: " + tipoVistaActual);
            // Por defecto mostrar vista DÍA
            scrollViewDia.setVisibility(View.VISIBLE);
            scrollViewSemana.setVisibility(View.GONE);
            inicializarVistaDia();
        }

        presenter.cargarCitas();
    }

    private void inicializarVistaDia() {
        Log.d(TAG, "Creando bloques para vista DÍA - Horas: " + HORA_INICIO + " a " + HORA_FIN);

        for (int hora = HORA_INICIO; hora <= HORA_FIN; hora++) {
            for (int bloque = 0; bloque < BLOQUES_POR_HORA; bloque++) {
                int minutos = bloque * 15;

                LinearLayout filaBloque = new LinearLayout(requireContext());
                filaBloque.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams paramsFila = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dpToPx(ALTURA_BLOQUE_15MIN_DP)
                );
                paramsFila.setMargins(0, 0, 0, 0);
                filaBloque.setLayoutParams(paramsFila);
                filaBloque.setBackgroundColor(Color.WHITE);

                // Columna de hora
                TextView tvHora = new TextView(requireContext());
                LinearLayout.LayoutParams paramsHora = new LinearLayout.LayoutParams(
                        dpToPx(80),
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
                paramsHora.setMargins(0, 0, 0, 0);
                tvHora.setLayoutParams(paramsHora);

                if (bloque == 0) {
                    tvHora.setText(String.format("%02d:00", hora));
                } else {
                    tvHora.setText(String.format("%02d:%02d", hora, minutos));
                }

                tvHora.setGravity(View.TEXT_ALIGNMENT_CENTER);
                tvHora.setTextSize(12);
                tvHora.setBackgroundColor(Color.LTGRAY);
                tvHora.setTextColor(Color.DKGRAY);
                tvHora.setPadding(dpToPx(8), 0, dpToPx(8), 0);
                filaBloque.addView(tvHora);

                // Columna de citas
                LinearLayout columnaCitas = new LinearLayout(requireContext());
                LinearLayout.LayoutParams paramsCitas = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1
                );
                paramsCitas.setMargins(0, 0, 0, 0);
                columnaCitas.setLayoutParams(paramsCitas);
                columnaCitas.setBackgroundColor(Color.WHITE);
                columnaCitas.setOrientation(LinearLayout.VERTICAL);

                // Guardar identificador único del bloque
                String tagBloque = "DIA_" + hora + "_" + String.format("%02d", minutos);
                columnaCitas.setTag(tagBloque);
                Log.d(TAG, "Creando bloque DÍA - Tag: " + tagBloque);

                // Hacer la columna clickeable
                final int horaFinal = hora;
                final int minutosFinal = minutos;
                columnaCitas.setOnClickListener(v -> {
                    LocalTime horaSeleccionada = LocalTime.of(horaFinal, minutosFinal);
                    presenter.onEspacioVacioClick(horaSeleccionada, presenter.getFechaSeleccionada());
                });

                filaBloque.addView(columnaCitas);
                contenedorHorasDia.addView(filaBloque);
            }

            // Línea separadora entre horas
            if (hora < HORA_FIN) {
                View separadorHora = new View(requireContext());
                LinearLayout.LayoutParams paramsSeparador = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dpToPx(1)
                );
                paramsSeparador.setMargins(dpToPx(80), 0, 0, 0);
                separadorHora.setLayoutParams(paramsSeparador);
                separadorHora.setBackgroundColor(Color.LTGRAY);
                contenedorHorasDia.addView(separadorHora);
            }
        }
        Log.d(TAG, "Vista DÍA inicializada - Total hijos: " + contenedorHorasDia.getChildCount());
    }

    private void inicializarVistaSemana() {
        Log.d(TAG, "Creando vista SEMANA");

        // Header con días de la semana
        LinearLayout headerSemana = crearHeaderSemana();
        contenedorHorasSemana.addView(headerSemana);

        // Contenido de la semana
        LinearLayout contenidoSemana = new LinearLayout(requireContext());
        contenidoSemana.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams paramsContenido = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        contenidoSemana.setLayoutParams(paramsContenido);

        // Columna de horas
        LinearLayout columnaHoras = crearColumnaHoras();
        contenidoSemana.addView(columnaHoras);

        // Columnas para cada día de la semana
        List<LocalDate> diasSemana = presenter.getDiasSemana();
        if (diasSemana != null && !diasSemana.isEmpty()) {
            Log.d(TAG, "Creando " + diasSemana.size() + " columnas para días de la semana");
            for (LocalDate dia : diasSemana) {
                LinearLayout columnaDia = crearColumnaDia(dia);
                contenidoSemana.addView(columnaDia);
            }
        } else {
            Log.w(TAG, "Lista de días de la semana es nula o vacía");
            // Crear días por defecto
            LocalDate inicioSemana = presenter.getFechaSeleccionada().minusDays(presenter.getFechaSeleccionada().getDayOfWeek().getValue() - 1);
            for (int i = 0; i < 7; i++) {
                LinearLayout columnaDia = crearColumnaDia(inicioSemana.plusDays(i));
                contenidoSemana.addView(columnaDia);
            }
        }

        contenedorHorasSemana.addView(contenidoSemana);
        Log.d(TAG, "Vista SEMANA inicializada - Total hijos: " + contenedorHorasSemana.getChildCount());
    }

    private LinearLayout crearHeaderSemana() {
        LinearLayout header = new LinearLayout(requireContext());
        header.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams paramsHeader = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dpToPx(40)
        );
        header.setLayoutParams(paramsHeader);
        header.setBackgroundColor(Color.LTGRAY);

        // Espacio para columna de horas
        TextView espacioHoras = new TextView(requireContext());
        LinearLayout.LayoutParams paramsEspacio = new LinearLayout.LayoutParams(
                dpToPx(80),
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        espacioHoras.setLayoutParams(paramsEspacio);
        header.addView(espacioHoras);

        // Días de la semana
        List<LocalDate> diasSemana = presenter.getDiasSemana();
        if (diasSemana != null && !diasSemana.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE\ndd");
            for (LocalDate dia : diasSemana) {
                TextView tvDia = new TextView(requireContext());
                tvDia.setText(dia.format(formatter));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1
                );
                tvDia.setLayoutParams(params);
                tvDia.setGravity(View.TEXT_ALIGNMENT_CENTER);
                tvDia.setTextSize(12);
                tvDia.setBackgroundColor(dia.equals(presenter.getFechaSeleccionada()) ?
                        Color.parseColor("#E3F2FD") : Color.WHITE);

                tvDia.setOnClickListener(v -> {
                    presenter.cambiarFecha(dia);
                    actualizarFechaDisplay();
                    actualizarVista();
                });

                header.addView(tvDia);
            }
        }

        return header;
    }

    private LinearLayout crearColumnaHoras() {
        LinearLayout columnaHoras = new LinearLayout(requireContext());
        columnaHoras.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams paramsColumna = new LinearLayout.LayoutParams(
                dpToPx(80),
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        columnaHoras.setLayoutParams(paramsColumna);

        for (int hora = HORA_INICIO; hora <= HORA_FIN; hora++) {
            for (int bloque = 0; bloque < BLOQUES_POR_HORA; bloque++) {
                TextView tvHora = new TextView(requireContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dpToPx(ALTURA_BLOQUE_15MIN_DP)
                );

                if (bloque == 0) {
                    tvHora.setText(String.format("%02d:00", hora));
                } else {
                    tvHora.setText("");
                }

                tvHora.setLayoutParams(params);
                tvHora.setGravity(View.TEXT_ALIGNMENT_CENTER);
                tvHora.setTextSize(12);
                tvHora.setBackgroundColor(Color.LTGRAY);
                tvHora.setTextColor(Color.DKGRAY);
                tvHora.setPadding(dpToPx(4), 0, dpToPx(4), 0);

                columnaHoras.addView(tvHora);
            }

            // Separador entre horas
            if (hora < HORA_FIN) {
                View separador = new View(requireContext());
                LinearLayout.LayoutParams paramsSeparador = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dpToPx(1)
                );
                separador.setLayoutParams(paramsSeparador);
                separador.setBackgroundColor(Color.LTGRAY);
                columnaHoras.addView(separador);
            }
        }

        return columnaHoras;
    }

    private LinearLayout crearColumnaDia(LocalDate dia) {
        LinearLayout columnaDia = new LinearLayout(requireContext());
        columnaDia.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        );
        columnaDia.setLayoutParams(params);
        columnaDia.setBackgroundColor(Color.WHITE);

        for (int hora = HORA_INICIO; hora <= HORA_FIN; hora++) {
            for (int bloque = 0; bloque < BLOQUES_POR_HORA; bloque++) {
                int minutos = bloque * 15;

                LinearLayout contenedorBloque = new LinearLayout(requireContext());
                contenedorBloque.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams paramsBloque = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dpToPx(ALTURA_BLOQUE_15MIN_DP)
                );
                contenedorBloque.setLayoutParams(paramsBloque);
                contenedorBloque.setBackgroundColor(Color.WHITE);

                // Guardar identificador único del bloque
                String tagBloque = "SEMANA_" + dia.toString() + "_" + hora + "_" + String.format("%02d", minutos);
                contenedorBloque.setTag(tagBloque);
                Log.d(TAG, "Creando bloque SEMANA - Tag: " + tagBloque);

                // Hacer clickeable para agregar citas
                final LocalDate diaFinal = dia;
                final int horaFinal = hora;
                final int minutosFinal = minutos;
                contenedorBloque.setOnClickListener(v -> {
                    LocalTime horaSeleccionada = LocalTime.of(horaFinal, minutosFinal);
                    presenter.onEspacioVacioClick(horaSeleccionada, diaFinal);
                });

                columnaDia.addView(contenedorBloque);
            }

            // Separador entre horas
            if (hora < HORA_FIN) {
                View separador = new View(requireContext());
                LinearLayout.LayoutParams paramsSeparador = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dpToPx(1)
                );
                separador.setLayoutParams(paramsSeparador);
                separador.setBackgroundColor(Color.LTGRAY);
                columnaDia.addView(separador);
            }
        }

        return columnaDia;
    }

    public void mostrarCitas(List<CitaVisual> citas) {
        Log.d(TAG, "Mostrando " + citas.size() + " citas en el horario");
        for (CitaVisual cita : citas) {
            Log.d(TAG, "Cita encontrada - ID: " + cita.getId() +
                    ", Fecha: " + cita.getFecha() +
                    ", Hora: " + cita.getHora() +
                    ", Paciente: " + cita.getNombrePaciente());
        }

        requireActivity().runOnUiThread(() -> {
            limpiarCitas();
            agregarCitasAlHorario(citas);
        });
    }

    private void limpiarCitas() {
        String tipoVista = presenter.getTipoVista();
        Log.d(TAG, "Limpiando citas anteriores - Vista: " + tipoVista);

        if ("DIA".equals(tipoVista)) {
            limpiarCitasVistaDia();
        } else if ("SEMANA".equals(tipoVista)) {
            limpiarCitasVistaSemana();
        }
    }

    private void limpiarCitasVistaDia() {
        int citasLimpiadas = 0;
        for (int i = 0; i < contenedorHorasDia.getChildCount(); i++) {
            View child = contenedorHorasDia.getChildAt(i);
            if (child instanceof LinearLayout) {
                LinearLayout fila = (LinearLayout) child;
                if (fila.getChildCount() > 1) {
                    View columnaCitas = fila.getChildAt(1);
                    if (columnaCitas instanceof LinearLayout) {
                        int antes = ((LinearLayout) columnaCitas).getChildCount();
                        ((LinearLayout) columnaCitas).removeAllViews();
                        citasLimpiadas += antes;
                    }
                }
            }
        }
        Log.d(TAG, "Citas limpiadas en vista DÍA: " + citasLimpiadas);
    }

    private void limpiarCitasVistaSemana() {
        int citasLimpiadas = 0;
        if (contenedorHorasSemana.getChildCount() > 1) {
            View contenidoSemana = contenedorHorasSemana.getChildAt(1);
            if (contenidoSemana instanceof LinearLayout) {
                LinearLayout contenido = (LinearLayout) contenidoSemana;
                for (int i = 1; i < contenido.getChildCount(); i++) {
                    View columnaDia = contenido.getChildAt(i);
                    if (columnaDia instanceof LinearLayout) {
                        citasLimpiadas += limpiarCitasDeColumnaDia((LinearLayout) columnaDia);
                    }
                }
            }
        }
        Log.d(TAG, "Citas limpiadas en vista SEMANA: " + citasLimpiadas);
    }

    private int limpiarCitasDeColumnaDia(LinearLayout columnaDia) {
        int citasLimpiadas = 0;
        for (int i = 0; i < columnaDia.getChildCount(); i++) {
            View child = columnaDia.getChildAt(i);
            if (child instanceof LinearLayout) {
                LinearLayout bloque = (LinearLayout) child;
                int antes = bloque.getChildCount();
                bloque.removeAllViews();
                citasLimpiadas += antes;
            }
        }
        return citasLimpiadas;
    }

    private void agregarCitasAlHorario(List<CitaVisual> citas) {
        String tipoVista = presenter.getTipoVista();
        Log.d(TAG, "Agregando " + citas.size() + " citas al horario - Vista: " + tipoVista);

        if ("DIA".equals(tipoVista)) {
            agregarCitasVistaDia(citas);
        } else if ("SEMANA".equals(tipoVista)) {
            agregarCitasVistaSemana(citas);
        } else {
            Log.e(TAG, "Tipo de vista desconocido en agregarCitasAlHorario: " + tipoVista);
        }
    }

    private void agregarCitasVistaDia(List<CitaVisual> citas) {
        Log.d(TAG, "Agregando citas a vista DÍA");
        int citasAgregadas = 0;
        LocalDate fechaSeleccionada = presenter.getFechaSeleccionada();

        for (CitaVisual cita : citas) {
            if (cita.getFecha().equals(fechaSeleccionada)) {
                boolean agregada = agregarCitaABloqueDia(cita);
                if (agregada) citasAgregadas++;
            } else {
                Log.d(TAG, "Cita omitida - Fecha cita: " + cita.getFecha() +
                        ", Fecha seleccionada: " + fechaSeleccionada);
            }
        }
        Log.d(TAG, "Citas agregadas en vista DÍA: " + citasAgregadas + " de " + citas.size());
    }

    private boolean agregarCitaABloqueDia(CitaVisual cita) {
        int horaCita = cita.getHora().getHour();
        int minutosCita = cita.getHora().getMinute();

        String tagBloqueBuscado = "DIA_" + horaCita + "_" + String.format("%02d", minutosCita);
        Log.d(TAG, "Buscando bloque DÍA para cita - Hora: " + horaCita + ":" +
                String.format("%02d", minutosCita) + ", Tag: " + tagBloqueBuscado);

        for (int i = 0; i < contenedorHorasDia.getChildCount(); i++) {
            View child = contenedorHorasDia.getChildAt(i);
            if (child instanceof LinearLayout) {
                LinearLayout fila = (LinearLayout) child;
                if (fila.getChildCount() > 1) {
                    View columnaCitas = fila.getChildAt(1);
                    if (columnaCitas instanceof LinearLayout) {
                        String tagBloque = (String) columnaCitas.getTag();
                        if (tagBloque != null && tagBloque.equals(tagBloqueBuscado)) {
                            Log.d(TAG, "Bloque DÍA encontrado! Agregando cita...");
                            View bloqueCita = crearBloqueCita(cita);
                            ((LinearLayout) columnaCitas).addView(bloqueCita);
                            return true;
                        }
                    }
                }
            }
        }
        Log.w(TAG, "Bloque DÍA NO encontrado para cita - Hora: " + horaCita + ":" +
                String.format("%02d", minutosCita));
        return false;
    }

    private void agregarCitasVistaSemana(List<CitaVisual> citas) {
        Log.d(TAG, "Agregando citas a vista SEMANA");
        int citasAgregadas = 0;
        for (CitaVisual cita : citas) {
            boolean agregada = agregarCitaABloqueSemana(cita);
            if (agregada) citasAgregadas++;
        }
        Log.d(TAG, "Citas agregadas en vista SEMANA: " + citasAgregadas + " de " + citas.size());
    }

    private boolean agregarCitaABloqueSemana(CitaVisual cita) {
        LocalDate fechaCita = cita.getFecha();
        int horaCita = cita.getHora().getHour();
        int minutosCita = cita.getHora().getMinute();

        String tagBloqueBuscado = "SEMANA_" + fechaCita.toString() + "_" + horaCita + "_" + String.format("%02d", minutosCita);
        Log.d(TAG, "Buscando bloque SEMANA para cita - Fecha: " + fechaCita +
                ", Hora: " + horaCita + ":" + String.format("%02d", minutosCita) + ", Tag: " + tagBloqueBuscado);

        if (contenedorHorasSemana.getChildCount() > 1) {
            View contenidoSemana = contenedorHorasSemana.getChildAt(1);
            if (contenidoSemana instanceof LinearLayout) {
                LinearLayout contenido = (LinearLayout) contenidoSemana;
                for (int i = 1; i < contenido.getChildCount(); i++) {
                    View columnaDia = contenido.getChildAt(i);
                    if (columnaDia instanceof LinearLayout) {
                        LinearLayout columna = (LinearLayout) columnaDia;
                        for (int j = 0; j < columna.getChildCount(); j++) {
                            View bloque = columna.getChildAt(j);
                            if (bloque instanceof LinearLayout) {
                                String tagBloque = (String) bloque.getTag();
                                if (tagBloque != null && tagBloque.equals(tagBloqueBuscado)) {
                                    Log.d(TAG, "Bloque SEMANA encontrado! Agregando cita...");
                                    View bloqueCita = crearBloqueCita(cita);
                                    ((LinearLayout) bloque).addView(bloqueCita);
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        Log.w(TAG, "Bloque SEMANA NO encontrado para cita");
        return false;
    }

    private View crearBloqueCita(CitaVisual cita) {
        View bloque = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_cita_calendario, null);

        TextView tvPaciente = bloque.findViewById(R.id.tvPacienteCitaCalendarAdapter);
        TextView tvOptometra = bloque.findViewById(R.id.tvOptometraCitaCalendarAdapter);
        TextView tvConsultorio = bloque.findViewById(R.id.tvConsultorioCitaCalendarAdapter);
        View estadoView = bloque.findViewById(R.id.estadoCitaView);

        // Guardar el ID de la cita dentro de la vista
        bloque.setTag(cita.getId());

        // --- Configuración visual ---
        String paciente = cita.getNombrePaciente();

        String optometra = cita.getNombreOptometra();

        tvPaciente.setText(paciente);
        tvOptometra.setText("Opt: " + optometra);
        tvConsultorio.setText("Cons: " + cita.getConsultorio());

        tvPaciente.setTextSize(10);
        tvOptometra.setTextSize(8);
        tvConsultorio.setTextSize(8);

        estadoView.setBackgroundColor(obtenerColorEstado(cita.getEstado()));

        bloque.setAlpha(0.95f);

        // --- Evento de clic ---
        bloque.setOnClickListener(v -> {
            Object tag = v.getTag();
            if (tag instanceof Integer) {
                int idCita = (int) tag;
                presenter.onCitaClick(idCita);
            }
        });

        return bloque;
    }


    private int obtenerColorEstado(String estado) {
        return Color.parseColor("#4CAF50");
    }



    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public void mostrarError(String mensaje) {
        Log.e(TAG, "Error: " + mensaje);
        requireActivity().runOnUiThread(() -> {
            android.widget.Toast.makeText(requireContext(), mensaje, android.widget.Toast.LENGTH_SHORT).show();
        });
    }

    public void mostrarDetalleCita(int idCita) {
        Log.d(TAG, "Mostrando detalle de cita ID: " + idCita);
        DetalleCitaFragment detalle = DetalleCitaFragment.newInstance(String.valueOf(idCita));

        if (getActivity() instanceof MenuActivity) {
            ((MenuActivity) getActivity()).replaceFragment(detalle);
        }
    }

    public void crearNuevaCita(LocalTime hora, LocalDate fecha) {
        Log.d(TAG, "Creando nueva cita - Fecha: " + fecha + ", Hora: " + hora);
        if (getActivity() instanceof MenuActivity) {
            MenuActivity activity = (MenuActivity) getActivity();
            activity.guardarFragmentActivo(this, "CALENDARIO");
            activity.mostrarFragmentConDatos(new BuscarPacienteCCFragment(), "BUSCAR_PACIENTE");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume - Recargando citas");
        presenter.cargarCitas();
    }
}