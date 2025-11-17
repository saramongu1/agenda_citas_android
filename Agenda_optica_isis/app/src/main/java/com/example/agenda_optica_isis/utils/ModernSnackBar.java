package com.example.agenda_optica_isis.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.agenda_optica_isis.R;
import com.google.android.material.snackbar.Snackbar;
public class ModernSnackBar {

    public static final int INFO = 1;
    public static final int ERROR = 2;
    public static final int SUCCESS = 3;

    public static void mostrar(View parentView, String mensaje, int tipoMensaje) {
        Snackbar snackbar = Snackbar.make(parentView, "", Snackbar.LENGTH_LONG);

        snackbar.setAnchorView(R.id.bottomAppBar);

        View snackbarView = snackbar.getView();
        Context context = parentView.getContext();
        int fondoDrawableRes;
        int iconoRes;

        switch (tipoMensaje) {
            case ERROR:
                fondoDrawableRes = R.drawable.bg_snackbar_error;
                iconoRes = R.drawable.error;
                break;
            case SUCCESS:
                fondoDrawableRes = R.drawable.bg_snackbar_success;
                iconoRes = R.drawable.baseline_done_24;
                break;
            default: // INFO
                fondoDrawableRes = R.drawable.bg_snackbar_info;
                iconoRes = R.drawable.outline_info_24;
                break;
        }

        snackbarView.setPadding(0, 0, 0, 0);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            snackbarView.setBackground(ContextCompat.getDrawable(context, fondoDrawableRes));
        } else {
            snackbarView.setBackgroundDrawable(ContextCompat.getDrawable(context, fondoDrawableRes));
        }

        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        if (textView != null) {
            textView.setTextColor(ContextCompat.getColor(context, android.R.color.white));
            textView.setMaxLines(5);
            textView.setText(mensaje);

            Drawable iconDrawable = ContextCompat.getDrawable(context, iconoRes);
            if (iconDrawable != null) {
                int sizeDp = 20;
                float scale = context.getResources().getDisplayMetrics().density;
                int sizePx = (int) (sizeDp * scale + 0.5f);
                iconDrawable = androidx.core.graphics.drawable.DrawableCompat.wrap(iconDrawable).mutate();
                iconDrawable.setBounds(0, 0, sizePx, sizePx);

                textView.setCompoundDrawablePadding((int) (12 * scale + 0.5f));
                textView.setCompoundDrawables(iconDrawable, null, null, null);
            }
        }

        snackbar.show();
    }
}