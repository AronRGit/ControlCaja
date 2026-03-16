package com.controlcaja.model;

import java.math.BigDecimal;

public enum DiferenciaCaja {
    CUADRADO,
    SOBRANTE,
    FALTANTE;

    public static DiferenciaCaja evaluar(BigDecimal totalSistema,
                                         BigDecimal totalFisico) {
        int comparacion = totalFisico.compareTo(totalSistema);
        if (comparacion == 0) return CUADRADO;
        if (comparacion  > 0) return SOBRANTE;
        return FALTANTE;
    }
}
