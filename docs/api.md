# CreditAI API 摘要

版权所有 © 2026 上海如静知华信息科技有限公司。

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/api/auth/login` | 登录并获取 JWT |
| GET | `/api/admin/dashboard` | 企业信用风险控制中心 |
| GET | `/api/admin/work-orders` | 信用评估任务 |
| GET | `/api/shopfloor/dashboard` | 信用分析师工作台 |
| POST | `/api/shopfloor/work-orders/{id}/reports` | 提交评估反馈 |
| POST | `/api/ai/credit/assess` | 风险等级、可用额度和账期建议 |
| POST | `/api/shopfloor/ai-risk-assessment` | AI 功能上线风险初筛 |

除登录外均需 JWT；社区版不连接真实征信机构。
