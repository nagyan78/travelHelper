package com.travel.itinerary.module.budget.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 智能预算优化服务
 * 提供多种预算方案对比，帮助用户合理分配旅行费用
 * 
 * @author Travel Team
 * @version 1.0.0
 */
@Service
@Slf4j
public class BudgetOptimizerService {
    
    /**
     * 基于预算生成多种方案
     * 
     * @param totalBudget 总预算
     * @param days 天数
     * @param destination 目的地
     * @return 多种预算方案 (按评分排序)
     */
    public List<BudgetPlan> optimize(
        BigDecimal totalBudget, 
        Integer days,
        String destination
    ) {
        log.info("开始优化预算: 总预算={}, 天数={}, 目的地={}", totalBudget, days, destination);
        
        List<BudgetPlan> plans = new ArrayList<>();
        
        // 生成3种不同策略的方案
        plans.add(generateEconomyPlan(totalBudget, days, destination));
        plans.add(generateComfortPlan(totalBudget, days, destination));
        plans.add(generateLuxuryPlan(totalBudget, days, destination));
        
        // 按评分排序
        List<BudgetPlan> sorted = plans.stream()
            .sorted(Comparator.comparing(BudgetPlan::getScore).reversed())
            .collect(Collectors.toList());
        
        log.info("预算优化完成, 生成{}个方案", sorted.size());
        
        return sorted;
    }
    
    /**
     * 经济型方案 - 最大化性价比
     */
    private BudgetPlan generateEconomyPlan(
        BigDecimal totalBudget, 
        Integer days,
        String destination
    ) {
        BudgetPlan plan = new BudgetPlan();
        plan.setType("经济型");
        plan.setStrategy("最大化性价比");
        plan.setDescription("适合预算有限的旅行者，在保证基本体验的前提下最大化性价比");
        
        // 预算分配比例
        BigDecimal transportRate = new BigDecimal("0.25");  // 交通 25%
        BigDecimal foodRate = new BigDecimal("0.30");       // 餐饮 30%
        BigDecimal ticketRate = new BigDecimal("0.25");     // 门票 25%
        BigDecimal shoppingRate = new BigDecimal("0.10");   // 购物 10%
        BigDecimal reserveRate = new BigDecimal("0.10");    // 备用金 10%
        
        plan.setTransportCost(totalBudget.multiply(transportRate));
        plan.setFoodCost(totalBudget.multiply(foodRate));
        plan.setTicketCost(totalBudget.multiply(ticketRate));
        plan.setShoppingCost(totalBudget.multiply(shoppingRate));
        plan.setReserveCost(totalBudget.multiply(reserveRate));
        
        // 详细建议
        List<String> tips = new ArrayList<>();
        tips.add("🚇 交通: 优先使用地铁、公交，避免打车");
        tips.add("🍜 餐饮: 尝试本地小吃和平价餐厅，人均30-50元/餐");
        tips.add("🎫 门票: 选择免费景点，或购买联票优惠");
        tips.add("🛍️ 购物: 控制购物预算，重点购买必需品");
        tips.add("💡 提示: 此方案可节省约30%费用");
        plan.setTips(tips);
        
        // 计算日均预算
        plan.setDailyBudget(plan.getTotalCost().divide(new BigDecimal(days), 2));
        
        // 评分 (性价比得分高)
        plan.setScore(calculateScore(plan, "economy"));
        
        return plan;
    }
    
    /**
     * 舒适型方案 - 平衡体验与成本
     */
    private BudgetPlan generateComfortPlan(
        BigDecimal totalBudget, 
        Integer days,
        String destination
    ) {
        BudgetPlan plan = new BudgetPlan();
        plan.setType("舒适型");
        plan.setStrategy("平衡体验与成本");
        plan.setDescription("适合大多数旅行者，在合理预算内获得较好的旅行体验");
        
        // 预算分配比例
        BigDecimal transportRate = new BigDecimal("0.30");  // 交通 30%
        BigDecimal foodRate = new BigDecimal("0.35");       // 餐饮 35%
        BigDecimal ticketRate = new BigDecimal("0.20");     // 门票 20%
        BigDecimal shoppingRate = new BigDecimal("0.10");   // 购物 10%
        BigDecimal reserveRate = new BigDecimal("0.05");    // 备用金 5%
        
        plan.setTransportCost(totalBudget.multiply(transportRate));
        plan.setFoodCost(totalBudget.multiply(foodRate));
        plan.setTicketCost(totalBudget.multiply(ticketRate));
        plan.setShoppingCost(totalBudget.multiply(shoppingRate));
        plan.setReserveCost(totalBudget.multiply(reserveRate));
        
        // 详细建议
        List<String> tips = new ArrayList<>();
        tips.add("🚕 交通: 短途步行/地铁，长途可考虑打车");
        tips.add("🍽️ 餐饮: 混合搭配，早餐当地小吃，午晚餐选择中档餐厅");
        tips.add("🎫 门票: 选择核心景点付费，其他自由选择");
        tips.add("🛍️ 购物: 适当购买纪念品和特产");
        tips.add("☕ 额外: 预留咖啡廳、下午茶等休闲预算");
        plan.setTips(tips);
        
        plan.setDailyBudget(plan.getTotalCost().divide(new BigDecimal(days), 2));
        plan.setScore(calculateScore(plan, "comfort"));
        
        return plan;
    }
    
