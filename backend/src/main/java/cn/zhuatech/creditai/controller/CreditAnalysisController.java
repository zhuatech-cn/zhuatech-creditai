/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.creditai.controller;

import cn.zhuatech.creditai.common.ApiResponse;
import cn.zhuatech.creditai.service.CreditAnalysisService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai/credit")
@PreAuthorize("hasAnyRole('DOMAIN_USER','DOMAIN_OPERATOR','ADMIN')")
public class CreditAnalysisController {
    private final CreditAnalysisService service;
    public CreditAnalysisController(CreditAnalysisService service) { this.service = service; }
    @PostMapping("/assess")
    public ApiResponse<CreditAnalysisService.Result> assess(@Valid @RequestBody CreditAnalysisService.Request request) {
        return ApiResponse.ok("客户信用评估完成", service.assess(request));
    }
}
