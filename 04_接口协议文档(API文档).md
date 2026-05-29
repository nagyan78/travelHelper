# 旅游攻略制作网站 - 接口协议文档(API文档)

## 1. 文档概述

### 1.1 文档目的
本文档定义系统所有对外提供的RESTful API接口，包括请求地址、请求参数、响应格式等，供前端开发和第三方集成使用。

### 1.2 适用范围
- 前端开发工程师对接接口
- 移动端APP开发
- 第三方系统集成
- 接口测试

### 1.3 基础信息
- **Base URL**: `http://localhost:8080/api` (开发环境)
- **Content-Type**: `application/json`
- **字符编码**: `UTF-8`
- **认证方式**: JWT Token (Bearer Token)

### 1.4 通用规范

#### 1.4.1 统一请求格式
```json
{
  "Content-Type": "application/json",
  "Authorization": "Bearer {token}"
}
```

#### 1.4.2 统一响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

#### 1.4.3 分页响应格式
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 100,
    "page": 1,
    "size": 10,
    "list": []
  }
}
```

#### 1.4.4 错误响应格式
```json
{
  "code": 400,
  "message": "参数错误",
  "data": null
}
```

### 1.5 状态码说明

| 状态码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权/Token失效 |
| 403 | 禁止访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

### 1.6 业务错误码

| 错误码 | 说明 |
|--------|------|
| 1001 | 用户名已存在 |
| 1002 | 用户不存在 |
| 1003 | 密码错误 |
| 2001 | 攻略不存在 |
| 2002 | 攻略保存失败 |
| 3001 | AI服务调用失败 |
| 3002 | AI响应解析失败 |
| 4001 | 天气API调用失败 |
| 4002 | 地图API调用失败 |

## 2. 用户模块 (User API)

### 2.1 用户注册

**接口地址**: `POST /api/user/register`

**请求参数**:
```json
{
  "username": "string, 必填, 用户名(3-20位字母数字下划线)",
  "password": "string, 必填, 密码(6-20位)",
  "email": "string, 可选, 邮箱地址",
  "phone": "string, 可选, 手机号"
}
```

**请求示例**:
```json
{
  "username": "zhangsan",
  "password": "123456",
  "email": "zhangsan@example.com",
  "phone": "13800138000"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "zhangsan",
    "nickname": null,
    "avatar": null,
    "createTime": "2024-01-01 12:00:00"
  }
}
```

---

### 2.2 用户登录

**接口地址**: `POST /api/user/login`

**请求参数**:
```json
{
  "username": "string, 必填, 用户名",
  "password": "string, 必填, 密码"
}
```

**请求示例**:
```json
{
  "username": "zhangsan",
  "password": "123456"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expireTime": 1800,
    "userInfo": {
      "id": 1,
      "username": "zhangsan",
      "nickname": "张三",
      "avatar": "https://example.com/avatar.jpg",
      "email": "zhangsan@example.com",
      "phone": "13800138000"
    }
  }
}
```

---

### 2.3 获取用户信息

**接口地址**: `GET /api/user/info`

**请求头**:
```
Authorization: Bearer {token}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "zhangsan",
    "nickname": "张三",
    "avatar": "https://example.com/avatar.jpg",
    "email": "zhangsan@example.com",
    "phone": "13800138000",
    "gender": 1,
    "birthday": "1990-01-01",
    "signature": "热爱旅行"
  }
}
```

---

### 2.4 更新用户信息

**接口地址**: `PUT /api/user/info`

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
```json
{
  "nickname": "string, 可选, 昵称",
  "avatar": "string, 可选, 头像URL",
  "email": "string, 可选, 邮箱",
  "phone": "string, 可选, 手机号",
  "gender": "int, 可选, 性别: 0-未知, 1-男, 2-女",
  "birthday": "string, 可选, 生日(YYYY-MM-DD)",
  "signature": "string, 可选, 个性签名"
}
```

**请求示例**:
```json
{
  "nickname": "张三",
  "gender": 1,
  "signature": "世界那么大，我想去看看"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 2.5 用户登出

**接口地址**: `POST /api/user/logout`

**请求头**:
```
Authorization: Bearer {token}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

## 3. 攻略模块 (Itinerary API)

### 3.1 生成攻略

**接口地址**: `POST /api/itinerary/generate`

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
```json
{
  "destination": "string, 必填, 目的地城市",
  "days": "int, 必填, 旅行天数",
  "startDate": "string, 必填, 开始日期(YYYY-MM-DD)",
  "endDate": "string, 必填, 结束日期(YYYY-MM-DD)",
  "style": "string, 必填, 旅行风格(leisure/intensive/culture/food/adventure/shopping/family/romantic)",
  "budget": "decimal, 必填, 总预算",
  "ticketBudget": "decimal, 可选, 门票预算",
  "transportBudget": "decimal, 可选, 交通预算",
  "foodBudget": "decimal, 可选, 美食预算",
  "startTime": "string, 可选, 每日开始时间(HH:mm), 默认09:00",
  "duration": "int, 可选, 每日游玩时长(小时), 默认8",
  "spots": "array, 可选, 想去的景点列表",
  "preferences": "array, 可选, 偏好标签"
}
```

**请求示例**:
```json
{
  "destination": "北京",
  "days": 3,
  "startDate": "2024-02-01",
  "endDate": "2024-02-03",
  "style": "culture",
  "budget": 5000.00,
  "ticketBudget": 1000.00,
  "transportBudget": 800.00,
  "foodBudget": 1200.00,
  "startTime": "09:00",
  "duration": 8,
  "spots": ["故宫", "长城", "颐和园"],
  "preferences": ["历史文化", "特色美食"]
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1234567890,
    "title": "北京三日文化之旅",
    "destination": "北京",
    "days": 3,
    "startDate": "2024-02-01",
    "endDate": "2024-02-03",
    "budget": 5000.00,
    "style": "culture",
    "htmlContent": "<!DOCTYPE html><html>...</html>",
    "dailyPlans": [
      {
        "day": 1,
        "date": "2024-02-01",
        "theme": "皇家文化探索",
        "departureTime": "09:00",
        "weather": {
          "condition": "晴",
          "tempMin": -5,
          "tempMax": 5
        },
        "timeSlots": [
          {
            "time": "09:00",
            "activity": "到达故宫",
            "spotName": "故宫博物院",
            "ticketPrice": 60.00,
            "needBooking": true,
            "duration": 180,
            "transportFromPrev": null,
            "transportCost": 0
          },
          {
            "time": "12:00",
            "activity": "午餐 - 全聚德烤鸭",
            "spotName": null,
            "ticketPrice": null,
            "needBooking": false,
            "duration": 60,
            "transportFromPrev": "步行",
            "transportCost": 0
          }
        ],
        "foods": ["北京烤鸭", "炸酱面", "豆汁"],
        "gifts": ["景泰蓝", "北京布鞋"]
      }
    ]
  }
}
```

---

### 3.2 保存攻略

**接口地址**: `POST /api/itinerary/save`

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
```json
{
  "title": "string, 必填, 攻略标题",
  "destination": "string, 必填, 目的地",
  "days": "int, 必填, 旅行天数",
  "startDate": "string, 必填, 开始日期(YYYY-MM-DD)",
  "endDate": "string, 必填, 结束日期(YYYY-MM-DD)",
  "budget": "decimal, 可选, 总预算",
  "ticketBudget": "decimal, 可选, 门票预算",
  "transportBudget": "decimal, 可选, 交通预算",
  "foodBudget": "decimal, 可选, 美食预算",
  "style": "string, 可选, 旅行风格",
  "htmlContent": "string, 必填, HTML内容",
  "jsonData": "object, 可选, JSON结构化数据",
  "coverImage": "string, 可选, 封面图片URL",
  "tags": "array, 可选, 标签列表"
}
```

**请求示例**:
```json
{
  "title": "北京三日文化之旅",
  "destination": "北京",
  "days": 3,
  "startDate": "2024-02-01",
  "endDate": "2024-02-03",
  "budget": 5000.00,
  "style": "culture",
  "htmlContent": "<!DOCTYPE html><html>...</html>",
  "tags": ["文化", "历史", "北京"]
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": 1
}
```

---

### 3.3 获取攻略详情

**接口地址**: `GET /api/itinerary/{id}`

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
- `id`: 攻略ID

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "userId": 1,
    "title": "北京三日文化之旅",
    "destination": "北京",
    "days": 3,
    "startDate": "2024-02-01",
    "endDate": "2024-02-03",
    "budget": 5000.00,
    "style": "culture",
    "htmlContent": "<!DOCTYPE html><html>...</html>",
    "viewCount": 100,
    "likeCount": 10,
    "shareCount": 5,
    "status": 1,
    "createTime": "2024-01-01 12:00:00",
    "updateTime": "2024-01-01 12:00:00"
  }
}
```

