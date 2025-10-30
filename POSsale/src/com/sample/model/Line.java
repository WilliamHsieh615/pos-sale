package com.sample.model;

import java.math.BigDecimal;

public class Line {
	
	private BigDecimal unit;
	private BigDecimal discount;
	private BigDecimal payables;
	private String discountFormula;
	private BigDecimal discountRatePct;
	
	public BigDecimal getUnit() {
		return unit;
	}
	public void setUnit(BigDecimal unit) {
		this.unit = unit;
	}
	public BigDecimal getDiscount() {
		return discount;
	}
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	public BigDecimal getPayables() {
		return payables;
	}
	public void setPayables(BigDecimal payables) {
		this.payables = payables;
	}
	public String getDiscountFormula() {
		return discountFormula;
	}
	public void setDiscountFormula(String discountFormula) {
		this.discountFormula = discountFormula;
	}
	public BigDecimal getDiscountRatePct() {
		return discountRatePct;
	}
	public void setDiscountRatePct(BigDecimal discountRatePct) {
		this.discountRatePct = discountRatePct;
	}
}
