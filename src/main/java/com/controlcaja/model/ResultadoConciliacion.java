package com.controlcaja.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record ResultadoConciliacion(
        LocalDate                   fecha,
        String                      cajero,
        BigDecimal                  totalSistema,
        BigDecimal                  totalFisico,
        BigDecimal                  diferencia,
        DiferenciaCaja              estado,
        Map<MetodoPago, BigDecimal> resumenPorMetodo,
        List<String>                observaciones
) {}
