package br.com.alura.screenmatch.modelos;

import br.com.alura.screenmatch.exceptions.ErroDeConversaoDeAnoException;
import com.google.gson.annotations.SerializedName;

public class Titulo implements Comparable<Titulo> {
    //@SerializedName("Title")
    private String nome;
    //@SerializedName("Year")
    private int anoDeLancamento;
    private boolean incluidoNoPlano;
    private double somaDasAvaliacoes;
    private int totalDeAvaliacoes;
    private int duracaoEmMinutos;

    public Titulo(String nome, int anoDeLancamento) {
        this.nome = nome;
        this.anoDeLancamento = anoDeLancamento;
    }

    public Titulo(TituloOmdb meuTituloOmdb) throws ErroDeConversaoDeAnoException {
        this.nome = meuTituloOmdb.title();
        if (meuTituloOmdb.year().length() > 4) {
            throw new ErroDeConversaoDeAnoException("Não foi possível converter o ano porque tem mais de 04 caracteres");
        }
        // Podemos usar o REGEX -> this.anoDeLancamento = Integer.parseInt(meuTituloOmdb.year().replaceAll("[^0-9]", ""));
        this.anoDeLancamento = Integer.parseInt(meuTituloOmdb.year());
        this.duracaoEmMinutos = Integer.parseInt(meuTituloOmdb.runtime().substring(0, 3).replaceAll("[a-zA-Z ]", ""));
    }

    public String getNome() {
        return nome;
    }

    public int getAnoDeLancamento() {
        return anoDeLancamento;
    }

    public boolean isIncluidoNoPlano() {
        return incluidoNoPlano;
    }

    public int getDuracaoEmMinutos() {
        return duracaoEmMinutos;
    }

    public int getTotalDeAvaliacoes() {
        return totalDeAvaliacoes;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setAnoDeLancamento(int anoDeLancamento) {
        this.anoDeLancamento = anoDeLancamento;
    }

    public void setIncluidoNoPlano(boolean incluidoNoPlano) {
        this.incluidoNoPlano = incluidoNoPlano;
    }

    public void setDuracaoEmMinutos(int duracaoEmMinutos) {
        this.duracaoEmMinutos = duracaoEmMinutos;
    }

    public void exibeFichaTecnica(){
        System.out.println("Nome do filme: " + nome);
        System.out.println("Ano de lançamento: " + anoDeLancamento);
    }

    public void avalia(double nota){
        somaDasAvaliacoes += nota;
        totalDeAvaliacoes++;
    }

    public double pegaMedia(){
        return somaDasAvaliacoes / totalDeAvaliacoes;
    }

    @Override
    public int compareTo(Titulo outroTitulo) {
        return this.getNome().compareTo(outroTitulo.getNome());
    }

    @Override
    public String toString() {
        return "\n Titulo = " + nome + "\n Ano de Lançamento = " + anoDeLancamento + "\n Duração = " + duracaoEmMinutos + " minutos ";
    }
}
