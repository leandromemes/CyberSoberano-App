package com.cybersoberano.app;

import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
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

public class MeusGruposActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private MeusGruposAdapter adapter;
    private List<Grupo> lista = new ArrayList<>();
    private DatabaseReference db;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_grupos);

        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());
        progress = findViewById(R.id.progressMeusGrupos);
        recycler = findViewById(R.id.recyclerMeusGrupos);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseDatabase.getInstance().getReference("Grupos");
        carregarMeusGrupos();
    }

    private void carregarMeusGrupos() {
        String meuId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        db.orderByChild("donoId").equalTo(meuId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lista.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Grupo g = ds.getValue(Grupo.class);
                    if (g != null) {
                        // O método setIdFirebase agora existe na classe Grupo [cite: 2026-03-02]
                        g.setIdFirebase(ds.getKey());
                        lista.add(g);
                    }
                }
                adapter = new MeusGruposAdapter(lista, MeusGruposActivity.this);
                recycler.setAdapter(adapter);
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MeusGruposActivity.this, "Erro ao carregar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deletarGrupo(String idFirebase) {
        db.child(idFirebase).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Grupo removido com sucesso! 🗑️", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao deletar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
// Créditos: dev Leandro - CyberSoberano [cite: 2026-01-31]