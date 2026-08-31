/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.creditai.controller;

import cn.zhuatech.creditai.service.CreditLimitGovernanceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 企业级决策 API，调用方应保存请求、响应与审批审计轨迹。 */
@RestController
@RequestMapping("/api/enterprise/credit")
@CrossOrigin(originPatterns = {"http://localhost:*", "http://127.0.0.1:*"})
public class CreditLimitGovernanceController {
    private final CreditLimitGovernanceService service;

    public CreditLimitGovernanceController(CreditLimitGovernanceService service) {
        this.service = service;
    }

    @PostMapping("/limit-governance")
    public CreditLimitGovernanceService.Decision decide(@Valid @RequestBody CreditLimitGovernanceService.Request request) {
        return service.govern(request);
    }
}
