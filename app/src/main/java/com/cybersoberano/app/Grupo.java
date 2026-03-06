package com.cybersoberano.app;

/* * Desenvolvido por Leandro | CyberSoberano
 * Modelo de dados para o Firebase
 */
public class Grupo {
    private String nome;
    private String link;
    private String imagem;
    private String categoria;
    private String donoId;
    private String idFirebase;
    private long lastBump; // Novo campo para controle de impulso [cite: 2026-03-02]

    // Construtor vazio necessário para o Firebase
    public Grupo() {
    }

    public Grupo(String nome, String link, String imagem, String categoria, String donoId) {
        this.nome = nome;
        this.link = link;
        this.imagem = imagem;
        this.categoria = categoria;
        this.donoId = donoId;
        this.lastBump = System.currentTimeMillis(); // Inicia com o tempo atual [cite: 2026-03-02]
    }

    // Getters e Setters Completos [cite: 2026-01-28]
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public String getImagem() { return imagem; }
    public void setImagem(String imagem) { this.imagem = imagem; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getDonoId() { return donoId; }
    public void setDonoId(String donoId) { this.donoId = donoId; }

    public String getIdFirebase() { return idFirebase; }
    public void setIdFirebase(String idFirebase) { this.idFirebase = idFirebase; }

    public long getLastBump() { return lastBump; }
    public void setLastBump(long lastBump) { this.lastBump = lastBump; }
}
// Créditos: dev Leandro - CyberSoberano [cite: 2026-01-31]