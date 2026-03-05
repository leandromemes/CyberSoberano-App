package com.cybersoberano.app;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ListaGruposActivity extends AppCompatActivity {

    private RecyclerView recyclerGrupos;
    private String categoriaSelecionada;
    private List<Grupo> listaGrupos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_grupos);

        // Pega a categoria selecionada na tela anterior
        categoriaSelecionada = getIntent().getStringExtra("CATEGORIA_SELECIONADA");

        // Botão Voltar Padrão [cite: 2026-02-04]
        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());

        TextView txtTitulo = findViewById(R.id.titulo);
        if (categoriaSelecionada != null) {
            txtTitulo.setText("GRUPOS: " + categoriaSelecionada.toUpperCase());
        }

        recyclerGrupos = findViewById(R.id.recyclerGrupos);
        recyclerGrupos.setLayoutManager(new LinearLayoutManager(this));

        carregarDadosDoFirebase();
    }

    private void carregarDadosDoFirebase() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Grupos");

        // Filtra os grupos pela categoria e escuta mudanças em tempo real [cite: 2026-03-02]
        db.orderByChild("categoria").equalTo(categoriaSelecionada)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listaGrupos.clear();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Grupo grupo = postSnapshot.getValue(Grupo.class);
                            if (grupo != null) {
                                listaGrupos.add(grupo);
                            }
                        }

                        // Liga os dados ao Adapter para exibir na tela [cite: 2026-03-02]
                        GrupoAdapter adapter = new GrupoAdapter(listaGrupos);
                        recyclerGrupos.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Ocorreu um erro no banco de dados
                    }
                });
    }
}
// Créditos: dev Leandro - CyberSoberano [cite: 2026-01-31]