package com.cybersoberano.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

/* * Desenvolvido por Leandro | CyberSoberano
 * Biblioteca de Cursos de Elite
 */
public class CursosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);

        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());

        LinearLayout container = findViewById(R.id.containerCursos);

        // Lista de Cursos e Links
        adicionarBotaoCurso(container, "🛡️ Blue Team, Red Team e Hardware", "https://t.me/FoxHackers/6930");
        adicionarBotaoCurso(container, "🕵️ Pentest Profissional", "https://t.me/FoxHackers/6355");
        adicionarBotaoCurso(container, "📊 Become a SOC Analyst", "https://t.me/FoxHackers/5556");
        adicionarBotaoCurso(container, "🎓 SecVox Academy (Parte 1)", "https://t.me/FoxHackers/5295");
        adicionarBotaoCurso(container, "🎓 SecVox Academy (Parte 2)", "https://t.me/FoxHackers/5523");
        adicionarBotaoCurso(container, "🧨 Metasploit & Exploração", "https://t.me/FoxHackers/5209");
        adicionarBotaoCurso(container, "🤖 Mr. Robot Full Course", "https://t.me/FoxHackers/4969");
        adicionarBotaoCurso(container, "🏫 Universidade Hacker", "https://t.me/FoxHackers/4771");
        adicionarBotaoCurso(container, "📜 CEH Certified Ethical Hacker", "https://t.me/FoxHackers/4347");
        adicionarBotaoCurso(container, "🎖️ CEH Elite Edition", "https://t.me/FoxHackers/4458");
        adicionarBotaoCurso(container, "💻 Especialista JPA-Algaworks", "https://t.me/FoxHackers/3753");
        adicionarBotaoCurso(container, "🔐 Formação Cybersegurança", "https://t.me/FoxHackers/1516");
        adicionarBotaoCurso(container, "📡 WiFi Hacking Enterprise", "https://t.me/FoxHackers/1393");
        adicionarBotaoCurso(container, "🔍 Hacker Investigador", "https://t.me/FoxHackers/1164");
    }

    private void adicionarBotaoCurso(LinearLayout container, String nome, String url) {
        Button btn = new Button(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.match_parent, 150);
        params.setMargins(0, 10, 0, 10);
        btn.setLayoutParams(params);
        btn.setText(nome);
        btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF111111));
        btn.setTextColor(0xFF00FF00);
        btn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        btn.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(i);
        });

        container.addView(btn);
    }
}