package com.sample.model;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class CartItem {
	
	private Item item;
    private Integer quantity;
    private BigDecimal memberDiscount;
    private BigDecimal itemActivityDiscount;
    
    private Map<String, BigDecimal> activityDiscountMap = new LinkedHashMap<>();
    
    public BigDecimal getTotal() {
        return item.getCurrentPrice().multiply(new BigDecimal(quantity));
    }

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getMemberDiscount() {
		return memberDiscount;
	}

	public void setMemberDiscount(BigDecimal memberDiscount) {
		this.memberDiscount = memberDiscount;
	}

	public BigDecimal getItemActivityDiscount() {
		return itemActivityDiscount;
	}

	public void setItemActivityDiscount(BigDecimal itemActivityDiscount) {
		this.itemActivityDiscount = itemActivityDiscount;
	}

	public Map<String, BigDecimal> getActivityDiscountMap() {
		return activityDiscountMap;
	}

	public void setActivityDiscountMap(Map<String, BigDecimal> activityDiscountMap) {
		this.activityDiscountMap = activityDiscountMap;
	}
}
