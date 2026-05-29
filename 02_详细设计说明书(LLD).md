# 旅游攻略制作网站 - 详细设计说明书(LLD)

## 1. 文档概述

### 1.1 文档目的
本文档对系统进行详细的类级别设计，包括类图、时序图、方法签名、数据结构等，为开发人员提供直接的编码指导。

### 1.2 适用范围
- 类结构设计
- 接口定义
- 数据表字段设计
- 业务流程时序图
- 算法实现细节

### 1.3 读者对象
- 后端开发工程师
- 前端开发工程师
- 测试工程师
- 系统架构师

## 2. 系统包结构

### 2.1 后端包结构

```
com.travel.itinerary
├── common                          # 公共模块
│   ├── config                      # 配置类
│   │   ├── SwaggerConfig.java
│   │   ├── RedisConfig.java
│   │   ├── MQConfig.java
│   │   ├── SecurityConfig.java
│   │   └── AliYunConfig.java
│   ├── constant                    # 常量定义
│   │   ├── ApiConstant.java
│   │   ├── CacheConstant.java
│   │   └── MQConstant.java
│   ├── exception                   # 异常处理
│   │   ├── BusinessException.java
│   │   ├── GlobalExceptionHandler.java
│   │   └── ErrorCode.java
│   ├── result                      # 统一返回
│   │   └── Result.java
│   └── util                        # 工具类
│       ├── JwtUtil.java
│       ├── RedisUtil.java
│       └── HtmlUtil.java
├── module                          # 业务模块
│   ├── user                        # 用户模块
│   │   ├── controller
│   │   │   └── UserController.java
│   │   ├── service
│   │   │   ├── UserService.java
│   │   │   └── impl\UserServiceImpl.java
│   │   ├── mapper
│   │   │   └── UserMapper.java
│   │   ├── entity
│   │   │   └── User.java
│   │   ├── dto
│   │   │   ├── UserRegisterDTO.java
│   │   │   ├── UserLoginDTO.java
│   │   │   └── UserInfoVO.java
│   │   └── vo
│   │       └── UserInfoVO.java
│   ├── itinerary                   # 攻略模块
│   │   ├── controller
│   │   │   └── ItineraryController.java
│   │   ├── service
│   │   │   ├── ItineraryService.java
│   │   │   └── impl\ItineraryServiceImpl.java
│   │   ├── mapper
│   │   │   └── ItineraryMapper.java
│   │   ├── entity
│   │   │   ├── Itinerary.java
│   │   │   ├── ItineraryDay.java
│   │   │   └── ItinerarySpot.java
│   │   ├── dto
│   │   │   ├── ItineraryGenerateDTO.java
│   │   │   ├── ItinerarySaveDTO.java
│   │   │   └── ItineraryDetailVO.java
│   │   └── vo
│   │       └── ItineraryListVO.java
│   ├── ai                          # AI服务模块
│   │   ├── service
│   │   │   ├── AIService.java
│   │   │   └── impl\AIServiceImpl.java
│   │   ├── client
│   │   │   └── TongYiClient.java
│   │   ├── prompt
│   │   │   └── PromptTemplate.java
│   │   └── dto
│   │       ├── AIRequestDTO.java
│   │       └── AIResponseDTO.java
│   ├── weather                     # 天气服务模块
│   │   ├── service
│   │   │   ├── WeatherService.java
│   │   │   └── impl\WeatherServiceImpl.java
│   │   ├── client
│   │   │   └── WeatherClient.java
│   │   └── dto
│   │       └── WeatherDTO.java
│   ├── spot                        # 景点服务模块
│   │   ├── service
│   │   │   ├── SpotService.java
│   │   │   └── impl\SpotServiceImpl.java
│   │   ├── client
│   │   │   └── MapClient.java
│   │   └── dto
│   │       └── SpotInfoDTO.java
│   └── traffic                     # 交通服务模块
│       ├── service
│       │   ├── TrafficService.java
│       │   └── impl\TrafficServiceImpl.java
│       └── dto
│           └── TrafficRouteDTO.java
└── TravelApplication.java          # 启动类
```

### 2.2 前端目录结构

```
frontend/
├── index.html                      # 主页面
├── css/
│   ├── style.css                   # 全局样式
│   └── itinerary.css               # 攻略页面样式
├── js/
│   ├── api.js                      # API调用封装
│   ├── auth.js                     # 认证相关
│   ├── itinerary.js                # 攻略生成与展示
│   └── editor.js                   # 拖拽编辑器
├── components/                     # 组件(如使用框架)
│   ├── Header.vue
│   ├── ItineraryCard.vue
│   └── DayPlan.vue
└── assets/
    └── images/                     # 图片资源
```

## 3. 核心类详细设计

### 3.1 用户模块 (User Module)

#### 3.1.1 UserController

**类说明**: 用户控制器，处理用户相关的HTTP请求

**接口列表**:
```java
@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 用户注册
     * POST /api/user/register
     */
    @PostMapping("/register")
    public Result<UserInfoVO> register(@Valid @RequestBody UserRegisterDTO dto) {
        return Result.success(userService.register(dto));
    }
    
    /**
     * 用户登录
     * POST /api/user/login
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody UserLoginDTO dto) {
        return Result.success(userService.login(dto));
    }
    
    /**
     * 获取用户信息
     * GET /api/user/info
     */
    @GetMapping("/info")
    public Result<UserInfoVO> getUserInfo() {
        Long userId = JwtUtil.getCurrentUserId();
        return Result.success(userService.getUserInfo(userId));
    }
    
    /**
     * 更新用户信息
     * PUT /api/user/info
     */
    @PutMapping("/info")
    public Result<Void> updateUserInfo(@RequestBody UserInfoDTO dto) {
        Long userId = JwtUtil.getCurrentUserId();
        userService.updateUserInfo(userId, dto);
        return Result.success();
    }
    
    /**
     * 用户登出
     * POST /api/user/logout
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        Long userId = JwtUtil.getCurrentUserId();
        userService.logout(userId);
        return Result.success();
    }
}
```

