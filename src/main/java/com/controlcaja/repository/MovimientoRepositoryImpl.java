package com.controlcaja.repository;

import com.controlcaja.db.Database;
import com.controlcaja.model.MetodoPago;
import com.controlcaja.model.MovimientoCaja;
import com.controlcaja.model.TipoMovimiento;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovimientoRepositoryImpl implements MovimientoRepository {

    @Override
    public void guardar(MovimientoCaja movimiento) {
        String sql = """
                INSERT INTO movimiento_caja
                    (id, fecha, descripcion, metodo_pago, monto, tipo, cajero)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        // try-with-resources cierra la conexión automáticamente
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString    (1, movimiento.id());
            ps.setDate      (2, Date.valueOf(movimiento.fecha()));
            ps.setString    (3, movimiento.descripcion());
            ps.setString    (4, movimiento.metodoPago().name());
            ps.setBigDecimal(5, movimiento.monto());
            ps.setString    (6, movimiento.tipo().name());
            ps.setString    (7, movimiento.cajero());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error guardando movimiento: " + e.getMessage(), e);
        }
    }

    @Override
    public List<MovimientoCaja> buscarPorFecha(LocalDate fecha) {
        String sql = """
                SELECT id, fecha, descripcion, metodo_pago, monto, tipo, cajero
                FROM movimiento_caja
                WHERE fecha = ?
                """;

        List<MovimientoCaja> resultado = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(fecha));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(new MovimientoCaja(
                            rs.getString  ("id"),
                            rs.getDate    ("fecha").toLocalDate(),
                            rs.getString  ("descripcion"),
                            MetodoPago.valueOf(rs.getString("metodo_pago")),
                            rs.getBigDecimal("monto"),
                            TipoMovimiento.valueOf(rs.getString("tipo")),
                            rs.getString  ("cajero")
                    ));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error buscando movimientos: " + e.getMessage(), e);
        }

        return resultado;
    }
}