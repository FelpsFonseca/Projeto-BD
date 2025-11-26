package org.example.dao;

import org.example.model.Casa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CasaDAO extends ConnectionDAO {

    // CREATE
    public boolean insert(Casa c) {
        connectToDB();
        String sql = "INSERT INTO casa (nome, paleta_de_cores) VALUES (?, ?)";

        try {
            pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, c.nome);
            pst.setString(2, c.paletaDeCores);
            pst.executeUpdate();

            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                c.casaId = rs.getInt(1);
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao inserir casa: " + e.getMessage());
            return false;
        } finally {
            finishConnection();
        }
    }

    // UPDATE
    public boolean update(Casa c) {
        connectToDB();
        String sql = "UPDATE casa SET nome=?, paleta_de_cores=? WHERE casa_id=?";

        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, c.nome);
            pst.setString(2, c.paletaDeCores);
            pst.setInt(3, c.casaId);
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar casa: " + e.getMessage());
            return false;
        } finally {
            finishConnection();
        }
    }

    // DELETE
    public boolean delete(int id) {
        connectToDB();
        String sql = "DELETE FROM casa WHERE casa_id=?";

        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao deletar casa: " + e.getMessage());
            return false;
        } finally {
            finishConnection();
        }
    }

    // READ 1 – buscar por id
    public Casa findById(int id) {
        connectToDB();
        String sql = "SELECT casa_id, nome, paleta_de_cores FROM casa WHERE casa_id=?";

        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();

            if (rs.next()) {
                Casa c = new Casa();
                c.casaId = rs.getInt("casa_id");
                c.nome = rs.getString("nome");
                c.paletaDeCores = rs.getString("paleta_de_cores");
                return c;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar casa: " + e.getMessage());
        } finally {
            finishConnection();
        }
        return null;
    }

    // READ 2 – listar todas
    public List<Casa> findAll() {
        connectToDB();
        List<Casa> lista = new ArrayList<>();
        String sql = "SELECT casa_id, nome, paleta_de_cores FROM casa";

        try {
            st = connection.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                Casa c = new Casa();
                c.casaId = rs.getInt("casa_id");
                c.nome = rs.getString("nome");
                c.paletaDeCores = rs.getString("paleta_de_cores");
                lista.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar casas: " + e.getMessage());
        } finally {
            finishConnection();
        }
        return lista;
    }
}