#### 3.1.2 UserService

**类说明**: 用户服务接口

```java
public interface UserService {
    
    /**
     * 用户注册
     */
    UserInfoVO register(UserRegisterDTO dto);
    
    /**
     * 用户登录
     */
    LoginVO login(UserLoginDTO dto);
    
    /**
     * 获取用户信息
     */
    UserInfoVO getUserInfo(Long userId);
    
    /**
     * 更新用户信息
     */
    void updateUserInfo(Long userId, UserInfoDTO dto);
    
    /**
     * 登出(清除Redis中的Token)
     */
    void logout(Long userId);
}
```

#### 3.1.3 UserServiceImpl

**核心方法实现**:
```java
@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private RedisUtil redisUtil;
    
    @Override
    public UserInfoVO register(UserRegisterDTO dto) {
        // 1. 检查用户名是否已存在
        if (userMapper.existsByUsername(dto.getUsername())) {
            throw new BusinessException(ErrorCode.USERNAME_EXISTS);
        }
        
        // 2. 创建用户实体
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(BCrypt.hashpw(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setCreateTime(LocalDateTime.now());
        
        // 3. 保存数据库
        userMapper.insert(user);
        
        // 4. 返回VO
        return convertToVO(user);
    }
    
    @Override
    public LoginVO login(UserLoginDTO dto) {
        // 1. 查询用户
        User user = userMapper.findByUsername(dto.getUsername());
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_EXISTS);
        }
        
        // 2. 验证密码
        if (!BCrypt.checkpw(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_ERROR);
        }
        
        // 3. 生成JWT Token
        String token = JwtUtil.generateToken(user.getId(), user.getUsername());
        
        // 4. 存储Token到Redis (30分钟过期)
        redisUtil.set(CacheConstant.USER_TOKEN + user.getId(), token, 30, TimeUnit.MINUTES);
        
        // 5. 返回登录信息
        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setUserInfo(convertToVO(user));
        return vo;
    }
    
    private UserInfoVO convertToVO(User user) {
        UserInfoVO vo = new UserInfoVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}
```

#### 3.1.4 User实体类

```java
@Data
@TableName("t_user")
public class User {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码(加密存储)
     */
    private String password;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 状态: 0-禁用, 1-正常
     */
    private Integer status;
}
```

### 3.2 攻略模块 (Itinerary Module)

#### 3.2.1 ItineraryController

```java
@RestController
@RequestMapping("/api/itinerary")
public class ItineraryController {
    
    @Autowired
    private ItineraryService itineraryService;
    
    /**
     * 生成攻略
     * POST /api/itinerary/generate
     */
    @PostMapping("/generate")
    public Result<ItineraryDetailVO> generate(@Valid @RequestBody ItineraryGenerateDTO dto) {
        Long userId = JwtUtil.getCurrentUserId();
        return Result.success(itineraryService.generate(dto, userId));
    }
    
    /**
     * 保存攻略
     * POST /api/itinerary/save
     */
    @PostMapping("/save")
    public Result<Long> save(@Valid @RequestBody ItinerarySaveDTO dto) {
        Long userId = JwtUtil.getCurrentUserId();
        Long itineraryId = itineraryService.save(dto, userId);
        return Result.success(itineraryId);
    }
    
    /**
     * 获取攻略详情
     * GET /api/itinerary/{id}
     */
    @GetMapping("/{id}")
    public Result<ItineraryDetailVO> getDetail(@PathVariable Long id) {
        return Result.success(itineraryService.getDetail(id));
    }
    
    /**
     * 获取攻略列表
     * GET /api/itinerary/list
     */
    @GetMapping("/list")
    public Result<List<ItineraryListVO>> getList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Long userId = JwtUtil.getCurrentUserId();
        return Result.success(itineraryService.getList(userId, page, size));
    }
    
    /**
     * 更新攻略
     * PUT /api/itinerary/{id}
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, 
                               @RequestBody ItineraryUpdateDTO dto) {
        itineraryService.update(id, dto);
        return Result.success();
    }
    
    /**
     * 删除攻略
     * DELETE /api/itinerary/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        itineraryService.delete(id);
        return Result.success();
    }
}
```

#### 3.2.2 ItineraryService

```java
public interface ItineraryService {
    
    /**
     * 生成攻略(AI调用)
     */
    ItineraryDetailVO generate(ItineraryGenerateDTO dto, Long userId);
    
    /**
     * 保存攻略
     */
    Long save(ItinerarySaveDTO dto, Long userId);
    
    /**
     * 获取攻略详情
     */
    ItineraryDetailVO getDetail(Long id);
    
    /**
     * 获取攻略列表
     */
    List<ItineraryListVO> getList(Long userId, Integer page, Integer size);
    
    /**
     * 更新攻略
     */
    void update(Long id, ItineraryUpdateDTO dto);
    
    /**
     * 删除攻略
     */
    void delete(Long id);
}
```

