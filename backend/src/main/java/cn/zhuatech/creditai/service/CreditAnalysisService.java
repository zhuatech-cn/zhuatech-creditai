/* Copyright 2026 上海如静知华信息科技有限公司 */
package cn.zhuatech.creditai.service;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/** 为 B2B 客户提供可解释的信用风险与额度建议。 */
@Service
public class CreditAnalysisService {
    public Result assess(Request request) {
        BigDecimal exposureRate = request.outstandingReceivable().divide(request.annualRevenue(), 4, RoundingMode.HALF_UP);
        int score = Math.round(request.externalRiskScore() * 0.35f);
        if (exposureRate.compareTo(new BigDecimal("0.20")) > 0) score += 25;
        else if (exposureRate.compareTo(new BigDecimal("0.10")) > 0) score += 12;
        if (request.overdueDays() > 90) score += 30;
        else if (request.overdueDays() > 30) score += 15;
        score += Math.min(20, request.latePayments12Months() * 4);
        if (request.yearsInBusiness() < 2) score += 12;
        score = Math.min(100, score);
        String level = score >= 70 ? "HIGH" : score >= 40 ? "MEDIUM" : "LOW";
        BigDecimal factor = score >= 70 ? new BigDecimal("0.01") : score >= 40 ? new BigDecimal("0.04") : new BigDecimal("0.08");
        BigDecimal suggestedLimit = request.annualRevenue().multiply(factor).subtract(request.outstandingReceivable())
            .max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
        List<String> reasons = new ArrayList<>();
        if (request.overdueDays() > 30) reasons.add("应收账款逾期天数偏高");
        if (exposureRate.compareTo(new BigDecimal("0.10")) > 0) reasons.add("当前信用敞口占营收比例偏高");
        if (request.latePayments12Months() >= 3) reasons.add("近十二个月多次延迟付款");
        if (request.yearsInBusiness() < 2) reasons.add("企业经营年限较短");
        if (reasons.isEmpty()) reasons.add("付款与经营信号保持稳定");
        return new Result(request.customerCode(), score, level, suggestedLimit,
            score >= 70 ? "CASH_IN_ADVANCE" : score >= 40 ? "MANUAL_REVIEW" : "STANDARD_TERMS",
            reasons, suggestedLimit.compareTo(new BigDecimal("500000")) > 0 || score >= 40);
    }

    public record Request(@NotBlank String customerCode,
                          @DecimalMin("0.01") BigDecimal annualRevenue,
                          @DecimalMin("0") BigDecimal outstandingReceivable,
                          @Min(0) int overdueDays, @Min(0) int latePayments12Months,
                          @Min(0) int yearsInBusiness,
                          @Min(0) @Max(100) int externalRiskScore) {}
    public record Result(String customerCode, int creditRiskScore, String riskLevel,
                         BigDecimal suggestedAvailableLimit, String paymentTerm,
                         List<String> reasons, boolean approvalRequired) {}
}
