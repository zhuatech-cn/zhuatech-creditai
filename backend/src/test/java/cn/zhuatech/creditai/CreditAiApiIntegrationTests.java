/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.creditai;
import org.junit.jupiter.api.*; import org.springframework.beans.factory.annotation.Autowired; import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc; import org.springframework.boot.test.context.SpringBootTest; import org.springframework.http.MediaType; import org.springframework.test.web.servlet.MockMvc; import java.util.regex.*; import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*; import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@SpringBootTest @AutoConfigureMockMvc class CreditAiApiIntegrationTests { @Autowired MockMvc mvc; private String token;
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 @BeforeEach void login()throws Exception{String json=mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"operator\",\"password\":\"Demo@2026\"}")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();Matcher m=Pattern.compile("\\\"token\\\":\\\"([^\\\"]+)\\\"").matcher(json);if(!m.find())throw new AssertionError("token missing");token=m.group(1);}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 @Test void operatorCanAssessCredit()throws Exception{mvc.perform(post("/api/ai/credit/assess").header("Authorization","Bearer "+token).contentType(MediaType.APPLICATION_JSON).content("{\"customerCode\":\"CUS-88\",\"annualRevenue\":2000000,\"outstandingReceivable\":600000,\"overdueDays\":120,\"latePayments12Months\":5,\"yearsInBusiness\":1,\"externalRiskScore\":82}")).andExpect(status().isOk()).andExpect(jsonPath("$.data.riskLevel").value("HIGH"));}}
