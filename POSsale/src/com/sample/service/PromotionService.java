package com.sample.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

import com.sample.model.Activity;
import com.sample.model.CartItem;
import com.sample.model.Item;
import com.sample.model.Line;
import com.sample.model.Receipt;
import com.sample.repo.ActivityRepository;
import com.sample.repo.ItemRepository;

public class PromotionService {

	private ItemRepository itemRepo;
	private ActivityRepository activityRepo;

	public PromotionService(Connection conn) {
		this.itemRepo = new ItemRepository(conn);
		this.activityRepo = new ActivityRepository(conn);
	}

	public void runPromotion() throws SQLException {
		Scanner sc = new Scanner(System.in);
		
		// 是否為會員
		System.out.print("請問您是會員嗎？(Y/N): ");
		String isMemberInput = sc.nextLine().trim().toLowerCase();
		boolean isMember = isMemberInput.equals("y") || isMemberInput.equals("yes");
		
		// 輸入商品代號與購買數量
		System.out.println("請輸入購買商品代號與數量（輸入 done 結束）");
		List<CartItem> cart = new ArrayList<>();

		while (true) {
			System.out.print("商品代號: ");
			String code = sc.nextLine().trim().toLowerCase();
			if (code.equalsIgnoreCase("done"))
				break;

			Item item = itemRepo.findItemByCode(code);
			if (item == null) {
				System.out.println("查無此商品代號，請重新輸入");
				continue;
			}

			int quantity = 0;
			while (true) {
				System.out.print("數量: ");
				try {
					quantity = Integer.parseInt(sc.nextLine().trim());
					if (quantity > 0)
						break;
					System.out.println("請輸入大於 0 的數量");
				} catch (NumberFormatException e) {
					System.out.println("請輸入有效的整數數量");
				}
			}

			CartItem cartItem = new CartItem();
			cartItem.setItem(item);
			cartItem.setQuantity(quantity);
			cart.add(cartItem);
		}
		
		calculate(cart, isMember);
	}

