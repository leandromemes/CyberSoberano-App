package com.cybersoberano.app;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

/* * Desenvolvido por Leandro | CyberSoberano
 * Adaptador com função de exclusão para o dono do grupo
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
        // Usa o mesmo layout de item, mas vamos tratar o botão de forma diferente [cite: 2026-03-02]
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_grupo, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Grupo grupo = lista.get(position);

        holder.nome.setText(grupo.getNome());

        // Carrega a imagem do grupo [cite: 2026-03-02]
        Glide.with(context).load(grupo.getImagem()).into(holder.foto);

        // Transforma o botão "ENTRAR" em "EXCLUIR" nesta tela [cite: 2026-03-02]
        holder.btnAcao.setText("EXCLUIR");
        holder.btnAcao.setBackgroundColor(android.graphics.Color.RED);
        holder.btnAcao.setTextColor(android.graphics.Color.WHITE);

        holder.btnAcao.setOnClickListener(v -> {
            // Alerta de confirmação para o Soberano [cite: 2026-03-02]
            new AlertDialog.Builder(context)
                    .setTitle("🗑️ REMOVER GRUPO")
                    .setMessage("Tem certeza que deseja apagar o grupo: " + grupo.getNome() + "?")
                    .setPositiveButton("SIM, APAGAR", (dialog, which) -> {
                        // Chama o método de deletar da Activity [cite: 2026-03-02]
                        if (context instanceof MeusGruposActivity) {
                            ((MeusGruposActivity) context).deletarGrupo(grupo.getIdFirebase());
                        }
                    })
                    .setNegativeButton("CANCELAR", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nome;
        ImageView foto;
        Button btnAcao;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.txtNomeGrupoItem);
            foto = itemView.findViewById(R.id.imgGrupoItem);
            btnAcao = itemView.findViewById(R.id.btnEntrarGrupoItem);
        }
    }
}
// Créditos: dev Leandro - CyberSoberano [cite: 2026-01-31]