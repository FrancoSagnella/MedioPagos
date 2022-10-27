/****** Object:  Table [dbo].[aplicaciones]    Script Date: 27/10/2022 12:46:17 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[aplicaciones](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[nombre] [varchar](255) NULL,
	[token] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[medios_pago]    Script Date: 27/10/2022 12:46:17 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[medios_pago](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[descripcion] [varchar](50) NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[pagadores]    Script Date: 27/10/2022 12:46:17 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[pagadores](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[correo] [varchar](255) NULL,
	[dni] [bigint] NULL,
	[nombre_apellido_razon_social] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[pagos]    Script Date: 27/10/2022 12:46:17 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[pagos](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[back_url] [varchar](255) NULL,
	[estado_pago] [varchar](255) NULL,
	[fecha_creacion] [bigint] NULL,
	[fecha_estado] [bigint] NULL,
	[fecha_vencimiento] [bigint] NULL,
	[id_aplicacion] [varchar](255) NULL,
	[id_pagador] [bigint] NULL,
	[id_transaccion_aplicacion] [varchar](255) NULL,
	[notificado] [bit] NULL,
	[notification_url] [varchar](255) NULL,
	[precio_total] [bigint] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[productos]    Script Date: 27/10/2022 12:46:17 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[productos](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[cantidad] [bigint] NULL,
	[codigo_sap] [varchar](255) NULL,
	[nombre_producto] [varchar](255) NULL,
	[pago] [bigint] NULL,
	[precio_unitario] [bigint] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[transacciones]    Script Date: 27/10/2022 12:46:17 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[transacciones](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[estado] [varchar](255) NULL,
	[fecha_estado] [bigint] NULL,
	[id_medio_pago] [bigint] NULL,
	[id_pago] [bigint] NULL,
	[id_transaccion] [bigint] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET IDENTITY_INSERT [dbo].[aplicaciones] ON 

INSERT [dbo].[aplicaciones] ([id], [nombre], [token]) VALUES (1, N'SIE', N'tokenconsumidor')
SET IDENTITY_INSERT [dbo].[aplicaciones] OFF
GO
SET IDENTITY_INSERT [dbo].[medios_pago] ON 

INSERT [dbo].[medios_pago] ([id], [descripcion]) VALUES (1, N'Mercado Pago')
INSERT [dbo].[medios_pago] ([id], [descripcion]) VALUES (2, N'Decidir')
SET IDENTITY_INSERT [dbo].[medios_pago] OFF
GO
SET IDENTITY_INSERT [dbo].[pagadores] ON 

INSERT [dbo].[pagadores] ([id], [correo], [dni], [nombre_apellido_razon_social]) VALUES (1, N'fransagn@gmail.com', 4425572, N'Franco Sagnella')
SET IDENTITY_INSERT [dbo].[pagadores] OFF
GO
SET IDENTITY_INSERT [dbo].[pagos] ON 

INSERT [dbo].[pagos] ([id], [back_url], [estado_pago], [fecha_creacion], [fecha_estado], [fecha_vencimiento], [id_aplicacion], [id_pagador], [id_transaccion_aplicacion], [notificado], [notification_url], [precio_total]) VALUES (1, N'https://www.google.com.ar/', N'approved', 1666884730968, 1666884798847, 1666885330968, N'tokenconsumidor', 1, N'asdqwdsad', 1, N'https://medio-pagos.herokuapp.com/api/pagos/MercadoPago/respuesta', 7000)
SET IDENTITY_INSERT [dbo].[pagos] OFF
GO
SET IDENTITY_INSERT [dbo].[productos] ON 

INSERT [dbo].[productos] ([id], [cantidad], [codigo_sap], [nombre_producto], [pago], [precio_unitario]) VALUES (1, 3, NULL, N'Carta Documento', 1, 1000)
INSERT [dbo].[productos] ([id], [cantidad], [codigo_sap], [nombre_producto], [pago], [precio_unitario]) VALUES (2, 1, NULL, N'Telegrama Simple', 1, 4000)
SET IDENTITY_INSERT [dbo].[productos] OFF
GO
SET IDENTITY_INSERT [dbo].[transacciones] ON 

INSERT [dbo].[transacciones] ([id], [estado], [fecha_estado], [id_medio_pago], [id_pago], [id_transaccion]) VALUES (1, N'approved', 1666884798847, 2, 1, 12938596)
SET IDENTITY_INSERT [dbo].[transacciones] OFF
GO
USE [master]
GO
ALTER DATABASE [medio_pagos] SET  READ_WRITE 
GO