---

### 3.4 获取攻略列表

**接口地址**: `GET /api/itinerary/list`

**请求头**:
```
Authorization: Bearer {token}
```

**查询参数**:
- `page`: 页码, 默认1
- `size`: 每页大小, 默认10
- `destination`: 可选, 目的地筛选
- `status`: 可选, 状态筛选(0-草稿, 1-已发布)

**请求示例**:
```
GET /api/itinerary/list?page=1&size=10&status=1
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 25,
    "page": 1,
    "size": 10,
    "list": [
      {
        "id": 1,
        "title": "北京三日文化之旅",
        "destination": "北京",
        "days": 3,
        "startDate": "2024-02-01",
        "endDate": "2024-02-03",
        "budget": 5000.00,
        "style": "culture",
        "coverImage": "https://example.com/cover.jpg",
        "viewCount": 100,
        "likeCount": 10,
        "createTime": "2024-01-01 12:00:00"
      }
    ]
  }
}
```

---

### 3.5 更新攻略

**接口地址**: `PUT /api/itinerary/{id}`

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
- `id`: 攻略ID

**请求参数**:
```json
{
  "title": "string, 可选, 攻略标题",
  "budget": "decimal, 可选, 总预算",
  "style": "string, 可选, 旅行风格",
  "htmlContent": "string, 可选, HTML内容",
  "jsonData": "object, 可选, JSON数据",
  "coverImage": "string, 可选, 封面图片",
  "tags": "array, 可选, 标签列表"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 3.6 删除攻略

**接口地址**: `DELETE /api/itinerary/{id}`

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
- `id`: 攻略ID

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 3.7 收藏攻略

**接口地址**: `POST /api/itinerary/{id}/favorite`

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
- `id`: 攻略ID

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 3.8 取消收藏

**接口地址**: `DELETE /api/itinerary/{id}/favorite`

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
- `id`: 攻略ID

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 3.9 获取热门攻略

**接口地址**: `GET /api/itinerary/hot`

**查询参数**:
- `page`: 页码, 默认1
- `size`: 每页大小, 默认10

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 100,
    "page": 1,
    "size": 10,
    "list": [
      {
        "id": 1,
        "title": "北京三日文化之旅",
        "destination": "北京",
        "days": 3,
        "viewCount": 1000,
        "likeCount": 100
      }
    ]
  }
}
```

