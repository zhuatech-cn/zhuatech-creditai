/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
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

/**
 * 企业信用额度治理，销售例外不得绕过财务与风险双人审批。
 *
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service
public class CreditLimitGovernanceService {
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public Decision govern(Request request) {
        BigDecimal revenueFactor = request.riskScore() >= 70 ? new BigDecimal("0.02")
                : request.riskScore() >= 40 ? new BigDecimal("0.05") : new BigDecimal("0.10");
        BigDecimal policyLimit = request.annualRevenue().multiply(revenueFactor)
                .add(request.collateralValue().multiply(new BigDecimal("0.50")))
                .setScale(2, RoundingMode.HALF_UP);
        if (request.overdueDays() > 90) policyLimit = BigDecimal.ZERO.setScale(2);
        BigDecimal availableLimit = policyLimit.subtract(request.currentExposure())
                .max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
        boolean exceedsPolicy = request.proposedLimit().compareTo(policyLimit) > 0;
        boolean dualApprovalRequired = request.salesOverrideRequested() || exceedsPolicy
                || request.riskScore() >= 40;
        boolean approved = request.overdueDays() <= 90 && (!dualApprovalRequired
                || request.financeApproved() && request.riskApproved());
        String route = request.overdueDays() > 90 ? "CASH_ONLY"
                : dualApprovalRequired && !approved ? "DUAL_APPROVAL_REQUIRED"
                : exceedsPolicy ? "CONTROLLED_OVERRIDE" : "POLICY_APPROVED";
        List<String> reasons = new ArrayList<>();
        if (request.overdueDays() > 90) reasons.add("严重逾期客户暂停新增信用敞口");
        if (exceedsPolicy) reasons.add("申请额度超过政策额度，必须记录业务理由");
        if (request.salesOverrideRequested()) reasons.add("销售例外需要财务和风险负责人双签");
        if (request.collateralValue().signum() > 0) reasons.add("抵押物价值按 50% 审慎折扣计入额度");
        if (reasons.isEmpty()) reasons.add("额度位于政策阈值内并保留审批快照");
        return new Decision(request.customerCode(), policyLimit, availableLimit, route,
                dualApprovalRequired, approved, List.copyOf(reasons));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Request(@NotBlank String customerCode,
                          @DecimalMin("0.01") BigDecimal annualRevenue,
                          @DecimalMin("0") BigDecimal currentExposure,
                          @DecimalMin("0") BigDecimal proposedLimit,
                          @DecimalMin("0") BigDecimal collateralValue,
                          @Min(0) @Max(100) int riskScore, @Min(0) int overdueDays,
                          boolean salesOverrideRequested, boolean financeApproved,
                          boolean riskApproved) {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Decision(String customerCode, BigDecimal policyLimit,
                           BigDecimal availableLimit, String route,
                           boolean dualApprovalRequired, boolean approved,
                           List<String> reasons) {}
}
