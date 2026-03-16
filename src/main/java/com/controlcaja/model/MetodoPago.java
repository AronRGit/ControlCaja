package com.controlcaja.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum MetodoPago {

    EFECTIVO     ("Efectivo",      BigDecimal.ZERO),
    YAPE         ("Yape",          BigDecimal.ZERO),
    PLIN         ("Plin",          BigDecimal.ZERO),
    POS          ("POS",           new BigDecimal("0.035")), // 3.5% comisión promedio en Perú
    TRANSFERENCIA("Transferencia", BigDecimal.ZERO);

    private final String nombre;
    private final BigDecimal comision;

    MetodoPago(String nombre, BigDecimal comision) {
        this.nombre   = nombre;
        this.comision = comision;
    }

    public String getNombre() { return nombre; }

    public BigDecimal calcularComision(BigDecimal monto) {
        return monto.multiply(comision)
                .setScale(2, RoundingMode.HALF_UP);
    }

}
