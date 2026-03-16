package com.controlcaja.service;

import com.controlcaja.model.MetodoPago;
import com.controlcaja.model.MovimientoCaja;
import com.controlcaja.model.TipoMovimiento;
import com.controlcaja.repository.MovimientoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class CajaService {

    private final MovimientoRepository repository;

    public CajaService(MovimientoRepository repository) {
        this.repository = repository;
    }

    public MovimientoCaja registrarVenta(String descripcion,
                                         MetodoPago metodoPago,
                                         BigDecimal monto,
                                         String cajero) {
        var movimiento = new MovimientoCaja(
                UUID.randomUUID().toString(),
                LocalDate.now(),
                descripcion,
                metodoPago,
                monto,
                TipoMovimiento.INGRESO,
                cajero
        );
        repository.guardar(movimiento);
        return movimiento;
    }

    public MovimientoCaja registrarEgreso(String descripcion,
                                          BigDecimal monto,
                                          String cajero) {
        // Los egresos siempre son en efectivo
        var movimiento = new MovimientoCaja(
                UUID.randomUUID().toString(),
                LocalDate.now(),
                descripcion,
                MetodoPago.EFECTIVO,
                monto,
                TipoMovimiento.EGRESO,
                cajero
        );
        repository.guardar(movimiento);
        return movimiento;
    }

    public List<MovimientoCaja> obtenerMovimientosDelDia() {
        return repository.buscarPorFecha(LocalDate.now());
    }
}