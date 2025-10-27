package com.sample.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Activity {
	
	private String activityCode; // 活動編號
	private String activityName; // 活動名稱
	private LocalDate startActivityDate; // 開始日期
	private LocalDate endActivityDate; // 結束日期
	private List<String> itemDiscountGroup; // 參加活動的商品類別
	private BigDecimal meetCriteriaAmtG1; // 滿額條件
	private BigDecimal awardAmtG1; // 折扣金額
	
	public String getActivityCode() {
		return activityCode;
	}
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public LocalDate getStartActivityDate() {
		return startActivityDate;
	}
	public void setStartActivityDate(LocalDate startActivityDate) {
		this.startActivityDate = startActivityDate;
	}
	public LocalDate getEndActivityDate() {
		return endActivityDate;
	}
	public void setEndActivityDate(LocalDate endActivityDate) {
		this.endActivityDate = endActivityDate;
	}
	public List<String> getItemDiscountGroup() {
		return itemDiscountGroup;
	}
	public void setItemDiscountGroup(List<String> itemDiscountGroup) {
		this.itemDiscountGroup = itemDiscountGroup;
	}
	public BigDecimal getMeetCriteriaAmtG1() {
		return meetCriteriaAmtG1;
	}
	public void setMeetCriteriaAmtG1(BigDecimal meetCriteriaAmtG1) {
		this.meetCriteriaAmtG1 = meetCriteriaAmtG1;
	}
	public BigDecimal getAwardAmtG1() {
		return awardAmtG1;
	}
	public void setAwardAmtG1(BigDecimal awardAmtG1) {
		this.awardAmtG1 = awardAmtG1;
	}
}
