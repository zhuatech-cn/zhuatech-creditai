/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.creditai;

import cn.zhuatech.creditai.service.CreditAnalysisService;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

class CreditAnalysisServiceTests {
    private final CreditAnalysisService service = new CreditAnalysisService();
    @Test void restrictsHighExposureOverdueCustomer() {
        var result = service.assess(new CreditAnalysisService.Request("CUS-88", new BigDecimal("2000000"), new BigDecimal("600000"), 120, 5, 1, 82));
        assertThat(result.riskLevel()).isEqualTo("HIGH");
        assertThat(result.paymentTerm()).isEqualTo("CASH_IN_ADVANCE");
    }
    @Test void grantsLimitToStableCustomer() {
        var result = service.assess(new CreditAnalysisService.Request("CUS-20", new BigDecimal("8000000"), new BigDecimal("100000"), 0, 0, 12, 10));
        assertThat(result.riskLevel()).isEqualTo("LOW");
        assertThat(result.suggestedAvailableLimit()).isPositive();
    }
}