## 4. 天气模块 (Weather API)

### 4.1 获取天气预报

**接口地址**: `GET /api/weather/forecast`

**请求头**:
```
Authorization: Bearer {token}
```

**查询参数**:
- `city`: 城市名称
- `days`: 天数, 默认7天

**请求示例**:
```
GET /api/weather/forecast?city=北京&days=7
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "date": "2024-02-01",
      "condition": "晴",
      "tempMin": -5,
      "tempMax": 5,
      "windDirection": "北风",
      "windLevel": "3-4级"
    },
    {
      "date": "2024-02-02",
      "condition": "多云",
      "tempMin": -3,
      "tempMax": 6,
      "windDirection": "南风",
      "windLevel": "2-3级"
    }
  ]
}
```

## 5. 景点模块 (Spot API)

### 5.1 搜索景点

**接口地址**: `GET /api/spot/search`

**请求头**:
```
Authorization: Bearer {token}
```

**查询参数**:
- `keyword`: 搜索关键词
- `city`: 城市名称, 可选

**请求示例**:
```
GET /api/spot/search?keyword=故宫&city=北京
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "name": "故宫博物院",
      "address": "北京市东城区景山前街4号",
      "latitude": 39.916345,
      "longitude": 116.397155,
      "ticketPrice": 60.00,
      "needBooking": true,
      "bookingAdvanceDays": 7,
      "openingHours": "08:30-17:00",
      "duration": 180,
      "description": "中国明清两代的皇家宫殿"
    }
  ]
}
```

