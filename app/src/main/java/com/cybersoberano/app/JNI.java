package com.cybersoberano.app;

/**
 * Métodos nativos para criar e gerenciar subprocessos de pseudoterminal.
 * O código C original está em jni/termux.c.
 * Créditos: dev Leandro - CyberSoberano
 */
public final class JNI {

    static {
        // Carrega a biblioteca nativa 'termux' que você compilou do termux.c
        System.loadLibrary("termux");
    }

    /**
     * Cria um subprocesso. Difere do ProcessBuilder por usar um pseudoterminal (PTY)
     * para se comunicar com o subprocesso, evitando erros de permissão.
     * * @param cmd       O comando para executar (ex: node)
     * @param cwd       O diretório de trabalho atual
     * @param args      Array de argumentos do comando
     * @param envVars   Array de strings "VAR=valor" para o ambiente
     * @param processId Array de um elemento onde o ID do processo será gravado
     * @return O descritor de arquivo do dispositivo mestre /dev/ptmx
     */
    public static native int createSubprocess(
            String cmd,
            String cwd,
            String[] args,
            String[] envVars,
            int[] processId,
            int rows,
            int columns,
            int cellWidth,
            int cellHeight
    );

    /** Define o tamanho da janela para um pty, permitindo que o Node saiba o tamanho da tela. */
    public static native void setPtyWindowSize(int fd, int rows, int cols, int cellWidth, int cellHeight);

    /**
     * Faz a thread atual esperar o processo terminar.
     * @return se >= 0, o status de saída. Se < 0, o sinal que causou a parada.
     */
    public static native int waitFor(int processId);

    /** Fecha um descritor de arquivo através da chamada de sistema close(2). */
    public static native void close(int fileDescriptor);
}