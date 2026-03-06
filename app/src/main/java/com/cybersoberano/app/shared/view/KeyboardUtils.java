package com.cybersoberano.app.shared.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.inputmethodservice.InputMethodService;
import android.os.Build;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.view.WindowInsetsCompat;

import com.cybersoberano.app.shared.logger.Logger;

/**
 * Utilitários para controle do teclado virtual (IME) no CyberSoberano.
 */
public class KeyboardUtils {

    private static final String LOG_TAG = "KeyboardUtils";

    /** Alterna a visibilidade com um pequeno atraso para garantir a renderização. */
    public static void setSoftKeyboardVisibility(@NonNull final Runnable showSoftKeyboardRunnable, final Activity activity, final View view, final boolean visible) {
        if (visible) {
            view.postDelayed(showSoftKeyboardRunnable, 500);
        } else {
            view.removeCallbacks(showSoftKeyboardRunnable);
            hideSoftKeyboard(activity, view);
        }
    }

    /** Alterna o estado do teclado (Inverte Atual). */
    public static void toggleSoftKeyboard(final Context context) {
        if (context == null) return;
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /** Força a exibição do teclado. */
    public static void showSoftKeyboard(final Context context, final View view) {
        if (context == null || view == null) return;
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.showSoftInput(view, 0);
    }

    /** Esconde o teclado virtual. */
    public static void hideSoftKeyboard(final Context context, final View view) {
        if (context == null || view == null) return;
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /** Desativa o teclado e define as flags da janela para ignorar o IME. */
    public static void disableSoftKeyboard(final Activity activity, final View view) {
        if (activity == null || view == null) return;
        hideSoftKeyboard(activity, view);
        setDisableSoftKeyboardFlags(activity);
    }

    public static void setDisableSoftKeyboardFlags(final Activity activity) {
        if (activity != null && activity.getWindow() != null)
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM, WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    public static void clearDisableSoftKeyboardFlags(final Activity activity) {
        if (activity != null && activity.getWindow() != null)
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    /** Verifica se o teclado está visível (Requer Android 6+). */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isSoftKeyboardVisible(final Activity activity) {
        if (activity != null && activity.getWindow() != null) {
            WindowInsets insets = activity.getWindow().getDecorView().getRootWindowInsets();
            if (insets != null) {
                WindowInsetsCompat insetsCompat = WindowInsetsCompat.toWindowInsetsCompat(insets);
                if (insetsCompat.isVisible(WindowInsetsCompat.Type.ime())) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Detecta se um teclado físico está conectado. */
    public static boolean isHardKeyboardConnected(final Context context) {
        if (context == null) return false;
        Configuration config = context.getResources().getConfiguration();
        return config.keyboard != Configuration.KEYBOARD_NOKEYS
                || config.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO;
    }

    /** Lógica para decidir se o teclado virtual deve ser bloqueado (ex: teclado físico conectado). */
    public static boolean shouldSoftKeyboardBeDisabled(final Context context, final boolean isSoftKeyboardEnabled, final boolean isSoftKeyboardEnabledOnlyIfNoHardware) {
        if (!isSoftKeyboardEnabled) {
            return true;
        } else {
            if(isSoftKeyboardEnabledOnlyIfNoHardware) {
                return KeyboardUtils.isHardKeyboardConnected(context);
            } else {
                return false;
            }
        }
    }
}