package org.example.model;

public class Professor {
    public int id;
    public String nomeCompleto;
    public String materia;
    public int idade;

    @Override
    public String toString() {
        return "Professor{" +
                "id=" + id +
                ", nomeCompleto='" + nomeCompleto + '\'' +
                ", materia='" + materia + '\'' +
                ", idade=" + idade +
                '}';
    }
}
