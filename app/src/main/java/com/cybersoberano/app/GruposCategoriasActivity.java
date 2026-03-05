package com.cybersoberano.app;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/* * Desenvolvido por Leandro | CyberSoberano
 * Hub de Categorias e Gerenciamento de Grupos
 */
public class GruposCategoriasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupos_categorias);

        // Botão Voltar (Padrão Soberano) [cite: 2026-02-04]
        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());

        // Configuração das Categorias [cite: 2026-03-02]
        setupBotaoCategoria(R.id.btnAmizade, "Amizade");
        setupBotaoCategoria(R.id.btnRomance, "Romance");
        setupBotaoCategoria(R.id.btnZueira, "Zueira");
        setupBotaoCategoria(R.id.btnOutros, "Outros");

        // NOVO: Abrir tela de gerenciamento "Meus Grupos" [cite: 2026-03-02]
        findViewById(R.id.btnMeusGrupos).setOnClickListener(v -> {
            startActivity(new Intent(this, MeusGruposActivity.class));
        });

        // Abrir tela de cadastro de novo grupo
        findViewById(R.id.btnAnunciar).setOnClickListener(v -> {
            startActivity(new Intent(this, AdicionarGrupoActivity.class));
        });
    }

    private void setupBotaoCategoria(int id, String categoria) {
        findViewById(id).setOnClickListener(v -> {
            Intent intent = new Intent(this, ListaGruposActivity.class);
            intent.putExtra("CATEGORIA_SELECIONADA", categoria);
            startActivity(intent);
        });
    }
}
// Créditos: dev Leandro - CyberSoberano [cite: 2026-01-31]