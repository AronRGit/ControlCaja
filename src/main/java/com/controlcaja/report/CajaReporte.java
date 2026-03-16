package com.controlcaja.report;

import com.controlcaja.model.ResultadoConciliacion;

public class CajaReporte {

    public void imprimir(ResultadoConciliacion resultado) {
        System.out.println("\n══════════════════════════════════════════");
        System.out.println("       CIERRE DE CAJA — " + resultado.fecha());
        System.out.println("       Cajero: " + resultado.cajero());
        System.out.println("══════════════════════════════════════════");

        System.out.println("\n── Ventas por método de pago ──────────────");
        resultado.resumenPorMetodo().forEach((metodo, total) ->
                System.out.printf("  %-15s S/ %,.2f%n", metodo.getNombre(), total));

        System.out.println("\n── Conciliación ───────────────────────────");
        System.out.printf("  Total sistema  : S/ %,.2f%n", resultado.totalSistema());
        System.out.printf("  Total físico   : S/ %,.2f%n", resultado.totalFisico());
        System.out.printf("  Diferencia     : S/ %,.2f%n", resultado.diferencia());
        System.out.printf("  Estado         : %s%n",       resultado.estado());

        System.out.println("\n── Observaciones ──────────────────────────");
        resultado.observaciones().forEach(obs -> System.out.println("  → " + obs));

        System.out.println("══════════════════════════════════════════\n");
    }
}