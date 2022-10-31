-- DROP SCHEMA dbo;

CREATE SCHEMA dbo;
-- medio_pagos.dbo.aplicaciones definition

-- Drop table

-- DROP TABLE medio_pagos.dbo.aplicaciones;

CREATE TABLE aplicaciones
(
    id     bigint IDENTITY(1,1) NOT NULL,
    nombre varchar(255),
    token  varchar(255),
    CONSTRAINT aplicaciones_PK PRIMARY KEY (id)
);


-- medio_pagos.dbo.medios_pago definition

-- Drop table

-- DROP TABLE medio_pagos.dbo.medios_pago;

CREATE TABLE medios_pago
(
    id          int IDENTITY(1,1) NOT NULL,
    descripcion varchar(50),
    CONSTRAINT medios_pago_PK PRIMARY KEY (id)
);


-- medio_pagos.dbo.pagadores definition

-- Drop table

-- DROP TABLE medio_pagos.dbo.pagadores;

CREATE TABLE pagadores
(
    id                           bigint IDENTITY(1,1) NOT NULL,
    correo                       varchar(255),
    dni                          bigint NULL,
    nombre_apellido_razon_social varchar(255),
    CONSTRAINT pagadores_PK PRIMARY KEY (id)
);


-- medio_pagos.dbo.pagos definition

-- Drop table

-- DROP TABLE medio_pagos.dbo.pagos;

CREATE TABLE pagos
(
    id                        bigint IDENTITY(1,1) NOT NULL,
    back_url                  varchar(255),
    estado_pago               varchar(255),
    fecha_creacion            bigint NULL,
    fecha_estado              bigint NULL,
    fecha_vencimiento         bigint NULL,
    id_aplicacion             varchar(255),
    id_pagador                bigint NULL,
    id_transaccion_aplicacion varchar(255),
    notificado                bit NULL,
    notification_url          varchar(255),
    precio_total              bigint NULL,
    CONSTRAINT PK__pagos PRIMARY KEY (id),
    CONSTRAINT pagos_pagadores_FK FOREIGN KEY (id_pagador) REFERENCES pagadores (id)
);


-- medio_pagos.dbo.productos definition

-- Drop table

-- DROP TABLE medio_pagos.dbo.productos;

CREATE TABLE productos
(
    id              bigint IDENTITY(1,1) NOT NULL,
    cantidad        bigint NULL,
    codigo_sap      varchar(255),
    nombre_producto varchar(255),
    pago            bigint NULL,
    precio_unitario bigint NULL,
    CONSTRAINT PK__productos PRIMARY KEY (id),
    CONSTRAINT productos_pagos_FK FOREIGN KEY (pago) REFERENCES pagos (id)
);


-- medio_pagos.dbo.transacciones definition

-- Drop table

-- DROP TABLE medio_pagos.dbo.transacciones;

CREATE TABLE transacciones
(
    id             bigint IDENTITY(1,1) NOT NULL,
    estado         varchar(255),
    fecha_estado   bigint NULL,
    id_medio_pago  int NULL,
    id_pago        bigint NULL,
    id_transaccion bigint NULL,
    CONSTRAINT PK__transacc__3213E83F0C5C0D0A PRIMARY KEY (id),
    CONSTRAINT transacciones_pagos_FK FOREIGN KEY (id_pago) REFERENCES pagos (id),
    CONSTRAINT transacciones_medios_pago_FK FOREIGN KEY (id_medio_pago) REFERENCES medios_pago (id)
);

INSERT INTO medios_pago (id, descripcion)
VALUES (1, 'Mercado Pago');
INSERT INTO medios_pago (id, descripcion)
VALUES (2, 'Decidir');