    /**
     * 豪华型方案 - 追求极致体验
     */
    private BudgetPlan generateLuxuryPlan(
        BigDecimal totalBudget, 
        Integer days,
        String destination
    ) {
        BudgetPlan plan = new BudgetPlan();
        plan.setType("豪华型");
        plan.setStrategy("追求极致体验");
        plan.setDescription("适合追求高品质旅行的用户，享受最佳服务和体验");
        
        // 预算分配比例
        BigDecimal transportRate = new BigDecimal("0.35");  // 交通 35%
        BigDecimal foodRate = new BigDecimal("0.40");       // 餐饮 40%
        BigDecimal ticketRate = new BigDecimal("0.15");     // 门票 15%
        BigDecimal shoppingRate = new BigDecimal("0.05");   // 购物 5%
        BigDecimal reserveRate = new BigDecimal("0.05");    // 备用金 5%
        
        plan.setTransportCost(totalBudget.multiply(transportRate));
        plan.setFoodCost(totalBudget.multiply(foodRate));
        plan.setTicketCost(totalBudget.multiply(ticketRate));
        plan.setShoppingCost(totalBudget.multiply(shoppingRate));
        plan.setReserveCost(totalBudget.multiply(reserveRate));
        
        // 详细建议
        List<String> tips = new ArrayList<>();
        tips.add("🚗 交通: 包车或专车接送，省时省力");
        tips.add("🍷 餐饮: 选择特色餐厅、米其林餐厅，品尝当地美食精华");
        tips.add("🎭 门票: VIP通道、私人导览服务");
        tips.add("🎁 购物: 精品店、免税店购物");
        tips.add("💆 额外: SPA、高端酒店下午茶等奢华体验");
        plan.setTips(tips);
        
        plan.setDailyBudget(plan.getTotalCost().divide(new BigDecimal(days), 2));
        plan.setScore(calculateScore(plan, "luxury"));
        
        return plan;
    }
    
    /**
     * 计算方案评分
     */
    private Double calculateScore(BudgetPlan plan, String type) {
        double baseScore = 80.0;
        
        switch (type) {
            case "economy":
                // 经济型: 性价比高，得分较高
                return baseScore + 10;
            case "comfort":
                // 舒适型: 平衡性好，得分最高
                return baseScore + 15;
            case "luxury":
                // 豪华型: 体验好但成本高
                return baseScore + 5;
            default:
                return baseScore;
        }
    }
    
    /**
     * 预算方案实体
     */
    @Data
    public static class BudgetPlan {
        private String type;                  // 方案类型
        private String strategy;              // 策略名称
        private String description;           // 方案描述
        private BigDecimal totalCost;         // 总费用 (由各项相加)
        private BigDecimal transportCost;     // 交通费用
        private BigDecimal foodCost;          // 餐饮费用
        private BigDecimal ticketCost;        // 门票费用
        private BigDecimal shoppingCost;      // 购物费用
        private BigDecimal reserveCost;       // 备用金
        private BigDecimal dailyBudget;       // 日均预算
        private List<String> tips;            // 详细建议
        private Double score;                 // 评分 (用于排序)
        
        /**
         * 自动计算总费用
         */
        public void calculateTotal() {
            this.totalCost = (transportCost != null ? transportCost : BigDecimal.ZERO)
                .add(foodCost != null ? foodCost : BigDecimal.ZERO)
                .add(ticketCost != null ? ticketCost : BigDecimal.ZERO)
                .add(shoppingCost != null ? shoppingCost : BigDecimal.ZERO)
                .add(reserveCost != null ? reserveCost : BigDecimal.ZERO);
        }
    }
}
