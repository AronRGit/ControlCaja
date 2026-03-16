# Control de Caja
 
Sistema de cierre y conciliación de caja diaria para negocios pequeños.
Detecta diferencias entre las ventas registradas y el dinero físico contado al final del día,
desglosando por método de pago (Efectivo, Yape, Plin, POS, Transferencia).
 
Desarrollado en **Java 17 puro** sin frameworks, como parte de mi portafolio backend.
 
## ¿Qué problema resuelve?
 
Un negocio pequeño cierra el día y no sabe si la plata en caja coincide con lo que vendió.
La diferencia puede ser un vuelto mal dado, una venta no registrada o una comisión de POS
que no se descontó. Este sistema concilia ambas fuentes y dice exactamente cuánto sobra o falta.
 
## Tecnologías
 
 Herramienta | Uso |
 Java 17 
 H2 Database | Base de datos embebida, no requiere instalación 
 HikariCP | Pool de conexiones 
 JDBC puro | Acceso a datos sin ORM 
 Maven | Gestión de dependencias 

## Ejemplo de salida
 
══════════════════════════════════════════
       CIERRE DE CAJA — 2025-03-06
       Cajero: María Quispe
══════════════════════════════════════════
 
── Ventas por método de pago ──────────────
  Efectivo        S/ 77.50
  Yape            S/ 28.00
  POS             S/ 115.80
  Plin            S/ 15.50
  Transferencia   S/ 280.00
 
── Conciliación ───────────────────────────
  Total sistema  : S/ 391.80
  Total físico   : S/ 389.50
  Diferencia     : S/ -2.30
  Estado         : FALTANTE
 
── Observaciones ──────────────────────────
  → Faltante de S/ 2.30 — revisar vueltos del día
  → El efectivo es el canal con mayor riesgo de error en vueltos
  → Ventas POS: S/ 115.80 (comisión 3.5% ya descontada)
══════════════════════════════════════════

