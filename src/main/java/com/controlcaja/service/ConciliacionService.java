package com.controlcaja.service;

import com.controlcaja.model.*;
import com.controlcaja.repository.MovimientoRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConciliacionService {

    private final MovimientoRepository repository;

    public ConciliacionService(MovimientoRepository repository) {
        this.repository = repository;
    }

    public ResultadoConciliacion conciliar(List<Billete> arqueo, String cajero) {
        List<MovimientoCaja> movimientos = repository.buscarPorFecha(LocalDate.now());

        if (movimientos.isEmpty()) {
            throw new IllegalStateException("No hay movimientos registrados hoy");
        }

        // Suma de ingresos descontando comision de cada metodo
        BigDecimal totalIngresos = movimientos.stream()
                .filter(m -> m.tipo() == TipoMovimiento.INGRESO)
                .map(MovimientoCaja::montoNeto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Suma de todos los egresos del dia
        BigDecimal totalEgresos = movimientos.stream()
                .filter(m -> m.tipo() == TipoMovimiento.EGRESO)
                .map(MovimientoCaja::monto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalSistema = totalIngresos.subtract(totalEgresos)
                .setScale(2, RoundingMode.HALF_UP);

        // Total del arqueo fisico: suma todos los subtotales de billetes y monedas
        BigDecimal totalFisico = arqueo.stream()
                .map(Billete::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        // Positivo = sobrante, negativo = faltante
        BigDecimal diferencia = totalFisico.subtract(totalSistema)
                .setScale(2, RoundingMode.HALF_UP);

        DiferenciaCaja estado = DiferenciaCaja.evaluar(totalSistema, totalFisico);

        // Agrupamos ventas por método de pago para el desglose del reporte
        Map<MetodoPago, BigDecimal> resumenPorMetodo = movimientos.stream()
                .filter(m -> m.tipo() == TipoMovimiento.INGRESO)
                .collect(Collectors.groupingBy(
                        MovimientoCaja::metodoPago,
                        Collectors.mapping(
                                MovimientoCaja::montoNeto,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));

        return new ResultadoConciliacion(
                LocalDate.now(),
                cajero,
                totalSistema,
                totalFisico,
                diferencia,
                estado,
                resumenPorMetodo,
                generarObservaciones(estado, diferencia, resumenPorMetodo)
        );
    }

    private List<String> generarObservaciones(DiferenciaCaja estado,
                                              BigDecimal diferencia,
                                              Map<MetodoPago, BigDecimal> resumen) {
        List<String> observaciones = new ArrayList<>();

        switch (estado) {
            case FALTANTE -> {
                observaciones.add("Faltante de S/ " + diferencia.abs() + " — revisar vueltos del día");
                if (resumen.containsKey(MetodoPago.EFECTIVO)) {
                    observaciones.add("El efectivo es el canal con mayor riesgo de error en vueltos");
                }
            }
            case SOBRANTE ->
                    observaciones.add("Sobrante de S/ " + diferencia + " — posible venta no registrada");
            case CUADRADO ->
                    observaciones.add("Caja cuadrada ✅");
        }

        // Si hubo ventas con POS, indicamos que la comisión ya fue descontada
        BigDecimal totalPOS = resumen.getOrDefault(MetodoPago.POS, BigDecimal.ZERO);
        if (totalPOS.compareTo(BigDecimal.ZERO) > 0) {
            observaciones.add("Ventas POS: S/ " + totalPOS + " (comisión 3.5% ya descontada)");
        }

        return observaciones;
    }
}