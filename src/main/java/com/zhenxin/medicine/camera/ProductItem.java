package com.zhenxin.medicine.camera;

/** 
 * REALLY BAD; Should not duplicate items here! Find some way to call ProductItem in ZhenXin project
 * 
 * @author Ivan
 *
 */
public class ProductItem {

	private int productKey;
	private String productName;
	private int drugKey;
	private String serialCode;
	//private Date validUntil;
	private int enteredBy;
	
	public ProductItem()	{
		super();
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
	public int getEnteredBy() {
		return enteredBy;
	}
	public void setEnteredBy(int enteredBy) {
		this.enteredBy = enteredBy;
	}
	
	
}
