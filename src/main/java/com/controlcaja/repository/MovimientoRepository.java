package com.controlcaja.repository;

import com.controlcaja.model.MovimientoCaja;
import java.time.LocalDate;
import java.util.List;

public interface MovimientoRepository {
    void guardar(MovimientoCaja movimiento);
    List<MovimientoCaja> buscarPorFecha(LocalDate fecha);
}