#### 3.2.3 ItineraryServiceImpl (核心业务逻辑)

```java
@Service
@Transactional
public class ItineraryServiceImpl implements ItineraryService {
    
    @Autowired
    private ItineraryMapper itineraryMapper;
    
    @Autowired
    private AIService aiService;
    
    @Autowired
    private WeatherService weatherService;
    
    @Autowired
    private SpotService spotService;
    
    @Autowired
    private TrafficService trafficService;
    
    @Autowired
    private RedisUtil redisUtil;
    
    @Override
    public ItineraryDetailVO generate(ItineraryGenerateDTO dto, Long userId) {
        // 1. 构建AI请求
        AIRequestDTO aiRequest = buildAIRequest(dto);
        
        // 2. 调用AI服务生成攻略内容
        AIResponseDTO aiResponse = aiService.generateItinerary(aiRequest);
        
        // 3. 补充实时数据(天气、景点详情、交通)
        enrichWithRealTimeData(aiResponse, dto);
        
        // 4. 转换为HTML格式
        String htmlContent = convertToHtml(aiResponse);
        
        // 5. 生成临时攻略ID(未保存)
        Long tempId = System.currentTimeMillis();
        
        // 6. 返回详情VO
        return buildDetailVO(tempId, aiResponse, htmlContent);
    }
    
    @Override
    public Long save(ItinerarySaveDTO dto, Long userId) {
        // 1. 创建攻略主表
        Itinerary itinerary = new Itinerary();
        itinerary.setUserId(userId);
        itinerary.setTitle(dto.getTitle());
        itinerary.setDestination(dto.getDestination());
        itinerary.setDays(dto.getDays());
        itinerary.setStartDate(dto.getStartDate());
        itinerary.setEndDate(dto.getEndDate());
        itinerary.setBudget(dto.getBudget());
        itinerary.setStyle(dto.getStyle());
        itinerary.setHtmlContent(dto.getHtmlContent());
        itinerary.setStatus(1); // 正常状态
        itinerary.setCreateTime(LocalDateTime.now());
        
        itineraryMapper.insert(itinerary);
        
        // 2. 保存每日行程
        saveDailyPlans(itinerary.getId(), dto.getDailyPlans());
        
        // 3. 缓存攻略
        cacheItinerary(itinerary.getId(), itinerary);
        
        // 4. 异步记录用户偏好(用于优化推荐)
        asyncRecordUserPreference(userId, dto);
        
        return itinerary.getId();
    }
    
    /**
     * 补充实时数据
     */
    private void enrichWithRealTimeData(AIResponseDTO response, ItineraryGenerateDTO dto) {
        // 并行调用外部API提高效率
        CompletableFuture<List<WeatherDTO>> weatherFuture = 
            CompletableFuture.supplyAsync(() -> 
                weatherService.getForecast(dto.getDestination(), 
                    dto.getStartDate(), dto.getDays()));
        
        CompletableFuture<Map<String, SpotInfoDTO>> spotsFuture = 
            CompletableFuture.supplyAsync(() -> 
                spotService.getSpotDetails(response.getSpotNames()));
        
        // 等待所有API调用完成
        CompletableFuture.allOf(weatherFuture, spotsFuture).join();
        
        // 设置数据
        response.setWeatherList(weatherFuture.join());
        response.setSpotInfoMap(spotsFuture.join());
    }
    
    /**
     * 转换为HTML
     */
    private String convertToHtml(AIResponseDTO response) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'>");
        html.append("<title>").append(response.getTitle()).append("</title>");
        html.append("<style>").append(getDefaultCss()).append("</style>");
        html.append("</head><body>");
        
        // 标题
        html.append("<h1>").append(response.getTitle()).append("</h1>");
        
        // 基本信息
        html.append("<div class='basic-info'>");
        html.append("<p>旅行天数: ").append(response.getDays()).append("天</p>");
        html.append("<p>预算: ").append(response.getBudget()).append("元</p>");
        html.append("<p>风格: ").append(response.getStyle()).append("</p>");
        html.append("</div>");
        
        // 每日行程
        for (DayPlanDTO day : response.getDailyPlans()) {
            html.append("<div class='day-plan' data-day='").append(day.getDay()).append("'>");
            html.append("<h2>第").append(day.getDay()).append("天 - ")
                .append(day.getDate()).append("</h2>");
            
            // 天气
            if (day.getWeather() != null) {
                html.append("<div class='weather'>");
                html.append("<span>天气: ").append(day.getWeather().getCondition()).append("</span>");
                html.append("<span>温度: ").append(day.getWeather().getTemp()).append("℃</span>");
                html.append("</div>");
            }
            
            // 时间安排
            html.append("<ul class='timeline'>");
            for (TimeSlotDTO slot : day.getTimeSlots()) {
                html.append("<li class='time-slot' draggable='true'>");
                html.append("<span class='time'>").append(slot.getTime()).append("</span>");
                html.append("<span class='activity'>").append(slot.getActivity()).append("</span>");
                
                if (slot.getSpot() != null) {
                    html.append("<div class='spot-info'>");
                    html.append("<p>门票: ").append(slot.getSpot().getTicketPrice()).append("元</p>");
                    html.append("<p>预约: ").append(slot.getSpot().getNeedBooking() ? "需要" : "不需要").append("</p>");
                    html.append("</div>");
                }
                
                html.append("</li>");
            }
            html.append("</ul>");
            
            // 美食推荐
            if (!CollectionUtils.isEmpty(day.getFoods())) {
                html.append("<div class='foods'><h3>特色美食</h3><ul>");
                for (String food : day.getFoods()) {
                    html.append("<li>").append(food).append("</li>");
                }
                html.append("</ul></div>");
            }
            
            // 伴手礼
            if (!CollectionUtils.isEmpty(day.getGifts())) {
                html.append("<div class='gifts'><h3>伴手礼推荐</h3><ul>");
                for (String gift : day.getGifts()) {
                    html.append("<li>").append(gift).append("</li>");
                }
                html.append("</ul></div>");
            }
            
            html.append("</div>");
        }
        
        html.append("</body></html>");
        return html.toString();
    }
}
```