---

### 5.2 获取景点详情

**接口地址**: `GET /api/spot/detail/{name}`

**请求头**:
```
Authorization: Bearer {token}
```

**路径参数**:
- `name`: 景点名称(URL编码)

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "name": "故宫博物院",
    "address": "北京市东城区景山前街4号",
    "latitude": 39.916345,
    "longitude": 116.397155,
    "ticketPrice": 60.00,
    "needBooking": true,
    "bookingAdvanceDays": 7,
    "openingHours": "08:30-17:00",
    "duration": 180,
    "description": "中国明清两代的皇家宫殿",
    "images": [
      "https://example.com/image1.jpg",
      "https://example.com/image2.jpg"
    ],
    "tips": [
      "建议提前网上购票",
      "周一闭馆",
      "携带身份证"
    ]
  }
}
```

## 6. 交通模块 (Traffic API)

### 6.1 查询路线规划

**接口地址**: `GET /api/traffic/route`

**请求头**:
```
Authorization: Bearer {token}
```

**查询参数**:
- `origin`: 起点(经纬度或地址)
- `destination`: 终点(经纬度或地址)
- `mode`: 交通方式(walk/bike/subway/bus/taxi/car), 可选, 默认all

**请求示例**:
```
GET /api/traffic/route?origin=故宫&destination=长城&mode=all
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "mode": "subway",
      "duration": 120,
      "distance": 60000,
      "cost": 7.00,
      "steps": [
        "步行至天安门东站",
        "乘坐地铁1号线",
        "换乘地铁2号线",
        "步行至目的地"
      ]
    },
    {
      "mode": "taxi",
      "duration": 60,
      "distance": 60000,
      "cost": 180.00,
      "steps": [
        "直接驾车前往"
      ]
    }
  ]
}
```

## 7. AI模块 (AI API)

### 7.1 AI问答

**接口地址**: `POST /api/ai/chat`

**请求头**:
```
Authorization: Bearer {token}
```

**请求参数**:
```json
{
  "question": "string, 必填, 问题内容",
  "context": "string, 可选, 上下文信息"
}
```

**请求示例**:
```json
{
  "question": "去北京旅游最佳季节是什么时候？",
  "context": "用户计划去北京旅游3天"
}
```

**响应示例**:
``json
{
  "code": 200,
  "message": "success",
  "data": {
    "answer": "北京旅游的最佳季节是春季(3-5月)和秋季(9-11月)。这两个季节气候宜人...\n\n具体建议:\n1. 春季可以欣赏花开\n2. 秋季秋高气爽，适合户外活动",
    "relatedQuestions": [
      "北京有哪些必去景点？",
      "北京特色美食推荐"
    ]
  }
}
```

## 8. 文件上传模块 (Upload API)

### 8.1 上传图片

**接口地址**: `POST /api/upload/image`

**请求头**:
```
Authorization: Bearer {token}
Content-Type: multipart/form-data
```

**请求参数**:
- `file`: 图片文件(支持jpg/png/gif, 最大5MB)

**请求示例**:
```
POST /api/upload/image
FormData: file=[binary]
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "url": "https://example.com/uploads/2024/01/01/abc123.jpg",
    "filename": "abc123.jpg",
    "size": 102400
  }
}
```

## 9. 统计模块 (Statistics API)

### 9.1 获取用户统计

**接口地址**: `GET /api/statistics/user`

**请求头**:
```
Authorization: Bearer {token}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "itineraryCount": 25,
    "favoriteCount": 10,
    "totalViewCount": 5000,
    "totalLikeCount": 200
  }
}
```

## 10. 智能偏好分析模块 (Analyzer API) ⭐新增

### 10.1 分析旅行偏好

**接口地址**: `POST /api/analyzer/analyze`

**功能说明**: 解析用户自然语言描述的旅行偏好，返回8个维度的权重分布

**请求头**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求参数**:
```json
{
  "userInput": "我喜欢美食和博物馆，想要轻松一点的行程"
}
```

