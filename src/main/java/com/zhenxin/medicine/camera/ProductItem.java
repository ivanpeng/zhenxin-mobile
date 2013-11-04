package com.zhenxin.medicine.camera;

import java.util.Date;

public class ProductItem {

	private int productKey;
	private String productName;
	private int drugKey;
	private String serialCode;
	private String checked;
	private String validUntil;
	private String enteredOn;
	private int enteredBy;
	
	public ProductItem()	{
		super();
	}
	
	public static ProductItem generateDefault()	{
		ProductItem item = new ProductItem();
		item.setProductKey(-1);
		item.setDrugKey(-1);
		item.setProductName("");
		item.setSerialCode("");
		item.setChecked(null);
		item.setValidUntil((new Date(System.currentTimeMillis())).toString());
		item.setEnteredBy(-1);
		item.setEnteredOn((new Date(System.currentTimeMillis())).toString());
		return item;
	}
	
	public int getProductKey() {
		return productKey;
	}
	public void setProductKey(int productKey) {
		this.productKey = productKey;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getDrugKey() {
		return drugKey;
	}
	public void setDrugKey(int drugKey) {
		this.drugKey = drugKey;
	}
	public String getSerialCode() {
		return serialCode;
	}
	public void setSerialCode(String serialCode) {
		this.serialCode = serialCode;
	}	
	public String getValidUntil() {
		return validUntil;
	}
	public void setValidUntil(String validUntil) {
		this.validUntil = validUntil;
	}
	public int getEnteredBy() {
		return enteredBy;
	}
	public void setEnteredBy(int enteredBy) {
		this.enteredBy = enteredBy;
	}
	public String getChecked() {
		return checked;
	}
	public void setChecked(String checked) {
		this.checked = checked;
	}
	public String getEnteredOn() {
		return enteredOn;
	}
	public void setEnteredOn(String enteredOn) {
		this.enteredOn = enteredOn;
	}
	
	
}
