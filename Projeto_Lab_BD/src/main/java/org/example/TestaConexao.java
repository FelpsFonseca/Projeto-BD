package org.example;

import org.example.dao.ConnectionDAO;

// classe simples só para testar a conexão
public class TestaConexao extends ConnectionDAO {

    public static void main(String[] args) {
        TestaConexao t = new TestaConexao();
        t.testar();
    }

    public void testar() {
        System.out.println("Tentando conectar ao banco...");
        connectToDB();

        if (connection != null) {
            System.out.println("Conexão estabelecida com sucesso! ✅");
        } else {
            System.out.println("Falha na conexão. ❌");
        }

        // fecha tudo
        finishConnection();
    }
}
