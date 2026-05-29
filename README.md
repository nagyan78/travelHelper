# 旅游攻略制作平台 (Travel Itinerary)

## 🎯 项目简介

基于AI的智能化旅游攻略生成平台，**专注游玩攻略**，不做住行捆绑，提供纯粹、客观、灵活的旅行规划服务。

用户通过**自然语言描述旅行偏好**（如"我喜欢美食和博物馆"），系统智能分析8个维度权重，结合预算自动生成个性化攻略，支持拖拽编辑和**自定义空闲时间**。

---

## ✨ 核心优势

### vs 携程AI对比

| 维度 | 携程AI | 本项目 |
|------|--------|--------|
| **输入方式** | 下拉选择固定风格 | ✅ 自然语言自由输入 |
| **个性化程度** | 中等(标准化) | ✅ 8维度细粒度分析 |
| **产品复杂度** | ⭐⭐⭐⭐⭐ 非常复杂 | ⭐⭐ 简洁易用 |
| **商业干扰** | 捆绑销售较多 | ✅ 完全无干扰 |
| **生成速度** | 3-5分钟 | ✅ 30秒-1分钟 |
| **灵活性** | 固定模板 | ✅ 自由编辑+空闲时间 |
| **价格透明度** | 隐性消费 | ✅ 动态预算分配 |

**定位**: 不是另一个携程，而是专注于"游玩攻略"的垂直领域专家

---

## 🚀 核心功能

### 基础功能
- 🤖 **智能攻略生成**: 通义千问AI大模型一键生成
- 📝 **可视化编辑**: 拖拽式增删改操作
- 🌤️ **实时数据**: 天气、门票、交通信息
- 💾 **攻略管理**: 查看、编辑、删除攻略

### ⭐ 亮点功能

#### 1. 智能偏好分析 (新增)
```
输入方式: 自然语言描述 (如"我喜欢美食和博物馆")
AI分析: 8维度权重 (美食/文化/自然/休闲/亲子/摄影/购物/探险)
输出结果: 权重分布 + 预算分配建议
核心价值: 零学习成本，真正的个性化
```

#### 2. 智能预算优化器
```
三种方案: 经济型 / 舒适型 / 豪华型
动态分配: 基于偏好权重调整各项费用比例
详细分解: 交通/餐饮/门票/购物各项明细
省钱技巧: 个性化省钱建议
```

#### 3. 空闲时间管理 (新增)
```
功能: 标记时间段为自由活动/休息调整/机动时间
类型: 🎯自由活动 | ☕休息调整 | ⏰机动时间
价值: 尊重用户自主安排，更人性化
UI: 特殊颜色区分，支持备注说明
```

#### 4. AI旅行助手
```
实时问答: "附近有什么好吃的？"
智能推荐: 基于当前位置推荐景点
行程优化: 分析行程并给出改进建议
```

---

## 🛠️ 技术栈

### 后端
- **Java**: 11.0.16.101
- **Spring Boot**: 2.7.18
- **MyBatis-Plus**: 3.5.5
- **MySQL**: 8.0
- **Redis**: 5.0.14
- **RabbitMQ**: 4.2.3
- **JWT**: jjwt 0.11.5

### 前端
- HTML5/CSS3 + JavaScript ES6+
- SortableJS (拖拽组件)

### 第三方服务
- 阿里云通义千问API (AI大模型 + 语义分析)
- 和风天气API (天气预报)
- 高德地图API (交通路线)

---

## 📁 项目结构

```
travel/
├── docs/                           # 设计文档
│   ├── 01_概要设计说明书(HLD).md
│   ├── 02_详细设计说明书(LLD).md
│   ├── 03_数据库设计文档.md
│   ├── 04_接口协议文档(API文档).md
│   ├── 05_可扩展性与竞品分析.md
│   └── 06_新增功能详细设计.md      ⭐ 新增
│
├── travel-backend/                 # 后端项目
│   ├── src/main/java/com/travel/itinerary/
│   │   ├── module/
│   │   │   ├── analyzer/          # ⭐智能偏好分析模块
│   │   │   ├── itinerary/         # 攻略模块
│   │   │   ├── ai/                # AI服务
│   │   │   ├── assistant/         # AI助手
│   │   │   └── budget/            # 预算优化
│   │   └── TravelApplication.java
│   └── pom.xml
│
├── travel-frontend/                # 前端项目
│   └── index.html
│
└── README.md
```

---

## ⚡ 快速开始