#### 3.2.4 攻略实体类

```java
/**
 * 攻略主表
 */
@Data
@TableName("t_itinerary")
public class Itinerary {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 攻略标题
     */
    private String title;
    
    /**
     * 目的地
     */
    private String destination;
    
    /**
     * 旅行天数
     */
    private Integer days;
    
    /**
     * 开始日期
     */
    private LocalDate startDate;
    
    /**
     * 结束日期
     */
    private LocalDate endDate;
    
    /**
     * 总预算
     */
    private BigDecimal budget;
    
    /**
     * 旅行风格(休闲、紧凑、文化、美食等)
     */
    private String style;
    
    /**
     * HTML内容
     */
    private String htmlContent;
    
    /**
     * JSON格式的行程数据(用于编辑)
     */
    private String jsonData;
    
    /**
     * 状态: 0-草稿, 1-已发布, 2-已删除
     */
    private Integer status;
    
    /**
     * 版本号
     */
    private Integer version;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

/**
 * 每日行程表
 */
@Data
@TableName("t_itinerary_day")
public class ItineraryDay {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 攻略ID
     */
    private Long itineraryId;
    
    /**
     * 第几天
     */
    private Integer dayNumber;
    
    /**
     * 日期
     */
    private LocalDate date;
    
    /**
     * 出发时间
     */
    private LocalTime departureTime;
    
    /**
     * 当日主题
     */
    private String theme;
    
    /**
     * 排序号
     */
    private Integer sortOrder;
}

/**
 * 景点时间表
 */
@Data
@TableName("t_itinerary_spot")
public class ItinerarySpot {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 日程ID
     */
    private Long dayId;
    
    /**
     * 时间点(如 09:00)
     */
    private String time;
    
    /**
     * 活动描述
     */
    private String activity;
    
    /**
     * 景点名称
     */
    private String spotName;
    
    /**
     * 景点地址
     */
    private String address;
    
    /**
     * 门票价格
     */
    private BigDecimal ticketPrice;
    
    /**
     * 是否需要预约
     */
    private Boolean needBooking;
    
    /**
     * 建议游玩时长(小时)
     */
    private Integer duration;
    
    /**
     * 交通方式(从上一点)
     */
    private String transportFromPrev;
    
    /**
     * 交通费用
     */
    private BigDecimal transportCost;
    
    /**
     * 排序号
     */
    private Integer sortOrder;
}
```

### 3.3 AI服务模块 (AI Service Module)

#### 3.3.1 AIService

```java
public interface AIService {
    
    /**
     * 生成攻略
     */
    AIResponseDTO generateItinerary(AIRequestDTO request);
}

@Service
@Slf4j
public class AIServiceImpl implements AIService {
    
    @Autowired
    private TongYiClient tongYiClient;
    
    @Autowired
    private PromptTemplate promptTemplate;
    
    @Value("${ai.model.timeout:30000}")
    private Integer timeout;
    
    @Value("${ai.model.temperature:0.7}")
    private Double temperature;
    
    @Override
    public AIResponseDTO generateItinerary(AIRequestDTO request) {
        // 1. 构建Prompt
        String prompt = promptTemplate.buildPrompt(request);
        
        log.info("调用AI生成攻略, 请求参数: {}", request);
        
        // 2. 调用通义千问API (带重试机制)
        String aiResult = null;
        int retryCount = 3;
        for (int i = 0; i < retryCount; i++) {
            try {
                aiResult = tongYiClient.call(prompt, temperature, timeout);
                break;
            } catch (Exception e) {
                log.warn("AI调用失败, 第{}次重试", i + 1, e);
                if (i == retryCount - 1) {
                    throw new BusinessException(ErrorCode.AI_SERVICE_ERROR);
                }
            }
        }
        
        log.info("AI返回结果: {}", aiResult);
        
        // 3. 解析JSON响应
        AIResponseDTO response = parseAIResponse(aiResult);
        
        // 4. 验证响应完整性
        validateResponse(response);
        
        return response;
    }
    
    /**
     * 解析AI返回的JSON
     */
    private AIResponseDTO parseAIResponse(String aiResult) {
        try {
            // 提取JSON部分(AI可能返回一些解释文字)
            String jsonStr = extractJson(aiResult);
            return JSON.parseObject(jsonStr, AIResponseDTO.class);
        } catch (Exception e) {
            log.error("解析AI响应失败", e);
            throw new BusinessException(ErrorCode.AI_PARSE_ERROR);
        }
    }
}
```

#### 3.3.2 TongYiClient (通义千问API客户端)

