package org.example.model;

public class Casa {
    public int casaId;
    public String nome;
    public String paletaDeCores;

    @Override
    public String toString() {
        return "Casa{" +
                "casaId=" + casaId +
                ", nome='" + nome + '\'' +
                ", paletaDeCores='" + paletaDeCores + '\'' +
                '}';
    }
}
