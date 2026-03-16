package com.controlcaja;

import com.controlcaja.db.Database;
import com.controlcaja.model.Billete;
import com.controlcaja.model.MetodoPago;
import com.controlcaja.report.CajaReporte;
import com.controlcaja.repository.MovimientoRepositoryImpl;
import com.controlcaja.service.CajaService;
import com.controlcaja.service.ConciliacionService;

import java.math.BigDecimal;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Database.init();

        var repository          = new MovimientoRepositoryImpl();
        var cajaService         = new CajaService(repository);
        var conciliacionService = new ConciliacionService(repository);
        var reporte             = new CajaReporte();

        String cajero = "María Quispe";

        // Registramos las ventas del día
        System.out.println("Registrando ventas del día...\n");

        cajaService.registrarVenta("Venta abarrotes", MetodoPago.EFECTIVO, new BigDecimal("45.50"),  cajero);
        cajaService.registrarVenta("Venta bebidas",   MetodoPago.YAPE,     new BigDecimal("28.00"),  cajero);
        cajaService.registrarVenta("Venta limpieza",  MetodoPago.POS,      new BigDecimal("120.00"), cajero);
        cajaService.registrarVenta("Venta golosinas", MetodoPago.PLIN,     new BigDecimal("11.50"),  cajero);

        // Egresos del día
        cajaService.registrarEgreso("Pago proveedor coca-cola", new BigDecimal("80.00"), cajero);
        cajaService.registrarEgreso("Pago luz del local",       new BigDecimal("45.00"), cajero);

        // Arqueo físico: billetes que el cajero cuenta al cerrar
        List<Billete> arqueo = List.of(
                new Billete(new BigDecimal("50"),   1),  // 50.00
                new Billete(new BigDecimal("20"),   1),  // 20.00
                new Billete(new BigDecimal("5"),    1),  //  5.00
                new Billete(new BigDecimal("0.20"), 4)   //  2.00
        );
        // Cambia las cantidades según lo que quieras demostrar

        var resultado = conciliacionService.conciliar(arqueo, cajero);
        reporte.imprimir(resultado);

        Database.close();
    }
}