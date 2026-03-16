-- IF NOT EXISTS evita error si las tablas ya existen de una ejecución anterior
CREATE TABLE IF NOT EXISTS movimiento_caja (
    id          VARCHAR(36)   PRIMARY KEY,   -- UUID como texto
    fecha       DATE          NOT NULL,
    descripcion VARCHAR(200)  NOT NULL,
    metodo_pago VARCHAR(20)   NOT NULL,       -- nombre del enum: 'YAPE', 'POS', etc.
    monto       DECIMAL(10,2) NOT NULL,       -- hasta 10 dígitos, 2 decimales
    tipo        VARCHAR(10)   NOT NULL,       -- 'INGRESO' o 'EGRESO'
    cajero      VARCHAR(100)  NOT NULL
);

CREATE TABLE IF NOT EXISTS cierre_caja (
    id            VARCHAR(36)   PRIMARY KEY,
    fecha         DATE          NOT NULL UNIQUE,  -- un cierre por día
    total_sistema DECIMAL(10,2) NOT NULL,
    total_fisico  DECIMAL(10,2) NOT NULL,
    diferencia    DECIMAL(10,2) NOT NULL,
    estado        VARCHAR(20)   NOT NULL,
    cajero        VARCHAR(100)  NOT NULL
);
