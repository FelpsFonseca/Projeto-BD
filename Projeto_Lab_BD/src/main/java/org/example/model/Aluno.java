package org.example.model;

public class Aluno {
    public int id;
    public String nomeCompleto;
    public int idade;
    public boolean trouxaOuPuroSangue; // TINYINT(1)
    public Integer casaId;             // pode ser null
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
