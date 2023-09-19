package com.pharma.order.dto;

import java.io.Serializable;

public class SupplierRetailerCreateDto implements Serializable {

	private static final long serialVersionUID = -7799306911148099715L;

	private Long retailerCode;
	private Long supplierCode;
	private Long supplierStoreCode;
	private Long retailerStoreCode;

	public SupplierRetailerCreateDto() {
		super();
	}

	public SupplierRetailerCreateDto(Long retailerCode, Long supplierCode, Long supplierStoreCode,
			Long retailerStoreCode) {
		super();
		this.retailerCode = retailerCode;
		this.supplierCode = supplierCode;
		this.supplierStoreCode = supplierStoreCode;
		this.retailerStoreCode = retailerStoreCode;
	}

	public Long getRetailerCode() {
		return retailerCode;
	}

	public void setRetailerCode(Long retailerCode) {
		this.retailerCode = retailerCode;
	}

	public Long getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(Long supplierCode) {
		this.supplierCode = supplierCode;
	}

	public Long getSupplierStoreCode() {
		return supplierStoreCode;
	}

	public void setSupplierStoreCode(Long supplierStoreCode) {
		this.supplierStoreCode = supplierStoreCode;
	}

	public Long getRetailerStoreCode() {
		return retailerStoreCode;
	}

	public void setRetailerStoreCode(Long retailerStoreCode) {
		this.retailerStoreCode = retailerStoreCode;
	}

	@Override
	public String toString() {
		return "SupplierRetailerCreateDto [retailerCode=" + retailerCode + ", supplierCode=" + supplierCode
				+ ", supplierStoreCode=" + supplierStoreCode + ", retailerStoreCode=" + retailerStoreCode + "]";
	}
}
