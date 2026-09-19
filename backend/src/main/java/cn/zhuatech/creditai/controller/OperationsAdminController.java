/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.creditai.controller; import cn.zhuatech.creditai.common.ApiResponse; import cn.zhuatech.creditai.dto.CreditAiDto.*; import cn.zhuatech.creditai.service.CreditAiService; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.util.List;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController @RequestMapping("/api/admin") @PreAuthorize("hasAnyRole('DOMAIN_OPERATOR','QUALITY','ADMIN')") public class OperationsAdminController {private final CreditAiService service;/**
                                                                                                                                                                                              * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                                                                              */
public OperationsAdminController(CreditAiService service){this.service=service;}/**
                                                                                                                                                                                                                                                                              * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                                                                                                                                                              */
@GetMapping("/dashboard") public ApiResponse<Dashboard> dashboard(){return ApiResponse.ok(service.adminDashboard());}/**
                                                                                                                                                                                                                                                                                                                                                                                                   * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                                                                                                                                                                                                                                                                                   */
@GetMapping("/work-orders") public ApiResponse<List<WorkRecordView>> orders(){return ApiResponse.ok(service.workRecords());}}
