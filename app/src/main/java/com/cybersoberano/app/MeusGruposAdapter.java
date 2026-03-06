package com.cybersoberano.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;
import java.util.Locale;

/* * Desenvolvido por Leandro | CyberSoberano
 * Adaptador com Cronômetro e Trava de Impulso [cite: 2026-01-28]
 */
public class MeusGruposAdapter extends RecyclerView.Adapter<MeusGruposAdapter.MyViewHolder> {

    private List<Grupo> lista;
    private Context context;

    public MeusGruposAdapter(List<Grupo> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_grupo, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Grupo grupo = lista.get(position);

        holder.nome.setText(grupo.getNome());
        Glide.with(context).load(grupo.getImagem()).into(holder.foto);

        // Ativa layout de gerenciamento [cite: 2026-03-02]
        holder.layoutImpulso.setVisibility(View.VISIBLE);
        holder.btnAcao.setText("EXCLUIR");
        holder.btnAcao.setBackgroundColor(android.graphics.Color.RED);

        // Inicia a verificação de tempo imediatamente [cite: 2026-03-02]
        verificarTempoImpulso(holder, grupo);

        holder.btnImpulso.setOnClickListener(v -> {
            executarImpulsoComAnimacao(grupo, holder);
        });

        holder.btnAcao.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("🗑️ REMOVER")
                    .setMessage("Apagar grupo: " + grupo.getNome() + "?")
                    .setPositiveButton("SIM", (dialog, which) -> {
                        if (context instanceof MeusGruposActivity) {
                            ((MeusGruposActivity) context).deletarGrupo(grupo.getIdFirebase());
                        }
                    })
                    .setNegativeButton("NÃO", null).show();
        });
    }

    private void verificarTempoImpulso(MyViewHolder holder, Grupo grupo) {
        long agora = System.currentTimeMillis();
        long tempoDoUltimoImpulso = grupo.getLastBump();
        long tempoEspera = 3600000; // 1 hora [cite: 2026-03-02]
        long proximoDisponivel = tempoDoUltimoImpulso + tempoEspera;

        // REMOVIDA A TRAVA DO SOBERANO PARA VOCÊ TESTAR O CRONÔMETRO [cite: 2026-03-02]
        if (agora >= proximoDisponivel) {
            holder.btnImpulso.setEnabled(true);
            holder.btnImpulso.setText("🚀 IMPULSIONAR AGORA");
            holder.txtCronometro.setText("Status: Liberado! ✅");
        } else {
            holder.btnImpulso.setEnabled(false);
            holder.btnImpulso.setText("🚀 AGUARDE...");
            iniciarContagem(holder.txtCronometro, proximoDisponivel - agora, holder.btnImpulso);
        }
    }

    private void iniciarContagem(TextView tv, long millis, Button btn) {
        if (millis <= 0) return;

        new CountDownTimer(millis, 1000) {
            public void onTick(long l) {
                long h = (l / 3600000) % 24;
                long m = (l / 60000) % 60;
                long s = (l / 1000) % 60;
                tv.setText(String.format(Locale.getDefault(), "Disponível em: %02d:%02d:%02d", h, m, s));
            }
            public void onFinish() {
                tv.setText("Status: Liberado! ✅");
                btn.setEnabled(true);
                btn.setText("🚀 IMPULSIONAR AGORA");
            }
        }.start();
    }

    private void executarImpulsoComAnimacao(Grupo grupo, MyViewHolder holder) {
        long tempoAtual = System.currentTimeMillis();

        // Desabilita o botão na hora para evitar múltiplos cliques [cite: 2026-03-02]
        holder.btnImpulso.setEnabled(false);

        holder.rocketAnim.setVisibility(View.VISIBLE);
        holder.rocketAnim.setTranslationY(0f);
        holder.rocketAnim.setAlpha(1f);

        holder.rocketAnim.animate()
                .translationY(-600f)
                .alpha(0f)
                .setDuration(1200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        holder.rocketAnim.setVisibility(View.GONE);
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Grupos");
                        db.child(grupo.getIdFirebase()).child("lastBump").setValue(tempoAtual)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(context, "🚀 GRUPO ENVIADO AO TOPO!", Toast.LENGTH_SHORT).show();
                                    // Notifica que os dados mudaram para reiniciar o cronômetro [cite: 2026-03-02]
                                    grupo.setLastBump(tempoAtual);
                                    notifyDataSetChanged();
                                });
                    }
                });
    }

    @Override
    public int getItemCount() { return lista.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nome, rocketAnim, txtCronometro;
        ImageView foto;
        Button btnAcao, btnImpulso;
        View layoutImpulso;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.txtNomeGrupoItem);
            foto = itemView.findViewById(R.id.imgGrupoItem);
            btnAcao = itemView.findViewById(R.id.btnEntrarGrupoItem);
            btnImpulso = itemView.findViewById(R.id.btnImpulsionar);
            txtCronometro = itemView.findViewById(R.id.txtCronometro);
            layoutImpulso = itemView.findViewById(R.id.layoutImpulso);

            rocketAnim = new TextView(itemView.getContext());
            rocketAnim.setText("🚀");
            rocketAnim.setTextSize(50);
            rocketAnim.setVisibility(View.GONE);
            ((ViewGroup) itemView).addView(rocketAnim);
        }
    }
}
// Créditos: dev Leandro - CyberSoberano [cite: 2026-01-31]