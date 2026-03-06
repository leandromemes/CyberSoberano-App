package com.cybersoberano.app.terminal.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * View customizada para exibir o terminal do CyberSoberano.
 * Essencial para o CursorController e TextSelection.
 */
public final class CyberTerminalView extends View {

    public CyberTerminalView(Context context) {
        super(context);
    }

    public CyberTerminalView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CyberTerminalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // Método placeholder para evitar erros no attach da Activity
    public void attachSession(Object session) {
        // Logica de conexão com a sessão do bot de WhatsApp
    }
}
