package com.travel.itinerary.module.budget.controller;

import com.travel.itinerary.common.result.Result;
import com.travel.itinerary.module.budget.service.BudgetOptimizerService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 智能预算优化控制器
 * 
 * @author Travel Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/budget")
public class BudgetOptimizerController {
    
    @Autowired
    private BudgetOptimizerService budgetService;
    
    /**
     * 生成预算方案
     */
    @PostMapping("/optimize")
    public Result<List<BudgetOptimizerService.BudgetPlan>> optimize(
        @RequestBody BudgetOptimizeRequest request
    ) {
        List<BudgetOptimizerService.BudgetPlan> plans = budgetService.optimize(
            request.getTotalBudget(),
            request.getDays(),
            request.getDestination()
        );
        
        return Result.success(plans);
    }
    
    /**
     * 快速预算估算
     */
    @GetMapping("/estimate")
    public Result<BudgetEstimate> estimate(
        @RequestParam String destination,
        @RequestParam Integer days
    ) {
        // TODO: 实现基于历史数据的预算估算
        BudgetEstimate estimate = new BudgetEstimate();
        estimate.setDestination(destination);
        estimate.setDays(days);
        estimate.setMinBudget(new BigDecimal(days * 300));  // 最低300元/天
        estimate.setMaxBudget(new BigDecimal(days * 2000)); // 最高2000元/天
        estimate.setRecommended(new BigDecimal(days * 800)); // 推荐800元/天
        
        return Result.success(estimate);
    }
    
    /**
     * 预算分配建议
     */
    @GetMapping("/allocation")
    public Result<String> allocation(@RequestParam String style) {
        String advice;
        switch (style.toLowerCase()) {
            case "economy":
                advice = "经济型预算分配:\n" +
                        "交通: 25% | 餐饮: 30% | 门票: 25% | 购物: 10% | 备用: 10%";
                break;
            case "comfort":
                advice = "舒适型预算分配:\n" +
                        "交通: 30% | 餐饮: 35% | 门票: 20% | 购物: 10% | 备用: 5%";
                break;
            case "luxury":
                advice = "豪华型预算分配:\n" +
                        "交通: 35% | 餐饮: 40% | 门票: 15% | 购物: 5% | 备用: 5%";
                break;
            default:
                advice = "标准预算分配:\n" +
                        "交通: 30% | 餐饮: 30% | 门票: 20% | 购物: 10% | 备用: 10%";
        }
        
        return Result.success(advice);
    }
    
    /**
     * 预算优化请求
     */
    @Data
    public static class BudgetOptimizeRequest {
        private BigDecimal totalBudget;  // 总预算
        private Integer days;            // 天数
        private String destination;      // 目的地
    }
    
    /**
     * 预算估算结果
     */
    @Data
    public static class BudgetEstimate {
        private String destination;       // 目的地
        private Integer days;             // 天数
        private BigDecimal minBudget;     // 最低预算
        private BigDecimal maxBudget;     // 最高预算
        private BigDecimal recommended;   // 推荐预算
    }
}
