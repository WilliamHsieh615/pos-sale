package com.sample.model;

import java.math.BigDecimal;

public class Item {
	
	private String itemCode; // 品號
	private String itemCName; // 品名
	private String category01; // 商品大類
	private BigDecimal currentPrice; // 售價
	
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemCName() {
		return itemCName;
	}
	public void setItemCName(String itemCName) {
		this.itemCName = itemCName;
	}
	public String getCategory01() {
		return category01;
	}
	public void setCategory01(String category01) {
		this.category01 = category01;
	}
	public BigDecimal getCurrentPrice() {
		return currentPrice;
	}
	public void setCurrentPrice(BigDecimal currentPrice) {
		this.currentPrice = currentPrice;
	}
	
	
}
