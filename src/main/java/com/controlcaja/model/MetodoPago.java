package com.controlcaja.model;

import java.math.BigDecimal;

public enum MetodoPago {

    EFECTIVO     ("Efectivo",      BigDecimal.ZERO),
    YAPE         ("Yape",          BigDecimal.ZERO),
    PLIN         ("Plin",          BigDecimal.ZERO),
    POS          ("POS",           new BigDecimal("0.035")),
    TRANSFERENCIA("Transferencia", BigDecimal.ZERO);

    private final String nombre;
    private final BigDecimal comision;

    MetodoPago(String nombre, BigDecimal comision) {
        this.nombre   = nombre;
        this.comision = comision;
    }

    public String getNombre() { return nombre; }
}
