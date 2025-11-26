package org.example.model;

public class Varinha {
    public int varinhaId;
    public String nome;
    public String tipoDeMadeira;
    public int alunoId;  // FK para aluno.id

    @Override
    public String toString() {
        return "Varinha{" +
                "varinhaId=" + varinhaId +
                ", nome='" + nome + '\'' +
                ", tipoDeMadeira='" + tipoDeMadeira + '\'' +
                ", alunoId=" + alunoId +
                '}';
    }
}
