package com.sample;

import java.sql.Connection;
import java.sql.DriverManager;

import com.sample.service.PromotionService;

public class App {
	
	
	private static final String URL = "jdbc:mysql://localhost:3306/twd4700002?serverTimezone=Asia/Taipei&useSSL=false&useUnicode=true&characterEncoding=utf-8";
    private static final String USER = "root";
    private static final String PASSWORD = "12345678";

	public static void main(String[] args) {
		
		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            PromotionService service = new PromotionService(connection);
            service.runPromotion();
        } catch (Exception e) {
            e.printStackTrace();
        }

	}

}
