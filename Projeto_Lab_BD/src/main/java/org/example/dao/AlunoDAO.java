package org.example.dao;

import org.example.model.Aluno;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO extends ConnectionDAO {

    // CREATE – inserir novo aluno
    public boolean insert(Aluno a) {
        connectToDB();
        String sql = "INSERT INTO aluno " +
                "(nome_completo, idade, trouxa_ou_puro_sangue, casa_casa_id, email) " +
                "VALUES (?, ?, ?, ?, ?)";

        try {
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

            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                a.id = rs.getInt(1); // pega o AUTO_INCREMENT gerado
            }

            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao inserir aluno: " + e.getMessage());
            return false;
        } finally {
            finishConnection();
        }
    }

    // UPDATE – atualizar aluno existente
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

    // DELETE – remover aluno pelo id
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

    // READ 1 – buscar aluno por id
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

    // READ 2 – listar todos os alunos
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

    public List<String> joinAlunosComCasas() {
        connectToDB();
        List<String> lista = new ArrayList<>();

        String sql = """
        SELECT a.id, a.nome_completo, c.nome AS casa
        FROM aluno a
        JOIN casa c ON c.casa_id = a.casa_casa_id;
    """;

        try {
            st = connection.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                String linha = "Aluno=" + rs.getString("nome_completo")
                        + ", Casa=" + rs.getString("casa");
                lista.add(linha);
            }
        } catch (Exception e) {
            System.out.println("Erro JOIN: " + e.getMessage());
        } finally {
            finishConnection();
        }

        return lista;
    }

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
                lista.add(
                        rs.getString("nome_completo") +
                                " possui um " +
                                rs.getString("especie") +
                                " chamado " +
                                rs.getString("animal")
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

