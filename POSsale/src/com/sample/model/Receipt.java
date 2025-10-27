package com.sample.model;

import java.math.BigDecimal;
import java.util.List;

public class Receipt {
	
	private double originalTotal; // 原價金額
	private BigDecimal memberDiscountTotal; // 會員折扣總額
    private BigDecimal activityDiscountTotal; // 活動折扣總額
    private double finalTotal; // 攤提後最終應付金額
    private List<CartItem> cartItems;
	public double getOriginalTotal() {
		return originalTotal;
	}
	public void setOriginalTotal(double originalTotal) {
		this.originalTotal = originalTotal;
	}
	public BigDecimal getMemberDiscountTotal() {
		return memberDiscountTotal;
	}
	public void setMemberDiscountTotal(BigDecimal memberDiscountTotal) {
		this.memberDiscountTotal = memberDiscountTotal;
	}
	public BigDecimal getActivityDiscountTotal() {
		return activityDiscountTotal;
	}
	public void setActivityDiscountTotal(BigDecimal activityDiscountTotal) {
		this.activityDiscountTotal = activityDiscountTotal;
	}
	public double getFinalTotal() {
		return finalTotal;
	}
	public void setFinalTotal(double finalTotal) {
		this.finalTotal = finalTotal;
	}
	public List<CartItem> getCartItems() {
		return cartItems;
	}
	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = cartItems;
	}
}
