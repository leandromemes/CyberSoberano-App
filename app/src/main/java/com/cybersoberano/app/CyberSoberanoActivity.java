package com.cybersoberano.app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.cybersoberano.app.R;

/**
 * Activity Principal do CyberSoberano - Versão Limpa.
 */
public class CyberSoberanoActivity extends AppCompatActivity {

    // Referências ao terminal foram removidas para eliminar os erros de compilação

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define o layout da tela.
        // Certifique-se de que o arquivo activity_main.xml não tenha mais o <com.cybersoberano.app.view.TerminalView>
        setContentView(R.layout.activity_main);
    }
}