```java
@Component
@Slf4j
public class TongYiClient {
    
    @Value("${aliyun.tongyi.api-key}")
    private String apiKey;
    
    @Value("${aliyun.tongyi.api-url:https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation}")
    private String apiUrl;
    
    /**
     * 调用通义千问API
     */
    public String call(String prompt, Double temperature, Integer timeout) {
        RestTemplate restTemplate = new RestTemplate();
        
        // 构建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        
        // 构建请求体
        Map<String, Object> body = new HashMap<>();
        body.put("model", "qwen-max");
        
        Map<String, Object> input = new HashMap<>();
        input.put("prompt", prompt);
        body.put("input", input);
        
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("temperature", temperature);
        parameters.put("max_tokens", 4000);
        parameters.put("result_format", "json");
        body.put("parameters", parameters);
        
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        
        // 发送请求
        ResponseEntity<Map> response = restTemplate.exchange(
            apiUrl, HttpMethod.POST, entity, Map.class);
        
        // 解析响应
        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && responseBody.containsKey("output")) {
            Map<String, Object> output = (Map<String, Object>) responseBody.get("output");
            return (String) output.get("text");
        }
        
        throw new BusinessException(ErrorCode.AI_SERVICE_ERROR);
    }
}
```

#### 3.3.3 PromptTemplate (提示词模板)

```java
@Component
public class PromptTemplate {
    
    /**
     * 构建攻略生成的Prompt
     */
    public String buildPrompt(AIRequestDTO request) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("你是一个专业的旅游攻略规划师，请根据以下信息生成一份详细的旅游攻略。\n\n");
        
        prompt.append("【基本信息】\n");
        prompt.append("目的地: ").append(request.getDestination()).append("\n");
        prompt.append("旅行天数: ").append(request.getDays()).append("天\n");
        prompt.append("旅行风格: ").append(request.getStyle()).append("\n");
        prompt.append("总预算: ").append(request.getBudget()).append("元\n");
        prompt.append("出发日期: ").append(request.getStartDate()).append("\n");
        prompt.append("每日开始游玩时间: ").append(request.getStartTime()).append("\n");
        prompt.append("每日游玩时长: ").append(request.getDuration()).append("小时\n\n");
        
        prompt.append("【兴趣点】\n");
        if (!CollectionUtils.isEmpty(request.getSpots())) {
            prompt.append("想去的景点: ").append(String.join(", ", request.getSpots())).append("\n");
        }
        if (!CollectionUtils.isEmpty(request.getPreferences())) {
            prompt.append("偏好: ").append(String.join(", ", request.getPreferences())).append("\n");
        }
        prompt.append("\n");
        
        prompt.append("【预算分配】\n");
        prompt.append("门票预算: ").append(request.getTicketBudget()).append("元\n");
        prompt.append("交通预算: ").append(request.getTransportBudget()).append("元\n");
        prompt.append("美食预算: ").append(request.getFoodBudget()).append("元\n\n");
        
        prompt.append("【输出要求】\n");
        prompt.append("请以JSON格式返回，包含以下字段:\n");
        prompt.append("{\n");
        prompt.append("  \"title\": \"攻略标题\",\n");
        prompt.append("  \"days\": 旅行天数,\n");
        prompt.append("  \"budget\": 总预算,\n");
        prompt.append("  \"style\": \"旅行风格\",\n");
        prompt.append("  \"daily_plans\": [\n");
        prompt.append("    {\n");
        prompt.append("      \"day\": 第几天,\n");
        prompt.append("      \"date\": \"日期(YYYY-MM-DD)\",\n");
        prompt.append("      \"theme\": \"当日主题\",\n");
        prompt.append("      \"departure_time\": \"出发时间(HH:mm)\",\n");
        prompt.append("      \"time_slots\": [\n");
        prompt.append("        {\n");
        prompt.append("          \"time\": \"时间点(HH:mm)\",\n");
        prompt.append("          \"activity\": \"活动描述\",\n");
        prompt.append("          \"spot_name\": \"景点名称(如果有)\",\n");
        prompt.append("          \"ticket_price\": 门票价格,\n");
        prompt.append("          \"need_booking\": true/false,\n");
        prompt.append("          \"duration\": 建议游玩时长(小时),\n");
        prompt.append("          \"transport_from_prev\": \"交通方式\",\n");
        prompt.append("          \"transport_cost\": 交通费用\n");
        prompt.append("        }\n");
        prompt.append("      ],\n");
        prompt.append("      \"foods\": [\"美食1\", \"美食2\"],\n");
        prompt.append("      \"gifts\": [\"伴手礼1\", \"伴手礼2\"]\n");
        prompt.append("    }\n");
        prompt.append("  ],\n");
        prompt.append("  \"spot_names\": [\"所有景点名称列表\"]\n");
        prompt.append("}\n\n");
        
        prompt.append("注意事项:\n");
        prompt.append("1. 合理安排行程，不要过于紧凑\n");
        prompt.append("2. 考虑景点之间的距离和交通时间\n");
        prompt.append("3. 推荐当地特色美食和伴手礼\n");
        prompt.append("4. 标注需要预约的景点\n");
        prompt.append("5. 根据预算合理选择交通方式\n");
        
        return prompt.toString();
    }
}
```

### 3.4 天气服务模块

#### 3.4.1 WeatherService

