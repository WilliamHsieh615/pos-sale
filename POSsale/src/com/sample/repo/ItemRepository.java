package com.sample.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sample.model.Item;

public class ItemRepository {
	
	private Connection connection;
	
    public ItemRepository(Connection connection) { 
    	this.connection = connection;
    }
    
    // 使用 itemCode 查 Item
    public Item findItemByCode(String code) throws SQLException {
    	
    	String sql = "SELECT i.item_code, i.item_c_name, i.category01, p.unit_price " +
                "FROM im_item i " +
                "LEFT JOIN im_item_price p ON i.item_code = p.item_code " +
                "WHERE i.item_code = ? " +
                "ORDER BY p.begin_date DESC " +
                "LIMIT 1";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        	
        	preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Item item = new Item();
                item.setItemCode(resultSet.getString("item_code"));
                item.setItemCName(resultSet.getString("item_c_name"));
                item.setCategory01(resultSet.getString("category01"));
                item.setCurrentPrice(resultSet.getBigDecimal("unit_price"));
                
                return item;
            }
        }
        return null;
    }
}
