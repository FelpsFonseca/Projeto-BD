package org.example.dao;

import org.example.model.Animal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnimalDAO extends ConnectionDAO {

    // CREATE
    public boolean insert(Animal an) {
        connectToDB();
        String sql = "INSERT INTO animal (nome_completo, especie, aluno_id) " +
                "VALUES (?, ?, ?)";

        try {
            pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, an.nomeCompleto);
            pst.setString(2, an.especie);
            pst.setInt(3, an.alunoId);
            pst.executeUpdate();

            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                an.animalId = rs.getInt(1);
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao inserir animal: " + e.getMessage());
            return false;
        } finally {
            finishConnection();
        }
    }

    // UPDATE
    public boolean update(Animal an) {
        connectToDB();
        String sql = "UPDATE animal SET nome_completo=?, especie=?, aluno_id=? " +
                "WHERE animal_id=?";

        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, an.nomeCompleto);
            pst.setString(2, an.especie);
            pst.setInt(3, an.alunoId);
            pst.setInt(4, an.animalId);
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar animal: " + e.getMessage());
            return false;
        } finally {
            finishConnection();
        }
    }

    // DELETE
    public boolean delete(int id) {
        connectToDB();
        String sql = "DELETE FROM animal WHERE animal_id=?";

        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao deletar animal: " + e.getMessage());
            return false;
        } finally {
            finishConnection();
        }
    }

    // READ 1 – buscar por id
    public Animal findById(int id) {
        connectToDB();
        String sql = "SELECT animal_id, nome_completo, especie, aluno_id " +
                "FROM animal WHERE animal_id=?";

        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();

            if (rs.next()) {
                Animal an = new Animal();
                an.animalId = rs.getInt("animal_id");
                an.nomeCompleto = rs.getString("nome_completo");
                an.especie = rs.getString("especie");
                an.alunoId = rs.getInt("aluno_id");
                return an;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar animal: " + e.getMessage());
        } finally {
            finishConnection();
        }

        return null;
    }

    // READ 2 – listar todos
    public List<Animal> findAll() {
        connectToDB();
        List<Animal> lista = new ArrayList<>();
        String sql = "SELECT animal_id, nome_completo, especie, aluno_id FROM animal";

        try {
            st = connection.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                Animal an = new Animal();
                an.animalId = rs.getInt("animal_id");
                an.nomeCompleto = rs.getString("nome_completo");
                an.especie = rs.getString("especie");
                an.alunoId = rs.getInt("aluno_id");
                lista.add(an);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar animais: " + e.getMessage());
        } finally {
            finishConnection();
        }

        return lista;
    }
}