	private void calculate(List<CartItem> cart, boolean isMember) throws SQLException {

	    Receipt receipt = new Receipt();

	    BigDecimal originalTotal = BigDecimal.ZERO;
	    BigDecimal memberDiscountTotal = BigDecimal.ZERO;
	    BigDecimal activityDiscountTotal = BigDecimal.ZERO;

	    LocalDate today = LocalDate.now();

	    // 計算原始總額
	    for (CartItem cartItem : cart) {
	        BigDecimal itemTotal = cartItem.getTotal();
	        originalTotal = originalTotal.add(itemTotal);
	        cartItem.setItemActivityDiscount(BigDecimal.ZERO); // 初始化總折扣
	        cartItem.setActivityDiscountMap(new LinkedHashMap<>()); // 初始化活動折扣明細
	    }

	    // 會員優惠（酒類 95折）
	    for (CartItem cartItem : cart) {
	        BigDecimal memberDiscount = BigDecimal.ZERO;
	        if (isMember && (cartItem.getItem().getCategory01().equals("07")
	                || cartItem.getItem().getCategory01().equals("16"))) {
	            memberDiscount = cartItem.getTotal().multiply(new BigDecimal("0.05"))
	                    .setScale(0, RoundingMode.HALF_UP);
	            cartItem.setMemberDiscount(memberDiscount);
		        memberDiscountTotal = memberDiscountTotal.add(memberDiscount);
	        }
	    }

	    // 找出當日所有有效活動
	    List<Activity> allActivities = activityRepo.findAllValidActivities(today);

	    // 每個活動單獨處理
	    for (Activity activity : allActivities) {

	        // 找出符合該活動的商品
	        List<CartItem> activityEligibleItems = new ArrayList<>();
	        BigDecimal eligibleSubtotal = BigDecimal.ZERO;
	        for (CartItem cartItem : cart) {
	            if (activity.getItemDiscountGroup().contains(cartItem.getItem().getCategory01())) {
	                activityEligibleItems.add(cartItem);
	                eligibleSubtotal = eligibleSubtotal.add(cartItem.getTotal());
	            }
	        }

	        // 達門檻才折扣
	        if (eligibleSubtotal.compareTo(activity.getMeetCriteriaAmtG1()) >= 0) {

	            // 活動折扣攤提給符合商品
	            for (CartItem cartItem : activityEligibleItems) {
	                BigDecimal proportion = cartItem.getTotal()
	                        .divide(eligibleSubtotal, 10, RoundingMode.HALF_UP);
	                BigDecimal itemActivityDiscount = activity.getAwardAmtG1()
	                        .multiply(proportion)
	                        .setScale(0, RoundingMode.HALF_UP);
	                // 累加總折扣
	                cartItem.setItemActivityDiscount(
	                        cartItem.getItemActivityDiscount().add(itemActivityDiscount)
	                );
	                // 記錄折扣明細
	                cartItem.getActivityDiscountMap().put(activity.getActivityName(), itemActivityDiscount);

	                activityDiscountTotal = activityDiscountTotal.add(itemActivityDiscount);
	            }
	        }
	    }
	    
	    // 計算最終應付金額
	    BigDecimal finalTotal = originalTotal
	            .subtract(memberDiscountTotal)
	            .subtract(activityDiscountTotal)
	            .setScale(0, RoundingMode.HALF_UP);
	    
	    // 建立 Receipt Line
	    List<Line> lines = new ArrayList<>();
	    for (CartItem cartItem : cart) {
	        Line line = new Line();

	        BigDecimal unit = cartItem.getItem().getCurrentPrice();
	        BigDecimal discount = cartItem.getMemberDiscount() != null 
	        		? cartItem.getMemberDiscount().add(cartItem.getItemActivityDiscount())
	        		: cartItem.getItemActivityDiscount();

	        BigDecimal payables = cartItem.getTotal().subtract(discount);
	        
	        BigDecimal discountRatePct = BigDecimal.ZERO;
	        if (cartItem.getTotal().compareTo(BigDecimal.ZERO) > 0) {
	            discountRatePct = discount
	                    .divide(cartItem.getTotal(), 4, RoundingMode.HALF_UP)
	                    .multiply(new BigDecimal("100"))
	                    .setScale(2, RoundingMode.HALF_UP);
	        }

	        // 活動明細字串
	        StringBuilder discountFormula = new StringBuilder();
	        for (var entry : cartItem.getActivityDiscountMap().entrySet()) {
	            discountFormula.append(entry.getKey())
	                    .append(":")
	                    .append(entry.getValue())
	                    .append(" ");
	        }

	        line.setUnit(unit);
	        line.setDiscount(discount);
	        line.setPayables(payables);
	        line.setDiscountRatePct(discountRatePct);
	        line.setDiscountFormula(discountFormula.toString().trim());
	        lines.add(line);
	    }
	    
	    // 填入 Receipt
        receipt.setOriginalTotal(originalTotal.doubleValue());
        receipt.setMemberDiscountTotal(memberDiscountTotal);
        receipt.setActivityDiscountTotal(activityDiscountTotal);
        receipt.setFinalTotal(finalTotal.doubleValue());
        receipt.setLines(lines);

	    // 輸出明細
        System.out.printf("%-15s %8s %8s%% %8s %s\n", "品名", "原價", "折扣比", "折扣總額", "活動折扣明細");
        for (int i = 0; i < cart.size(); i++) {
	        CartItem cartItem = cart.get(i);
	        Line line = lines.get(i);

	        System.out.printf("%-15s %8s %8s%% %8s %s\n",
	                cartItem.getItem().getItemCName(),
	                line.getUnit().multiply(new BigDecimal(cartItem.getQuantity())).setScale(0, RoundingMode.HALF_UP),
	                line.getDiscountRatePct(),
	                line.getDiscount(),
	                line.getDiscountFormula());
	    }

	    System.out.println("\n=============================");
	    System.out.println("原價總額: " + originalTotal.setScale(0, RoundingMode.HALF_UP));
	    System.out.println("會員折扣總額: " + memberDiscountTotal.setScale(0, RoundingMode.HALF_UP));
	    System.out.println("活動折扣總額: " + activityDiscountTotal.setScale(0, RoundingMode.HALF_UP));
	    System.out.println("最終應付金額: " + finalTotal);
	    System.out.println("=============================");
	}
}
