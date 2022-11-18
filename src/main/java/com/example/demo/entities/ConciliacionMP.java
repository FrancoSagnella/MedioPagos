package com.example.demo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="conciliacionMp")
public class ConciliacionMP {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column
	private String externalReference;
	
	@Column
	private Integer sourceId;
	
	@Column
	private Integer userId;
	
	@Column 
	private String paymentMethod;

	@Column 
	private String transactionDate;

	@Column 
	private String settlementDate;
	
	@Column
	private Float couponAmount;
	
	@Column
	private Float mkpFeeAmount;
	
	@Column
	private Float shippingFeeAmount;
	
	@Column
	private Float financingFeeAmount;
	
	@Column
	private Float taxesAmount;
	
	@Column
	private String fpag;
	
	@Column
	private String fechaCreacion;
	
	@Column
	private Long idTransaccion;
	
}
