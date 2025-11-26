package org.example.dao;

import org.example.model.Varinha;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VarinhaDAO extends ConnectionDAO {

    // CREATE
    public boolean insert(Varinha v) {
        connectToDB();
        String sql = "INSERT INTO varinha (nome, tipo_de_madeira, aluno_id) " +
                "VALUES (?, ?, ?)";

        try {
            pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, v.nome);
            pst.setString(2, v.tipoDeMadeira);
            pst.setInt(3, v.alunoId);
            pst.executeUpdate();

            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                v.varinhaId = rs.getInt(1);
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao inserir varinha: " + e.getMessage());
            return false;
        } finally {
            finishConnection();
        }
    }

    // UPDATE
    public boolean update(Varinha v) {
        connectToDB();
        String sql = "UPDATE varinha SET nome=?, tipo_de_madeira=?, aluno_id=? " +
                "WHERE varinha_id=?";

        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, v.nome);
            pst.setString(2, v.tipoDeMadeira);
            pst.setInt(3, v.alunoId);
            pst.setInt(4, v.varinhaId);
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar varinha: " + e.getMessage());
            return false;
        } finally {
            finishConnection();
        }
    }

    // DELETE
    public boolean delete(int id) {
        connectToDB();
        String sql = "DELETE FROM varinha WHERE varinha_id=?";

        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao deletar varinha: " + e.getMessage());
            return false;
        } finally {
            finishConnection();
        }
    }

    // READ 1 – buscar por id
    public Varinha findById(int id) {
        connectToDB();
        String sql = "SELECT varinha_id, nome, tipo_de_madeira, aluno_id " +
                "FROM varinha WHERE varinha_id=?";

        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();

            if (rs.next()) {
                Varinha v = new Varinha();
                v.varinhaId = rs.getInt("varinha_id");
                v.nome = rs.getString("nome");
                v.tipoDeMadeira = rs.getString("tipo_de_madeira");
                v.alunoId = rs.getInt("aluno_id");
                return v;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar varinha: " + e.getMessage());
        } finally {
            finishConnection();
        }

        return null;
    }

    // READ 2 – listar todas
    public List<Varinha> findAll() {
        connectToDB();
        List<Varinha> lista = new ArrayList<>();
        String sql = "SELECT varinha_id, nome, tipo_de_madeira, aluno_id FROM varinha";

        try {
            st = connection.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                Varinha v = new Varinha();
                v.varinhaId = rs.getInt("varinha_id");
                v.nome = rs.getString("nome");
                v.tipoDeMadeira = rs.getString("tipo_de_madeira");
                v.alunoId = rs.getInt("aluno_id");
                lista.add(v);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar varinhas: " + e.getMessage());
        } finally {
            finishConnection();
        }

        return lista;
    }
}
