package com.sample.repo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.sample.model.Activity;

public class ActivityRepository {
	
	private Connection connection;
	
    public ActivityRepository(Connection connection) { 
    	this.connection = connection; 
    }
    
    // 找出當日多筆活動
    public List<Activity> findAllValidActivities(LocalDate date) throws SQLException {
        String sql = "SELECT * FROM crm_promo_rebate_h " +
                     "WHERE ? BETWEEN start_activity_date AND end_activity_date";

        List<Activity> activities = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, Date.valueOf(date));

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Activity activity = new Activity();
                activity.setActivityCode(resultSet.getString("activity_code"));
                activity.setActivityName(resultSet.getString("activity_name"));
                activity.setStartActivityDate(resultSet.getDate("start_activity_date").toLocalDate());
                activity.setEndActivityDate(resultSet.getDate("end_activity_date").toLocalDate());
                activity.setMeetCriteriaAmtG1(resultSet.getBigDecimal("meet_criteria_amt_g1"));
                activity.setAwardAmtG1(resultSet.getBigDecimal("award_amt_g1"));

                String discountGroupStr = resultSet.getString("item_discount_group");
                List<String> discountGroupList = new ArrayList<>();
                if (discountGroupStr != null && !discountGroupStr.isEmpty()) {
                    for (String part : discountGroupStr.split(",")) {
                        discountGroupList.add(part.trim());
                    }
                }
                activity.setItemDiscountGroup(discountGroupList);

                activities.add(activity);
            }
        }
        return activities;
    }

}
