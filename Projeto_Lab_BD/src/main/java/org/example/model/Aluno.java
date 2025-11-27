package org.example.model;

/**
 * Classe que representa a tabela ALUNO no Java.
 * Cada objeto Aluno = 1 linha da tabela aluno no banco.
 */
public class Aluno {
    public int id;
    public String nomeCompleto;
    public int idade;
    public boolean trouxaOuPuroSangue; // true = puro sangue; false = trouxa
    public Integer casaId;              // pode ser null
    public String email;

    @Override
    public String toString() {
        return "Aluno{" +
                "id=" + id +
                ", nome='" + nomeCompleto + '\'' +
                ", idade=" + idade +
                ", sangue=" + (trouxaOuPuroSangue ? "Puro" : "Trouxa") +
                ", casaId=" + casaId +
                ", email='" + email + '\'' +
                '}';
    }
}