**参数说明**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userInput | String | 是 | 用户自由输入的旅行偏好描述 |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "foodIndex": 85,
    "cultureDepth": 90,
    "natureScenery": 40,
    "leisureLevel": 80,
    "parentChild": 20,
    "photography": 50,
    "shopping": 30,
    "adventure": 15,
    "topDimension": "文化"
  }
}
```

**响应字段说明**:

| 字段名 | 类型 | 说明 |
|--------|------|------|
| foodIndex | Integer | 美食指数权重 (0-100) |
| cultureDepth | Integer | 文化深度权重 (0-100) |
| natureScenery | Integer | 自然风光权重 (0-100) |
| leisureLevel | Integer | 休闲程度权重 (0-100) |
| parentChild | Integer | 亲子友好权重 (0-100) |
| photography | Integer | 摄影需求权重 (0-100) |
| shopping | Integer | 购物倾向权重 (0-100) |
| adventure | Integer | 探险精神权重 (0-100) |
| topDimension | String | 最高权重的维度名称 |

---

### 10.2 生成预算建议

**接口地址**: `POST /api/analyzer/budget`

**功能说明**: 基于用户偏好权重和总预算，生成个性化的预算分配方案

**请求头**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求参数**:
```json
{
  "userInput": "我喜欢美食和博物馆",
  "totalBudget": 5000
}
```

**参数说明**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userInput | String | 是 | 用户自由输入的旅行偏好描述 |
| totalBudget | BigDecimal | 是 | 总预算金额(元) |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "foodCost": 1750.00,
    "ticketCost": 1500.00,
    "transportCost": 1250.00,
    "shoppingCost": 250.00,
    "reserveCost": 250.00
  }
}
```

**响应字段说明**:

| 字段名 | 类型 | 说明 |
|--------|------|------|
| foodCost | BigDecimal | 餐饮费用(元) |
| ticketCost | BigDecimal | 门票费用(元) |
| transportCost | BigDecimal | 交通费用(元) |
| shoppingCost | BigDecimal | 购物费用(元) |
| reserveCost | BigDecimal | 备用金(元) |

---

## 11. 攻略管理模块增强 (Itinerary API Enhanced) ⭐更新

### 11.1 保存攻略(支持空闲时间)

**接口地址**: `POST /api/itinerary/save`

**功能说明**: 保存攻略，支持包含空闲时间段

**请求头**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**请求参数**:
```json
{
  "title": "北京3日游",
  "destination": "北京",
  "days": 3,
  "startDate": "2024-02-01",
  "endDate": "2024-02-03",
  "budget": 5000,
  "preferenceDescription": "我喜欢美食和博物馆",
  "dailyPlans": [
    {
      "dayNumber": 1,
      "date": "2024-02-01",
      "spots": [
        {
          "title": "故宫博物院",
          "type": "spot",
          "startTime": "09:00",
          "endTime": "11:00",
          "location": "北京市东城区景山前街4号",
          "cost": 60,
          "notes": "需提前预约"
        },
        {
          "title": "☕ 休息调整",
          "type": "free",
          "freeTimeType": "rest",
          "freeTimeNotes": "在附近咖啡厅休息一下",
          "startTime": "11:00",
          "endTime": "12:00"
        },
        {
          "title": "全聚德烤鸭",
          "type": "food",
          "startTime": "12:00",
          "endTime": "13:30",
          "location": "前门大街",
          "cost": 150
        }
      ]
    }
  ]
}
```

**新增字段说明**:

| 字段名 | 类型 | 说明 |
|--------|------|------|
| preferenceDescription | String | 用户偏好描述(原始输入) |
| spots[].type | String | 类型: spot-景点, food-餐饮, traffic-交通, **free-空闲时间** |
| spots[].freeTimeType | String | ⭐空闲时间类型: activity-自由活动, rest-休息调整, flexible-机动时间 |
| spots[].freeTimeNotes | String | ⭐空闲时间备注说明 |

**响应示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "itineraryId": 123
  }
}
```

---

## 12. 接口调用示例(更新)

### 12.1 JavaScript Fetch示例(新增)

```javascript
// 分析旅行偏好
async function analyzePreference(userInput) {
  const token = localStorage.getItem('token');
  const response = await fetch('http://localhost:8080/api/analyzer/analyze', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify({
      userInput: userInput
    })
  });
  
  const result = await response.json();
  if (result.code === 200) {
    return result.data; // 返回8维度权重
  } else {
    throw new Error(result.message);
  }
}

