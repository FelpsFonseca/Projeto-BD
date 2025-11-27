package org.example.dao;

import org.example.model.Aluno;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por ACESSAR a tabela ALUNO no banco.
 * Todas as operações de CRUD e JOIN que envolvem aluno ficam aqui.
 */
public class AlunoDAO extends ConnectionDAO {

    // =========================
    // CREATE - INSERT
    // =========================
    public boolean insert(Aluno a) {
        connectToDB();

        String sql = "INSERT INTO aluno " +
                "(nome_completo, idade, trouxa_ou_puro_sangue, casa_casa_id, email) " +
                "VALUES (?, ?, ?, ?, ?)";

        try {
            // RETURN_GENERATED_KEYS permite pegar o id auto_increment gerado
            pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, a.nomeCompleto);
            pst.setInt(2, a.idade);
            pst.setBoolean(3, a.trouxaOuPuroSangue);

            if (a.casaId != null) {
                pst.setInt(4, a.casaId);
            } else {
                pst.setNull(4, Types.INTEGER);
            }

            pst.setString(5, a.email);

            pst.executeUpdate();

            // pega o id gerado pelo AUTO_INCREMENT
            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                a.id = rs.getInt(1);
            }

            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao inserir aluno: " + e.getMessage());
            return false;
        } finally {
            finishConnection();
        }
    }

    // =========================
    // UPDATE
    // =========================
    public boolean update(Aluno a) {
        connectToDB();

        String sql = "UPDATE aluno SET nome_completo=?, idade=?, " +
                "trouxa_ou_puro_sangue=?, casa_casa_id=?, email=? " +
                "WHERE id=?";

        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, a.nomeCompleto);
            pst.setInt(2, a.idade);
            pst.setBoolean(3, a.trouxaOuPuroSangue);

            if (a.casaId != null) {
                pst.setInt(4, a.casaId);
            } else {
                pst.setNull(4, Types.INTEGER);
            }

            pst.setString(5, a.email);
            pst.setInt(6, a.id);

            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar aluno: " + e.getMessage());
            return false;
        } finally {
            finishConnection();
        }
    }

    // =========================
    // DELETE
    // =========================
    public boolean delete(int id) {
        connectToDB();
        String sql = "DELETE FROM aluno WHERE id=?";

        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao deletar aluno: " + e.getMessage());
            return false;
        } finally {
            finishConnection();
        }
    }

    // =========================
    // READ 1 - buscar por ID
    // =========================
    public Aluno findById(int id) {
        connectToDB();
        String sql = "SELECT id, nome_completo, idade, trouxa_ou_puro_sangue, " +
                "casa_casa_id, email FROM aluno WHERE id=?";

        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();

            if (rs.next()) {
                Aluno a = new Aluno();
                a.id = rs.getInt("id");
                a.nomeCompleto = rs.getString("nome_completo");
                a.idade = rs.getInt("idade");
                a.trouxaOuPuroSangue = rs.getBoolean("trouxa_ou_puro_sangue");
                int casa = rs.getInt("casa_casa_id");
                a.casaId = rs.wasNull() ? null : casa;
                a.email = rs.getString("email");
                return a;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar aluno: " + e.getMessage());
        } finally {
            finishConnection();
        }

        return null;
    }

    // =========================
    // READ 2 - listar todos
    // =========================
    public List<Aluno> findAll() {
        connectToDB();
        List<Aluno> lista = new ArrayList<>();

        String sql = "SELECT id, nome_completo, idade, trouxa_ou_puro_sangue, " +
                "casa_casa_id, email FROM aluno";

        try {
            st = connection.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                Aluno a = new Aluno();
                a.id = rs.getInt("id");
                a.nomeCompleto = rs.getString("nome_completo");
                a.idade = rs.getInt("idade");
                a.trouxaOuPuroSangue = rs.getBoolean("trouxa_ou_puro_sangue");
                int casa = rs.getInt("casa_casa_id");
                a.casaId = rs.wasNull() ? null : casa;
                a.email = rs.getString("email");
                lista.add(a);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar alunos: " + e.getMessage());
        } finally {
            finishConnection();
        }

        return lista;
    }

    // =========================
    // JOIN 1: Alunos + Casas (Direto sem tabela intermediária
    // =========================
    public List<String> joinAlunosComCasas() {
        connectToDB();
        List<String> lista = new ArrayList<>();

        String sql = """
            SELECT a.nome_completo, c.nome AS casa
            FROM aluno a
            JOIN casa c ON c.casa_id = a.casa_casa_id;
        """;

        try {
            st = connection.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                String linha = "Aluno = " + rs.getString("nome_completo") +
                        " | Casa = " + rs.getString("casa");
                lista.add(linha);
            }
        } catch (SQLException e) {
            System.out.println("Erro JOIN alunos+casas: " + e.getMessage());
        } finally {
            finishConnection();
        }

        return lista;
    }

    // =========================
    // JOIN 2: Alunos + Animais
    // =========================
    public List<String> joinAlunosComAnimais() {
        connectToDB();
        List<String> lista = new ArrayList<>();

        String sql = """
            SELECT a.nome_completo, an.especie, an.nome_completo AS animal
            FROM aluno a
            JOIN animal an ON an.aluno_id = a.id;
        """;

        try {
            st = connection.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                String linha = rs.getString("nome_completo") +
                        " possui um(a) " + rs.getString("especie") +
                        " chamado(a) " + rs.getString("animal");
                lista.add(linha);
            }
        } catch (SQLException e) {
            System.out.println("Erro JOIN alunos+animais: " + e.getMessage());
        } finally {
            finishConnection();
        }

        return lista;
    }
}
