package org.example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe base para todos os DAOs.
 * Aqui fica a lógica de conexão e fechamento com o banco.
 * As outras classes (AlunoDAO, CasaDAO, etc.) HERDAM desta classe.
 */
public abstract class ConnectionDAO {

    // Objetos usados nas consultas
    protected Connection connection;   // Conexão com o banco
    protected PreparedStatement pst;   // Comandos SQL com parâmetros (?)
    protected Statement st;            // Comandos SQL simples
    protected ResultSet rs;            // Resultado das consultas SELECT

    // Dados do banco (ajuste se necessário)
    private final String database = "mydb";              // nome do banco
    private final String user = "appuser";               // usuário
    private final String password = "SenhaForte!123";    // senha
    private final String url =
            "jdbc:mysql://localhost:3306/" + database +
                    "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    /**
     * Abre a conexão com o MySQL.
     * As classes filhas chamam connectToDB() antes de executar o SQL.
     */
    protected void connectToDB() {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco: " + e.getMessage());
        }
    }

    /**
     * Fecha TODOS os recursos abertos (ResultSet, Statement, Connection).
     * As classes filhas chamam finishConnection() no finally.
     */
    protected void finishConnection() {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            System.out.println("Erro ao fechar ResultSet: " + e.getMessage());
        }

        try {
            if (pst != null) pst.close();
        } catch (SQLException e) {
            System.out.println("Erro ao fechar PreparedStatement: " + e.getMessage());
        }

        try {
            if (st != null) st.close();
        } catch (SQLException e) {
            System.out.println("Erro ao fechar Statement: " + e.getMessage());
        }

        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            System.out.println("Erro ao fechar Connection: " + e.getMessage());
        }
    }
}
