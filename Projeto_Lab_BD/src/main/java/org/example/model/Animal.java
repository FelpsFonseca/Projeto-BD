package org.example.model;

public class Animal {
    public int animalId;
    public String nomeCompleto;
    public String especie;
    public int alunoId; // FK para aluno.id

    @Override
    public String toString() {
        return "Animal{" +
                "animalId=" + animalId +
                ", nomeCompleto='" + nomeCompleto + '\'' +
                ", especie='" + especie + '\'' +
                ", alunoId=" + alunoId +
                '}';
    }
}