### 环境要求
```bash
✅ JDK 11.0.16+
✅ Maven 3.9.10+
✅ MySQL 8.0
✅ Redis 5.0.14+
✅ RabbitMQ 4.2.3+
```

### 1. 数据库初始化
```bash
mysql -u root -p << EOF
CREATE DATABASE travel_itinerary DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
EOF

mysql -u root -p travel_itinerary < docs/schema.sql
```

### 2. 配置修改

编辑 `travel-backend/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    username: root
    password: your_db_password
  
aliyun:
  tongyi:
    api-key: your_tongyi_api_key  # 通义千问API Key

weather:
  api-key: your_weather_api_key

amap:
  api-key: your_amap_api_key
```

### 3. 启动后端
```bash
cd travel-backend
mvn clean package
java -jar target/travel-itinerary-1.0.0.jar
```

访问Swagger文档: http://localhost:8080/doc.html

---

## 🔌 API使用示例

### 1. 智能偏好分析 ⭐新增
```bash
curl -X POST http://localhost:8080/api/analyzer/analyze \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "userInput": "我喜欢美食和博物馆，想要轻松一点的行程"
  }'
```

**响应**:
```json
{
  "foodIndex": 85,
  "cultureDepth": 90,
  "leisureLevel": 80,
  "topDimension": "文化"
}
```

### 2. 生成预算建议 ⭐新增
```bash
curl -X POST http://localhost:8080/api/analyzer/budget \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "userInput": "我喜欢美食",
    "totalBudget": 5000
  }'
```

**响应**:
```json
{
  "foodCost": 1750.00,
  "ticketCost": 1250.00,
  "transportCost": 1250.00,
  "shoppingCost": 500.00,
  "reserveCost": 250.00
}
```

### 3. 保存包含空闲时间的攻略 ⭐新增
```bash
curl -X POST http://localhost:8080/api/itinerary/save \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "title": "北京3日游",
    "preferenceDescription": "我喜欢美食",
    "dailyPlans": [{
      "dayNumber": 1,
      "spots": [
        {
          "title": "故宫",
          "type": "spot",
          "startTime": "09:00",
          "endTime": "11:00"
        },
        {
          "title": "☕ 休息调整",
          "type": "free",
          "freeTimeType": "rest",
          "freeTimeNotes": "咖啡厅休息",
          "startTime": "11:00",
          "endTime": "12:00"
        }
      ]
    }]
  }'
```

---

## 📊 智能偏好分析详解

### 8个核心维度

| 维度 | 说明 | 影响要素 |
|------|------|---------|
| **美食指数** | 对当地美食的关注度 | 餐饮预算、餐厅推荐数量 |
| **文化深度** | 历史文化景点偏好 | 门票预算、博物馆/古迹数量 |
| **自然风光** | 自然景观偏好 | 交通预算、户外景点数量 |
| **休闲程度** | 行程松紧度 | 每日景点数量、休息时间 |
| **亲子友好** | 是否带儿童 | 亲子活动、安全考虑 |
| **摄影需求** | 拍照打卡需求 | 观景台、网红景点 |
| **购物倾向** | 购物兴趣 | 购物预算、商圈安排 |
| **探险精神** | 刺激体验需求 | 特殊活动、极限运动 |

### 工作流程

```
用户输入: "我喜欢美食和博物馆"
    ↓
NLP语义分析 → 提取关键词: [美食, 博物馆]
    ↓
规则映射 → 美食指数+90, 文化深度+95
    ↓
归一化处理 → 转换为0-100权重
    ↓
AI二次校准 → 通义千问语义理解优化
    ↓
输出结果 → 权重分布 + 预算建议
```

---

## 🎯 发展路线图

### Phase 1: MVP版本 (1-2个月)
- [ ] 基础框架搭建
- [ ] 用户模块实现
- [ ] ⭐智能偏好分析上线
- [ ] 攻略生成功能
- [ ] ⭐空闲时间管理上线
- [ ] AI助手上线

### Phase 2: 功能完善 (2-4个月)
- [ ] 拖拽编辑器增强
- [ ] 智能提醒助手
- [ ] 攻略分享社区
- [ ] 移动端适配

### Phase 3: 商业化探索 (4-6个月)
- [ ] 会员体系
- [ ] 高级模板市场
- [ ] API开放平台

---

## 📞 技术支持

- 📖 详细设计文档: `docs/` 目录
- 🆕 新增功能设计: `docs/06_新增功能详细设计.md`
- 🌐 Swagger: http://localhost:8080/doc.html

---

**祝您旅途愉快! 让AI帮您规划完美行程! ✈️🌍**