```java
public interface WeatherService {
    
    /**
     * 获取天气预报
     * @param city 城市名称
     * @param startDate 开始日期
     * @param days 天数
     */
    List<WeatherDTO> getForecast(String city, LocalDate startDate, Integer days);
}

@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {
    
    @Autowired
    private WeatherClient weatherClient;
    
    @Autowired
    private RedisUtil redisUtil;
    
    @Value("${weather.cache.hours:1}")
    private Integer cacheHours;
    
    @Override
    public List<WeatherDTO> getForecast(String city, LocalDate startDate, Integer days) {
        // 1. 尝试从缓存获取
        String cacheKey = CacheConstant.WEATHER_FORECAST + city + ":" + startDate;
        List<WeatherDTO> cached = redisUtil.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        
        // 2. 调用天气API
        List<WeatherDTO> forecast = weatherClient.queryForecast(city, days);
        
        // 3. 存入缓存
        redisUtil.set(cacheKey, forecast, cacheHours, TimeUnit.HOURS);
        
        return forecast;
    }
}
```

### 3.5 前端JavaScript核心代码

#### 3.5.1 API调用封装 (api.js)

```javascript
const API_BASE_URL = 'http://localhost:8080/api';

class ApiService {
    constructor() {
        this.token = localStorage.getItem('token');
    }
    
    /**
     * 通用请求方法
     */
    async request(url, options = {}) {
        const defaultOptions = {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${this.token}`
            }
        };
        
        const config = {
            ...defaultOptions,
            ...options,
            headers: {
                ...defaultOptions.headers,
                ...options.headers
            }
        };
        
        try {
            const response = await fetch(`${API_BASE_URL}${url}`, config);
            const data = await response.json();
            
            if (data.code !== 200) {
                throw new Error(data.message || '请求失败');
            }
            
            return data.data;
        } catch (error) {
            console.error('API请求错误:', error);
            throw error;
        }
    }
    
    /**
     * GET请求
     */
    get(url) {
        return this.request(url, { method: 'GET' });
    }
    
    /**
     * POST请求
     */
    post(url, data) {
        return this.request(url, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    }
    
    /**
     * PUT请求
     */
    put(url, data) {
        return this.request(url, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    }
    
    /**
     * DELETE请求
     */
    delete(url) {
        return this.request(url, { method: 'DELETE' });
    }
}

// 实例化
const api = new ApiService();
```

#### 3.5.2 攻略编辑器 (editor.js)

```javascript
class ItineraryEditor {
    constructor(containerId) {
        this.container = document.getElementById(containerId);
        this.isEditing = false;
        this.autoSaveTimer = null;
        this.init();
    }
    
    /**
     * 初始化编辑器
     */
    init() {
        this.enableDragAndDrop();
        this.enableContentEdit();
        this.startAutoSave();
    }
    
    /**
     * 启用拖拽排序
     */
    enableDragAndDrop() {
        const timeSlots = this.container.querySelectorAll('.time-slot');
        
        timeSlots.forEach(slot => {
            slot.draggable = true;
            
            slot.addEventListener('dragstart', (e) => {
                e.dataTransfer.setData('text/plain', slot.dataset.index);
                slot.classList.add('dragging');
            });
            
            slot.addEventListener('dragend', () => {
                slot.classList.remove('dragging');
            });
            
            slot.addEventListener('dragover', (e) => {
                e.preventDefault();
            });
            
            slot.addEventListener('drop', (e) => {
                e.preventDefault();
                const fromIndex = e.dataTransfer.getData('text/plain');
                const toIndex = slot.dataset.index;
                this.swapSlots(fromIndex, toIndex);
            });
        });
    }
    
    /**
     * 交换时间段位置
     */
    swapSlots(fromIndex, toIndex) {
        const slots = Array.from(this.container.querySelectorAll('.time-slot'));
        const fromSlot = slots[fromIndex];
        const toSlot = slots[toIndex];
        
        // 交换DOM节点
        fromSlot.parentNode.insertBefore(toSlot, fromSlot);
        fromSlot.parentNode.insertBefore(fromSlot, toSlot.nextSibling);
        
        // 重新编号
        this.reindexSlots();
        
        // 标记为已修改
        this.markAsModified();
    }
    
    /**
     * 重新索引时间段
     */
    reindexSlots() {
        const slots = this.container.querySelectorAll('.time-slot');
        slots.forEach((slot, index) => {
            slot.dataset.index = index;
        });
    }
    
    /**
     * 启用内容编辑
     */
    enableContentEdit() {
        const editableElements = this.container.querySelectorAll('[contenteditable]');
        
        editableElements.forEach(el => {
            el.addEventListener('blur', () => {
                this.markAsModified();
            });
        });
    }
    
    /**
     * 自动保存
     */
    startAutoSave() {
        this.autoSaveTimer = setInterval(() => {
            if (this.isModified) {
                this.saveDraft();
            }
        }, 30000); // 每30秒保存一次
    }
    
    /**
     * 保存草稿
     */
    async saveDraft() {
        const data = this.extractData();
        
        try {
            await api.post('/itinerary/draft', data);
            this.isModified = false;
            this.showNotification('草稿已保存', 'success');
        } catch (error) {
            this.showNotification('保存失败', 'error');
        }
    }
    
    /**
     * 提取编辑后的数据
     */
    extractData() {
        const data = {
            title: this.container.querySelector('h1').textContent,
            dailyPlans: []
        };
        
        const dayPlans = this.container.querySelectorAll('.day-plan');
        dayPlans.forEach(dayEl => {
            const dayData = {
                day: parseInt(dayEl.dataset.day),
                timeSlots: []
            };
            
            const timeSlots = dayEl.querySelectorAll('.time-slot');
            timeSlots.forEach(slot => {
                dayData.timeSlots.push({
                    time: slot.querySelector('.time').textContent,
                    activity: slot.querySelector('.activity').textContent
                });
            });
            
            data.dailyPlans.push(dayData);
        });
        
        return data;
    }
    
    /**
     * 显示通知
     */
    showNotification(message, type) {
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.textContent = message;
        
        document.body.appendChild(notification);
        
        setTimeout(() => {
            notification.remove();
        }, 3000);
    }
    
    /**
     * 标记为已修改
     */
    markAsModified() {
        this.isModified = true;
    }
}
```

## 4. 业务流程时序图

### 4.1 用户登录流程

```
用户浏览器          UserController        UserService         Redis           MySQL
    |                    |                    |                 |               |
    |--输入账号密码----->|                    |                 |               |
    |                    |--POST /login------>|                 |               |
    |                    |                    |--查询用户--------|               |
    |                    |                    |                 |--SELECT------>|
    |                    |                    |<--用户信息-------|               |
    |                    |                    |                 |               |
    |                    |                    |--验证密码--------|               |
    |                    |                    |                 |               |
    |                    |                    |--生成Token------|               |
    |                    |                    |--存储Token------>|               |
    |                    |                    |                 |--SET---------->|
    |                    |                    |                 |               |
    |                    |<--返回Token--------|                 |               |
    |<--显示首页---------|                    |                 |               |
```

### 4.2 攻略生成流程

```
用户     ItineraryController  ItineraryService  AIService  WeatherAPI  SpotAPI  TongYiAPI
 |              |                  |               |            |          |         |
 |--填写参数--->|                  |               |            |          |         |
 |              |--POST /generate->|               |            |          |         |
 |              |                  |--构建请求------|            |          |         |
 |              |                  |               |--构建Prompt|          |         |
 |              |                  |               |--调用AI---------------------------->|
 |              |                  |               |                                    |
 |              |                  |               |<--AI返回JSON-----------------------|
 |              |                  |               |                                    |
 |              |                  |--并行调用API---|            |          |         |
 |              |                  |               |--获取天气-->|          |         |
 |              |                  |               |--获取景点-------------->|         |
 |              |                  |               |<--天气数据--|          |         |
 |              |                  |               |<--景点信息-------------->|         |
 |              |                  |               |                                    |
 |              |                  |--生成HTML------|            |          |         |
 |              |                  |               |            |          |         |
 |              |<--返回攻略HTML----|               |            |          |         |
 |<--展示攻略---|                  |               |            |          |         |
 |              |                  |               |            |          |         |
 |--编辑攻略--->|                  |               |            |          |         |
 |              |--POST /save----->|               |            |          |         |
 |              |                  |--保存到DB---------------------------------------->|
 |              |<--返回攻略ID-----|               |            |          |         |
 |<--显示成功---|                  |               |            |          |         |
```

## 5. 缓存设计

### 5.1 Redis Key设计规范

```
用户Token:        user:token:{userId}
用户信息:         user:info:{userId}
天气数据:         weather:forecast:{city}:{date}
景点信息:         spot:info:{spotName}
攻略详情:         itinerary:detail:{itineraryId}
攻略列表:         itinerary:list:{userId}:{page}:{size}
热门攻略:         itinerary:hot:{page}:{size}
API限流:          rate:limit:{userId}:{api}
```

### 5.2 缓存过期策略

| 数据类型 | 过期时间 | 说明 |
|---------|---------|------|
| 用户Token | 30分钟 | 滑动过期 |
| 用户信息 | 1小时 | 用户信息变更时失效 |
| 天气数据 | 1小时 | 天气预报每小时更新 |
| 景点信息 | 24小时 | 景点信息变化不大 |
| 攻略详情 | 10分钟 | 频繁访问的数据 |
| 攻略列表 | 5分钟 | 分页缓存 |
| API限流 | 1分钟 | 滑动窗口 |

## 6. 消息队列设计

### 6.1 Queue定义

```java
public class MQConstant {
    
    /**
     * 攻略生成队列
     */
    public static final String ITINERARY_GENERATE_QUEUE = "itinerary.generate.queue";
    
    /**
     * 用户操作日志队列
     */
    public static final String USER_LOG_QUEUE = "user.log.queue";
    
    /**
     * 邮件通知队列
     */
    public static final String EMAIL_NOTIFY_QUEUE = "email.notify.queue";
}
```

### 6.2 消息体定义

```java
/**
 * 攻略生成消息
 */
@Data
public class ItineraryGenerateMessage {
    
    private Long userId;
    private Long itineraryId;
    private AIRequestDTO request;
    private LocalDateTime createTime;
}

/**
 * 用户操作日志消息
 */
@Data
public class UserLogMessage {
    
    private Long userId;
    private String action;
    private String module;
    private String detail;
    private LocalDateTime actionTime;
}
```

## 7. 异常处理设计

### 7.1 全局异常处理器

```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }
    
    /**
     * 参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        log.warn("参数校验失败: {}", message);
        return Result.error(ErrorCode.PARAM_ERROR.getCode(), message);
    }
    
    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return Result.error(ErrorCode.SYSTEM_ERROR.getCode(), "系统繁忙，请稍后重试");
    }
}
```

### 7.2 错误码定义

```java
@Getter
public enum ErrorCode {
    
    SUCCESS(200, "成功"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    
    // 用户相关
    USERNAME_EXISTS(1001, "用户名已存在"),
    USER_NOT_EXISTS(1002, "用户不存在"),
    PASSWORD_ERROR(1003, "密码错误"),
    
    // 攻略相关
    ITINERARY_NOT_EXISTS(2001, "攻略不存在"),
    ITINERARY_SAVE_FAILED(2002, "攻略保存失败"),
    
    // AI服务
    AI_SERVICE_ERROR(3001, "AI服务调用失败"),
    AI_PARSE_ERROR(3002, "AI响应解析失败"),
    
    // 第三方服务
    WEATHER_API_ERROR(4001, "天气API调用失败"),
    MAP_API_ERROR(4002, "地图API调用失败"),
    
    SYSTEM_ERROR(5000, "系统内部错误");
    
    private final Integer code;
    private final String message;
    
    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
```

## 8. 安全设计

### 8.1 JWT Token实现

```java
@Component
public class JwtUtil {
    
    @Value("${jwt.secret:travel-itinerary-secret-key}")
    private String secret;
    
    @Value("${jwt.expiration:1800}")
    private Long expiration; // 30分钟
    
    /**
     * 生成Token
     */
    public String generateToken(Long userId, String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration * 1000);
        
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("username", username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
    
    /**
     * 解析Token
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * 验证Token是否有效
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 从Token中获取用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return Long.parseLong(claims.getSubject());
    }
}
```

### 8.2 权限拦截器

```java
@Component
public class AuthInterceptor implements HandlerInterceptor {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private RedisUtil redisUtil;
    
    @Override
    public boolean preHandle(HttpServletRequest request, 
                            HttpServletResponse response, 
                            Object handler) {
        // 获取Token
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        
        token = token.substring(7);
        
        // 验证Token
        if (!jwtUtil.validateToken(token)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        
        // 检查Redis中是否存在
        Long userId = jwtUtil.getUserIdFromToken(token);
        String redisToken = redisUtil.get(CacheConstant.USER_TOKEN + userId);
        if (!token.equals(redisToken)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        
        // 将用户ID存入请求属性
        request.setAttribute("userId", userId);
        
        return true;
    }
}
```

## 9. 配置类设计

### 9.1 Redis配置

```java
@Configuration
public class RedisConfig {
    
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        
        // 使用JSON序列化
        Jackson2JsonRedisSerializer<Object> serializer = 
            new Jackson2JsonRedisSerializer<>(Object.class);
        
        template.setDefaultSerializer(serializer);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        
        return template;
    }
}
```

### 9.2 跨域配置

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
```

## 10. 工具类设计

### 10.1 HTML工具类

```java
public class HtmlUtil {
    
    /**
     * 转义HTML特殊字符
     */
    public static String escapeHtml(String text) {
        if (text == null) {
            return null;
        }
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#39;");
    }
    
    /**
     * 从HTML中提取纯文本
     */
    public static String extractText(String html) {
        return html.replaceAll("<[^>]*>", "");
    }
}
```

## 11. 测试用例设计

### 11.1 单元测试示例

```java
@SpringBootTest
class ItineraryServiceTest {
    
