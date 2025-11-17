package com.example.agenda_optica_isis.utils;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.agenda_optica_isis.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class AppTourManager {
    private Activity activity;
    private View overlayView;
    private ViewGroup rootView;
    private int currentStep = 0;
    private OnTourCompleteListener completeListener;
    private boolean isTourActive = false;

    // Pasos del tour
    private TourStep[] tourSteps;

    public interface OnTourCompleteListener {
        void onTourComplete();
    }

    public static class TourStep {
        public View targetView;
        public String title;
        public String description;
        public int arrowDirection;

        public TourStep(View targetView, String title, String description, int arrowDirection) {
            this.targetView = targetView;
            this.title = title;
            this.description = description;
            this.arrowDirection = arrowDirection;
        }
    }

    public AppTourManager(Activity activity) {
        this.activity = activity;
        this.rootView = activity.findViewById(android.R.id.content);
        initTourSteps();
    }

    private void initTourSteps() {
        // Obtener referencias a las vistas del menú
        BottomNavigationView bottomNav = activity.findViewById(R.id.bottomNavigationView);
        View fabButton = activity.findViewById(R.id.btnAgregarCita);

        // Definir los pasos del tour según tu menú
        tourSteps = new TourStep[]{
                // Paso 1: Botón Agregar Cita (FAB)
                new TourStep(
                        fabButton,
                        "Agregar Nueva Cita",
                        "Presiona aquí para crear una nueva cita para tus pacientes. Podrás seleccionar optómetra, paciente, fecha y hora.",
                        ArrowDirection.BOTTOM
                ),
                // Paso 2: Calendario
                new TourStep(
                        getBottomNavItemView(bottomNav, R.id.btnCalendario),
                        "Calendario de Citas",
                        "Visualiza todas tus citas organizadas por día. Puedes navegar entre fechas y ver las citas programadas.",
                        ArrowDirection.TOP
                ),
                // Paso 3: Agenda
                new TourStep(
                        getBottomNavItemView(bottomNav, R.id.btnAgenda),
                        "Agenda Semanal",
                        "Consulta tu agenda organizada por semana. Ideal para planificar tu trabajo y ver la disponibilidad.",
                        ArrowDirection.TOP
                ),
                // Paso 4: Pacientes
                new TourStep(
                        getBottomNavItemView(bottomNav, R.id.btnPacientes),
                        "Gestión de Pacientes",
                        "Administra la información de todos tus pacientes. Agrega nuevos, edita existentes y consulta su historial médico.",
                        ArrowDirection.TOP
                ),
                // Paso 5: Optómetras
                new TourStep(
                        getBottomNavItemView(bottomNav, R.id.btnOptometras),
                        "Equipo de Optómetras",
                        "Gestiona el equipo de profesionales. Revisa sus horarios y asigna citas según su disponibilidad.",
                        ArrowDirection.TOP
                )
        };
    }

    // Método auxiliar para obtener la vista de un ítem del BottomNavigation
    private View getBottomNavItemView(BottomNavigationView bottomNav, int itemId) {
        if (bottomNav == null) return null;

        View view = bottomNav.findViewById(itemId);
        if (view != null) {
            return view;
        }

        // Si no encuentra la vista directamente, buscar en los hijos
        for (int i = 0; i < bottomNav.getChildCount(); i++) {
            View child = bottomNav.getChildAt(i);
            if (child instanceof ViewGroup) {
                View foundView = ((ViewGroup) child).findViewById(itemId);
                if (foundView != null) {
                    return foundView;
                }
            }
        }
        return null;
    }

    public void setOnTourCompleteListener(OnTourCompleteListener listener) {
        this.completeListener = listener;
    }

    public void startTour() {
        if (tourSteps.length == 0 || isTourActive) {
            return;
        }

        isTourActive = true;
        System.out.println("=== INICIANDO TOUR ===");

        // Pequeño delay para asegurar que la UI esté completamente cargada
        new android.os.Handler().postDelayed(() -> {
            if (isTourActive) {
                createOverlay();
                showStep(0);
            }
        }, 500);
    }

    private void createOverlay() {
        if (!isTourActive || rootView == null) return;

        System.out.println("=== CREANDO OVERLAY ===");

        // Crear vista de overlay que cubra toda la pantalla EXCEPTO el menú inferior
        overlayView = new View(activity);

        // Convertir 70dp a píxeles
        int bottomNavHeight = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                70,
                activity.getResources().getDisplayMetrics()
        );

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT - bottomNavHeight
        );

        // Posicionar en la parte superior, dejando espacio para el menú inferior
        params.gravity = Gravity.TOP;
        overlayView.setLayoutParams(params);

        // Animación de color de fondo
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(),
                Color.TRANSPARENT, Color.argb(200, 0, 0, 0));
        colorAnimation.setDuration(500);
        colorAnimation.addUpdateListener(animator -> {
            if (overlayView != null && isTourActive) {
                overlayView.setBackgroundColor((int) animator.getAnimatedValue());
            }
        });

        overlayView.setOnClickListener(v -> {
            // No hacer nada al tocar el overlay - previene interacciones accidentales
        });

        rootView.addView(overlayView);
        colorAnimation.start();

        System.out.println("=== OVERLAY AGREGADO ===");
    }

    private void showStep(int stepIndex) {
        if (!isTourActive) {
            completeTour();
            return;
        }

        if (stepIndex >= tourSteps.length) {
            completeTour();
            return;
        }

        currentStep = stepIndex;
        TourStep currentStep = tourSteps[stepIndex];

        System.out.println("=== MOSTRANDO PASO " + stepIndex + " ===");

        // Remover tooltip anterior si existe
        removeCurrentTooltip();

        // Verificar que la vista objetivo existe
        if (currentStep.targetView == null) {
            System.out.println("=== VISTA OBJETIVO NULA, SIGUIENTE PASO ===");
            showStep(stepIndex + 1);
            return;
        }

        // Crear y mostrar tooltip
        createAndShowTooltip(currentStep);
    }

    private void createAndShowTooltip(TourStep step) {
        if (!isTourActive || rootView == null) return;

        System.out.println("=== CREANDO TOOLTIP ===");

        // Inflar layout del tooltip
        LayoutInflater inflater = LayoutInflater.from(activity);
        View tooltipView = inflater.inflate(R.layout.layout_tour_tooltip, rootView, false);

        // Configurar contenido
        TextView titleText = tooltipView.findViewById(R.id.tooltip_title);
        TextView descriptionText = tooltipView.findViewById(R.id.tooltip_description);
        MaterialButton nextButton = tooltipView.findViewById(R.id.btn_next);
        MaterialButton skipButton = tooltipView.findViewById(R.id.btn_skip);
        ImageView arrowImage = tooltipView.findViewById(R.id.tooltip_arrow);
        TextView counterText = tooltipView.findViewById(R.id.tooltip_counter);

        titleText.setText(step.title);
        descriptionText.setText(step.description);
        counterText.setText("Paso " + (currentStep + 1) + " de " + tourSteps.length);

        // Configurar flecha direccional
        setArrowDirection(arrowImage, step.arrowDirection);

        // Configurar botones
        if (currentStep == tourSteps.length - 1) {
            nextButton.setText("Finalizar");
        }

        nextButton.setOnClickListener(v -> {
            if (isTourActive) {
                showStep(currentStep + 1);
            }
        });

        skipButton.setOnClickListener(v -> {
            if (isTourActive) {
                completeTour();
            }
        });

        // Posicionar tooltip FIJO en el centro
        positionTooltip(tooltipView, step);

        // Agregar tooltip al overlay
        rootView.addView(tooltipView);

        // Animación de entrada
        Animation fadeIn = AnimationUtils.loadAnimation(activity, R.anim.fade_in);
        tooltipView.startAnimation(fadeIn);

        // Resaltar vista objetivo (SIN modificar el background)
        highlightTargetView(step.targetView);

        System.out.println("=== TOOLTIP MOSTRADO ===");
    }

    private void setArrowDirection(ImageView arrowImage, int direction) {
        switch (direction) {
            case ArrowDirection.TOP:
                arrowImage.setRotation(0);
                arrowImage.setVisibility(View.VISIBLE);
                break;
            case ArrowDirection.BOTTOM:
                arrowImage.setRotation(180);
                arrowImage.setVisibility(View.VISIBLE);
                break;
            case ArrowDirection.LEFT:
                arrowImage.setRotation(270);
                arrowImage.setVisibility(View.VISIBLE);
                break;
            case ArrowDirection.RIGHT:
                arrowImage.setRotation(90);
                arrowImage.setVisibility(View.VISIBLE);
                break;
            case ArrowDirection.NONE:
                arrowImage.setVisibility(View.GONE);
                break;
        }
    }

    private void positionTooltip(View tooltipView, TourStep step) {
        // Usar FrameLayout.LayoutParams para posicionar FIJO en el centro
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // POSICIÓN FIJA en el centro de la pantalla
        params.gravity = Gravity.CENTER;

        // Ajustes menores según la dirección de la flecha
        switch (step.arrowDirection) {
            case ArrowDirection.BOTTOM:
                // Si la flecha apunta hacia abajo, mover un poco hacia arriba
                params.topMargin = -100;
                break;
            case ArrowDirection.TOP:
                // Si la flecha apunta hacia arriba, mover un poco hacia abajo
                params.topMargin = 100;
                break;
        }

        tooltipView.setLayoutParams(params);

        System.out.println("=== TOOLTIP POSICIONADO EN EL CENTRO ===");
    }

    private void highlightTargetView(View targetView) {
        if (!isTourActive || targetView == null) return;

        // Guardar el background original ANTES de cualquier modificación
        Object originalBackground = targetView.getBackground();
        targetView.setTag(R.id.original_background, originalBackground);

        // Crear efecto de resaltado SOLO con escala, SIN cambiar el background
        ValueAnimator animator = ValueAnimator.ofFloat(1.0f, 1.15f, 1.0f);
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(animation -> {
            if (targetView != null && isTourActive) {
                float scale = (float) animation.getAnimatedValue();
                targetView.setScaleX(scale);
                targetView.setScaleY(scale);

                // NO modificar el background - mantener el original del menú
                // Solo agregar un leve efecto de elevación
                if (scale > 1.1f) {
                    targetView.setElevation(16f);
                } else {
                    targetView.setElevation(8f);
                }
            }
        });
        animator.start();

        // Guardar referencia para restaurar después
        targetView.setTag(R.id.tour_animator, animator);

        System.out.println("=== VISTA RESALTADA (SIN MODIFICAR BACKGROUND) ===");
    }

    private void removeCurrentTooltip() {
        // Remover tooltip actual
        View tooltip = rootView.findViewById(R.id.tour_tooltip_container);
        if (tooltip != null) {
            Animation fadeOut = AnimationUtils.loadAnimation(activity, R.anim.fade_out);
            tooltip.startAnimation(fadeOut);

            new android.os.Handler().postDelayed(() -> {
                if (rootView != null && tooltip.getParent() != null) {
                    rootView.removeView(tooltip);
                }
            }, 300);
        }

        // Restaurar vistas resaltadas - IMPORTANTE: restaurar el background original
        for (TourStep step : tourSteps) {
            if (step.targetView != null) {
                ValueAnimator animator = (ValueAnimator) step.targetView.getTag(R.id.tour_animator);
                if (animator != null) {
                    animator.cancel();
                }

                // Restaurar propiedades originales
                step.targetView.setScaleX(1.0f);
                step.targetView.setScaleY(1.0f);
                step.targetView.setElevation(0f);

                // RESTAURAR EL BACKGROUND ORIGINAL
                Object originalBackground = step.targetView.getTag(R.id.original_background);
                if (originalBackground instanceof android.graphics.drawable.Drawable) {
                    step.targetView.setBackground((android.graphics.drawable.Drawable) originalBackground);
                }

                step.targetView.setTag(R.id.tour_animator, null);
                step.targetView.setTag(R.id.original_background, null);
            }
        }
    }

    private void completeTour() {
        if (!isTourActive) return;

        System.out.println("=== COMPLETANDO TOUR ===");
        isTourActive = false;

        // Guardar preferencia de que el tour ya se mostró
        android.content.SharedPreferences prefs = activity.getSharedPreferences("app_prefs", Activity.MODE_PRIVATE);
        prefs.edit().putBoolean("tour_completed", true).apply();

        // Remover tooltips primero
        removeCurrentTooltip();

        // Remover overlay con animación
        if (overlayView != null) {
            ValueAnimator fadeOut = ValueAnimator.ofObject(new ArgbEvaluator(),
                    Color.argb(200, 0, 0, 0), Color.TRANSPARENT);
            fadeOut.setDuration(300);

            final View finalOverlayView = overlayView;
            fadeOut.addUpdateListener(animator -> {
                if (finalOverlayView != null) {
                    finalOverlayView.setBackgroundColor((int) animator.getAnimatedValue());
                }
            });

            fadeOut.start();

            new android.os.Handler().postDelayed(() -> {
                if (rootView != null && finalOverlayView != null && finalOverlayView.getParent() != null) {
                    rootView.removeView(finalOverlayView);
                }
            }, 350);

            overlayView = null;
        }

        // Notificar completado
        if (completeListener != null) {
            completeListener.onTourComplete();
        }

        System.out.println("=== TOUR COMPLETADO ===");
    }

    public static boolean shouldShowTour(Activity activity) {
        android.content.SharedPreferences prefs = activity.getSharedPreferences("app_prefs", Activity.MODE_PRIVATE);
        boolean shouldShow = !prefs.getBoolean("tour_completed", false);
        System.out.println("=== DEBE MOSTRAR TOUR: " + shouldShow + " ===");
        return shouldShow;
    }

    // Clase para direcciones de flecha
    public static class ArrowDirection {
        public static final int TOP = 0;
        public static final int BOTTOM = 1;
        public static final int LEFT = 2;
        public static final int RIGHT = 3;
        public static final int NONE = 4;
    }
}