// 生成预算建议
async function suggestBudget(userInput, totalBudget) {
  const token = localStorage.getItem('token');
  const response = await fetch('http://localhost:8080/api/analyzer/budget', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify({
      userInput: userInput,
      totalBudget: totalBudget
    })
  });
  
  const result = await response.json();
  if (result.code === 200) {
    return result.data; // 返回预算分配
  } else {
    throw new Error(result.message);
  }
}

// 添加空闲时间到日程
function addFreeTimeToDay(dayPlan, startTime, endTime, type, notes) {
  dayPlan.spots.push({
    title: getFreeTimeLabel(type),
    type: 'free',
    freeTimeType: type,
    freeTimeNotes: notes,
    startTime: startTime,
    endTime: endTime
  });
}

function getFreeTimeLabel(type) {
  const labels = {
    'activity': '🎯 自由活动',
    'rest': '☕ 休息调整',
    'flexible': '⏰ 机动时间'
  };
  return labels[type] || '空闲时间';
}

// 完整使用示例
async function createSmartItinerary() {
  // 1. 分析用户偏好
  const weights = await analyzePreference("我喜欢美食和博物馆");
  console.log('偏好权重:', weights);
  
  // 2. 生成预算建议
  const budget = await suggestBudget("我喜欢美食和博物馆", 5000);
  console.log('预算分配:', budget);
  
  // 3. 创建日程并添加空闲时间
  const dayPlan = {
    dayNumber: 1,
    date: "2024-02-01",
    spots: []
  };
  
  // 添加景点
  dayPlan.spots.push({
    title: "故宫博物院",
    type: "spot",
    startTime: "09:00",
    endTime: "11:00"
  });
  
  // 添加空闲时间
  addFreeTimeToDay(dayPlan, "11:00", "12:00", "rest", "休息一下");
  
  // 添加午餐
  dayPlan.spots.push({
    title: "全聚德烤鸭",
    type: "food",
    startTime: "12:00",
    endTime: "13:30"
  });
  
  // 4. 保存攻略
  const itineraryData = {
    title: "北京3日游",
    destination: "北京",
    days: 3,
    preferenceDescription: "我喜欢美食和博物馆",
    dailyPlans: [dayPlan]
  };
  
  const result = await saveItinerary(itineraryData);
  console.log('攻略ID:', result.itineraryId);
}
```

---

## 13. 接口限流说明

### 13.1 限流规则

| 接口类型 | 限流策略 |
|---------|---------|
| 登录注册 | 同一IP每分钟最多5次 |
| 攻略生成 | 同一用户每分钟最多3次 |
| 普通查询 | 同一用户每秒最多20次 |
| 文件上传 | 同一用户每分钟最多10次 |

### 13.2 限流响应
当触发限流时，返回:
```json
{
  "code": 429,
  "message": "请求过于频繁，请稍后重试",
  "data": null
}
```

## 14. 版本管理

### 14.1 API版本
当前版本: v1 (默认不显示在URL中)

未来如需升级，可在URL中加入版本号:
- `/api/v1/user/login`
- `/api/v2/user/login`

### 14.2 兼容性保证
- 向后兼容至少一个版本
- 废弃的API会标记为@Deprecated
- 重大变更会增加新版本号

## 15. 安全建议

### 15.1 Token管理
- Token有效期: 30分钟
- 建议实现Refresh Token机制
- 不要在客户端明文存储Token

### 15.2 HTTPS
- 生产环境必须使用HTTPS
- 敏感数据传输必须加密

### 15.3 输入验证
- 所有参数都需要进行合法性校验
- 防止SQL注入、XSS攻击
- 文件上传需要类型和大小限制

## 16. 附录

### 16.1 Swagger文档
系统集成了Swagger UI，可通过以下地址访问:
```
http://localhost:8080/swagger-ui.html
```

### 16.2 Postman集合
提供Postman Collection文件，方便接口测试

### 16.3 联系方式
如有接口相关问题，请联系后端开发团队
