/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.creditai;

import cn.zhuatech.creditai.service.CreditLimitGovernanceService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CreditLimitGovernanceServiceTests {
    private final CreditLimitGovernanceService service = new CreditLimitGovernanceService();

    @Test void blocksSeverelyOverdueCustomer() {
        var result = service.govern(new CreditLimitGovernanceService.Request("CUS-01",
                new BigDecimal("10000000"), new BigDecimal("100000"), new BigDecimal("300000"),
                BigDecimal.ZERO, 30, 120, false, true, true));
        assertThat(result.route()).isEqualTo("CASH_ONLY");
        assertThat(result.approved()).isFalse();
    }

    @Test void permitsDualApprovedControlledOverride() {
        var result = service.govern(new CreditLimitGovernanceService.Request("CUS-02",
                new BigDecimal("10000000"), new BigDecimal("100000"), new BigDecimal("700000"),
                new BigDecimal("200000"), 45, 5, true, true, true));
        assertThat(result.route()).isEqualTo("CONTROLLED_OVERRIDE");
        assertThat(result.approved()).isTrue();
    }
}
