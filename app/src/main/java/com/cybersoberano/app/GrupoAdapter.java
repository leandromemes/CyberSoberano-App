package com.cybersoberano.app;

import android.content.Intent;
import android.net.Uri;
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

public class GrupoAdapter extends RecyclerView.Adapter<GrupoAdapter.MyViewHolder> {

    private List<Grupo> lista;

    public GrupoAdapter(List<Grupo> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_grupo, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Grupo grupo = lista.get(position);
        holder.nome.setText(grupo.getNome());

        // Carrega a imagem do WhatsApp usando Glide [cite: 2026-03-02]
        Glide.with(holder.itemView.getContext()).load(grupo.getImagem()).into(holder.foto);

        holder.btnEntrar.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(grupo.getLink()));
            holder.itemView.getContext().startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nome;
        ImageView foto;
        Button btnEntrar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.txtNomeGrupoItem);
            foto = itemView.findViewById(R.id.imgGrupoItem);
            btnEntrar = itemView.findViewById(R.id.btnEntrarGrupoItem);
        }
    }
}
// Créditos: dev Leandro - CyberSoberano [cite: 2026-01-31]