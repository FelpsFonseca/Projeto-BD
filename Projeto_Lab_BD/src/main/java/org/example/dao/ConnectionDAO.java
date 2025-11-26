package org.example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Classe base para todos os DAOs
public abstract class ConnectionDAO {

    // Objetos de integração com o banco
    protected Connection connection;   // Conexão
    protected PreparedStatement pst;   // Comandos SQL parametrizados
    protected Statement st;            // Comandos simples (sem parâmetros)
    protected ResultSet rs;            // Resultado das consultas

    // Dados do banco (ajuste se seu banco for diferente)
    private final String database = "mydb";              // nome do banco
    private final String user = "appuser";               // usuário criado no script
    private final String password = "SenhaForte!123";    // senha do usuário
    private final String url = "jdbc:mysql://localhost:3306/" + database
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";


    // Abre a conexão com o MySQL
    protected void connectToDB() {
        try {
            connection = DriverManager.getConnection(url, user, password);
            // System.out.println("Conectado ao banco!");
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco: " + e.getMessage());
        }
    }

    // Fecha TODOS os recursos usados na conexão
    protected void finishConnection() {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao fechar ResultSet: " + e.getMessage());
        }

        try {
            if (pst != null) {
                pst.close();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao fechar PreparedStatement: " + e.getMessage());
        }

        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao fechar Statement: " + e.getMessage());
        }

        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao fechar Connection: " + e.getMessage());
        }
    }
}