    @Autowired
    private ItineraryService itineraryService;
    
    @Test
    void testGenerateItinerary() {
        ItineraryGenerateDTO dto = new ItineraryGenerateDTO();
        dto.setDestination("北京");
        dto.setDays(3);
        dto.setStyle("文化");
        dto.setBudget(new BigDecimal(5000));
        dto.setStartDate(LocalDate.now().plusDays(7));
        
        AIResponseDTO response = itineraryService.generate(dto, 1L);
        
        assertNotNull(response);
        assertEquals(3, response.getDays());
        assertNotNull(response.getDailyPlans());
    }
    
    @Test
    void testSaveItinerary() {
        ItinerarySaveDTO dto = new ItinerarySaveDTO();
        dto.setTitle("北京三日游");
        dto.setDestination("北京");
        dto.setDays(3);
        
        Long id = itineraryService.save(dto, 1L);
        
        assertNotNull(id);
        assertTrue(id > 0);
    }
}
```

## 12. 性能优化方案

### 12.1 数据库优化
- 为常用查询字段添加索引(user_id, create_time)
- 使用连接池(HikariCP)
- 批量插入优化(每日行程批量保存)
- 读写分离配置

### 12.2 缓存优化
- 多级缓存策略
- 缓存预热(热门景点、天气)
- 缓存穿透保护(布隆过滤器)
- 缓存雪崩预防(随机过期时间)

### 12.3 接口优化
- 并行调用外部API(CompletableFuture)
- 异步处理非关键任务(MQ)
- 接口限流(Redis计数器)
- 响应数据压缩(GZIP)

## 13. 部署配置

### 13.1 application.yml配置

```yaml
server:
  port: 8080
  
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/travel_itinerary?useUnicode=true&characterEncoding=utf8
    username: root
    password: ${DB_PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver
    
  redis:
    host: localhost
    port: 6379
    password: ${REDIS_PASSWORD}
    database: 0
    
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: ${MQ_PASSWORD}
    
aliyun:
  tongyi:
    api-key: ${TONGYI_API_KEY}
    api-url: https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation
    
weather:
  api-key: ${WEATHER_API_KEY}
  cache:
    hours: 1
    
jwt:
  secret: ${JWT_SECRET}
  expiration: 1800
  
logging:
  level:
    com.travel.itinerary: INFO
  file:
    name: logs/application.log
```

## 14. 附录

### 14.1 开发环境要求
- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Redis 7.x
- Node.js 16+(前端开发)

### 14.2 依赖管理
详见pom.xml文件
