package org.example.dao;

import org.example.model.Professor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfessorDAO extends ConnectionDAO {

    // CREATE – inserir professor
    public boolean insert(Professor p) {
        connectToDB();
        String sql = "INSERT INTO professor (nome_completo, materia, idade) " +
                "VALUES (?, ?, ?)";

        try {
            pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, p.nomeCompleto);
            pst.setString(2, p.materia);
            pst.setInt(3, p.idade);

            pst.executeUpdate();

            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                p.id = rs.getInt(1);
            }

            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao inserir professor: " + e.getMessage());
            return false;
        } finally {
            finishConnection();
        }
    }

    // UPDATE – atualizar professor
    public boolean update(Professor p) {
        connectToDB();
        String sql = "UPDATE professor SET nome_completo=?, materia=?, idade=? " +
                "WHERE id=?";

        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, p.nomeCompleto);
            pst.setString(2, p.materia);
            pst.setInt(3, p.idade);
            pst.setInt(4, p.id);

            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar professor: " + e.getMessage());
            return false;
        } finally {
            finishConnection();
        }
    }

    // DELETE – remover professor
    public boolean delete(int id) {
        connectToDB();
        String sql = "DELETE FROM professor WHERE id=?";

        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao deletar professor: " + e.getMessage());
            return false;
        } finally {
            finishConnection();
        }
    }

    // READ 1 – buscar professor por id
    public Professor findById(int id) {
        connectToDB();
        String sql = "SELECT id, nome_completo, materia, idade " +
                "FROM professor WHERE id=?";

        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();

            if (rs.next()) {
                Professor p = new Professor();
                p.id = rs.getInt("id");
                p.nomeCompleto = rs.getString("nome_completo");
                p.materia = rs.getString("materia");
                p.idade = rs.getInt("idade");
                return p;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar professor: " + e.getMessage());
        } finally {
            finishConnection();
        }

        return null;
    }

    // READ 2 – listar todos os professores
    public List<Professor> findAll() {
        connectToDB();
        List<Professor> lista = new ArrayList<>();
        String sql = "SELECT id, nome_completo, materia, idade FROM professor";

        try {
            st = connection.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                Professor p = new Professor();
                p.id = rs.getInt("id");
                p.nomeCompleto = rs.getString("nome_completo");
                p.materia = rs.getString("materia");
                p.idade = rs.getInt("idade");
                lista.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar professores: " + e.getMessage());
        } finally {
            finishConnection();
        }

        return lista;
    }

    public List<String> joinProfessoresAlunos() {
        connectToDB();
        List<String> lista = new ArrayList<>();

        String sql = """
        SELECT p.nome_completo AS professor, a.nome_completo AS aluno
        FROM professor p
        JOIN professor_has_aluno pa ON pa.professor_id = p.id
        JOIN aluno a ON a.id = pa.aluno_id;
    """;

        try {
            st = connection.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                lista.add(
                        "Professor " + rs.getString("professor") +
                                " orienta " + rs.getString("aluno")
                );
            }
        } catch (Exception e) {
            System.out.println("Erro JOIN: " + e.getMessage());
        } finally {
            finishConnection();
        }

        return lista;
    }

}
