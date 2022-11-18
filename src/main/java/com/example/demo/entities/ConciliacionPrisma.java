package com.example.demo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="conciliacionPrisma")
public class ConciliacionPrisma {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column
	private String fpres;
	
	@Column
	private String codOp;
	
	@Column
	private Integer lote;
	
	@Column 
	private Integer numTar;

	@Column 
	private String forigCompra;

	@Column 
	private String fpag;
	
	@Column
	private Integer numComp;
	
	@Column
	private Float importe;
	
	@Column
	private String signo;
	
	@Column
	private Integer numAut;
	
	@Column
	private Integer numCuot;
	
	@Column
	private Integer planCuot;
	
	@Column
	private Float impPlan;
	
	@Column
	private String signoPlan;
	
	@Column
	private String mcaPex;
	
	@Column
	private Integer nroLiq;
	
	@Column
	private String ccoOrigen;
	
	@Column
	private String ccoMotivo;
	
	@Column
	private String idIvaCf;
	
	@Column
	private Float costoFin;
	
	@Column
	private String signoCf;
	
	@Column
	private Integer numEst;
	
	@Column
	private String tipoPlan;
	
	@Column
	private String ccoMotivoMc;
	
	@Column
	private String fechaCreacion;

	@Column
	private Long idTransaccion;
}
