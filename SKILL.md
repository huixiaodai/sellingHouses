---
name: spring-boot-java-backend-codex
description: Use this skill when generating, modifying, reviewing, or refactoring Java Spring Boot backend projects. It enforces project-first conventions, layered architecture, GET/POST-only APIs, DTO/VO separation, validation, unified responses, business exceptions, logging, SQL safety, transaction/idempotency checks, API security, Maven compile checks, static checks, format checks, and minimal maintainable changes.
---

# Spring Boot Java 后端开发规范

## 0. 适用范围

当任务涉及 Java / Spring Boot 后端项目时使用本 Skill，包括：

- Controller、Service、Mapper/Repository、Entity、DTO/Request、VO/Response、Enum、Constant、Config、Job、MQ Consumer、第三方 API 服务
- MyBatis / MyBatis-Plus / XML Mapper / JPA 等数据访问代码
- 接口新增、修改、审查、重构、修 bug、补接口文档
- SQL、事务、幂等、异常、日志、校验、安全、性能相关改动

不适用于纯前端、纯脚本、纯运维、非 Java 项目；除非该任务会影响 Spring Boot 后端代码。

---

## 1. Codex 工作模式

### 1.1 先读项目，再写代码

修改前必须先检查当前项目已有实现，禁止凭空创建基础类或新规范。

优先查找并沿用：

- 统一返回对象：如 `Result`、`R`、`AjaxResult` 及其成功/失败方法
- 分页对象：如 `PageResult`、`PageResp`、`IPage` 包装方式
- 业务异常和错误码：如 `BusinessException`、`ServiceException`、`ErrorCode`
- 全局异常处理：`@RestControllerAdvice`、`@ExceptionHandler`
- Mapper 技术栈：MyBatis、MyBatis-Plus、XML、注解 SQL、JPA
- 接口文档注解：Swagger 2 / Knife4j 或 OpenAPI 3，不允许混用
- 依赖：Lombok、Hutool、Validation、MapStruct、MyBatis-Plus 等是否已引入
- 包结构、类命名、URL 风格、字段命名、枚举风格、SQL 风格

推荐搜索方式：

```bash
rg "class .*Controller|@RestController|Result<|PageResult|BusinessException|@RestControllerAdvice|@Mapper|@ApiOperation|@Operation" -n .
rg "@Transactional|@RequiredArgsConstructor|@Resource|@Autowired|select \*|System\.out|printStackTrace" -n .
```

### 1.2 优先级

1. 用户明确要求
2. 当前项目已有统一约定
3. 本 Skill
4. Spring / Java / 后端通用最佳实践

当本 Skill 与项目既有风格冲突时，优先保持项目一致；但安全问题、数据损坏风险、明显 bug 不应为了风格而保留。

### 1.3 改动原则

- 只改与任务直接相关的文件。
- 不做无关重构，不批量格式化无关文件。
- 不新增无关模板、通用工具类、基础框架类。
- 能复用项目已有能力就不要新建。
- 缺少上下文时，先基于项目现有代码推断；仍无法确认时，使用明显占位名并说明需要替换。
- 生成代码必须能融入当前项目，而不是孤立示例。
- 不执行 `git commit`、不生成提交信息，除非用户明确要求。

---

## 2. 硬性规则

### 2.1 Controller 只允许 GET / POST

只允许：

```java
@GetMapping
@PostMapping
```

禁止：

```java
@PutMapping
@DeleteMapping
@PatchMapping
@RequestMapping(method = RequestMethod.PUT)
@RequestMapping(method = RequestMethod.DELETE)
@RequestMapping(method = RequestMethod.PATCH)
```

规则：

- `GET` 只用于查询，不能新增、修改、删除、提交、同步、导入、导出或触发副作用。
- `POST` 用于新增、修改、删除、提交、同步、导入、导出、回调、下发、重试等业务操作。
- POST JSON 请求必须使用 `@RequestBody @Valid`。
- 文件上传、表单提交、下载导出、第三方回调按项目约定使用 `@RequestParam`、`@RequestPart`、`HttpServletRequest`、`HttpServletResponse` 等。

### 2.2 依赖注入

优先构造函数注入，推荐 Lombok：

```java
@RequiredArgsConstructor
@Service
public class XxxServiceImpl implements XxxService {
    private final XxxMapper xxxMapper;
}
```

允许在项目已有风格或特殊场景使用：

```java
@Resource
private XxxService xxxService;
```

禁止字段 `@Autowired`。

### 2.3 基础代码风格

- `if`、`for`、`while`、`else` 即使只有一行也必须加大括号。
- 禁止 `System.out.println()`。
- 禁止 `e.printStackTrace()`。
- 使用 `@Slf4j` 或项目统一日志方式。
- 不写魔法值；状态、类型、来源、渠道优先枚举或常量。
- 生成代码只覆盖需求范围，不输出无关类和方法。

---

## 3. 分层职责

### 3.1 Controller

Controller 只做接口入口：

允许：

- 接收请求参数
- 参数校验注解
- Swagger / Knife4j / OpenAPI 注解
- 调用 Service
- 返回统一响应对象

禁止：

- 复杂业务逻辑
- 数据库访问
- 第三方 HTTP、签名、URL 拼接、响应 JSON 解析
- 事务控制
- 手动拼接错误响应
- 大量参数转换
- 多层 if/for 业务流程

示例结构：

```java
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/xxx")
public class XxxController {

    private final XxxService xxxService;

    @GetMapping("/detail")
    public Result<XxxDetailVO> detail(@NotNull(message = "主键ID不能为空") @RequestParam Long id) {
        return Result.success(xxxService.detail(id));
    }

    @PostMapping("/updateQty")
    public Result<Void> updateQty(@RequestBody @Valid XxxUpdateQtyRequest request) {
        xxxService.updateQty(request);
        return Result.success();
    }
}
```

### 3.2 Service 接口

- 定义业务能力，不暴露数据库细节。
- 方法名必须表达业务含义。
- Service 接口方法必须写 JavaDoc。
- 禁止使用 `handle`、`process`、`doSomething`、`test` 等模糊命名。
- 参数优先使用明确的 Request / DTO。
- 返回值优先使用明确的 VO / DTO。

### 3.3 Service 实现

Service 实现负责编排业务流程：

- 业务参数校验
- 权限/归属校验调用
- 状态流转判断
- 幂等判断
- Mapper/Repository 调用
- 第三方 API 服务调用
- 事务边界
- 必要业务注释

禁止：

- 把 Controller 变薄后把无结构代码全部堆到 Service 的超长方法中
- 无意义拆分私有方法
- 为简单判断、简单赋值抽方法
- 直接写 HTTP 调用、签名、URL 拼接、JSON 解析
- 直接拼复杂 SQL 字符串

抽方法只在以下场景使用：

- 多处重复
- 独立业务概念
- 单个方法过长影响阅读
- 可独立测试
- 第三方参数转换/结果校验等边界清晰

### 3.4 Mapper / Repository

Mapper / Repository 只负责数据库访问。

要求：

- 不写业务逻辑。
- XML SQL 禁止 `select *`。
- SQL 字段明确列出。
- 复杂 SQL 添加必要注释。
- 动态排序、动态字段、动态表名必须白名单控制。
- `update` / `delete` 必须有明确 `where` 条件。
- 空集合 `in` 查询必须在 Service 层拦截，不让 SQL 执行。

### 3.5 Entity

- 只表示数据库表结构。
- 不写业务逻辑。
- 不作为 Controller 请求参数。
- 不直接作为 Controller 响应，除非项目已有明确约定。

### 3.6 DTO / Request

- 用于请求入参。
- 必填字段必须加 Validation 注解。
- 集合参数校验非空，必要时校验元素合法性。
- 金额、数量、重量、面积、比例等精度字段必须使用 `BigDecimal`。
- 字段添加项目统一接口文档注解。
- 不写业务逻辑。

### 3.7 VO / Response

- 用于接口响应。
- 只返回前端/API 调用方需要的字段。
- 禁止暴露密码、token、密钥、完整手机号、身份证号、内部状态说明等敏感字段。
- 字段添加项目统一接口文档注解。
- 简单同名字段可使用项目已有 Bean 拷贝工具。
- 敏感字段、字段语义不一致、嵌套对象、复杂转换必须显式赋值或使用转换器。
- 禁止“Entity 全量复制后再删除敏感字段”的写法。

### 3.8 API Service / Client

第三方系统调用必须封装到 API Service / Client。

要求：

- Service 不直接写 HTTP 请求、URL 拼接、签名、认证、JSON 解析。
- API Service 负责请求构建、认证、签名、发送、响应解析、基础响应校验。
- 第三方异常日志包含业务单号、请求摘要、响应码、错误信息。
- 不记录密钥、token、密码、完整个人敏感信息。
- 远程调用超时、重试、降级、幂等按项目已有组件实现。

---

## 4. 统一返回、分页与异常

### 4.1 统一返回

- Controller 必须返回项目统一响应对象，如 `Result<T>`。
- 无返回数据使用 `Result<Void>` 或项目约定空结果类型。
- 禁止直接返回 `String`、`Map`、`List`、Entity、`Page`、`IPage`。
- 不要在生成代码中重新定义 `Result`、`PageResult`、异常基类。

分页接口通常形态：

```java
Result<PageResult<XxxListVO>>
```

具体类名和构造方式以项目现有实现为准。

### 4.2 异常

- 使用项目统一业务异常和错误码。
- 错误响应交给全局异常处理器。
- Controller 不手动拼错误响应。
- 禁止返回错误字符串。
- 禁止 catch 后吞异常。
- catch 后必须记录关键上下文，并带异常对象。
- 面向客户端的错误信息避免泄露堆栈、SQL、内部类名、第三方原始错误详情。

示例：

```java
try {
    xxxApiService.sync(orderNo);
} catch (XxxApiException e) {
    log.error("同步订单失败，orderNo={}, errorCode={}", orderNo, e.getErrorCode(), e);
    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "同步订单失败");
}
```

---

## 5. 参数校验与接口文档

### 5.1 参数校验

- POST JSON：`@RequestBody @Valid`。
- GET 查询参数：Controller 类加 `@Validated`，参数加 `@NotNull`、`@NotBlank` 等。
- DTO 必填字段必须加校验注解。
- 集合字段使用 `@NotEmpty` 或项目约定，必要时校验元素。
- `BigDecimal` 字段按业务添加 `@DecimalMin`、`@Digits` 等。
- Controller 做格式和基础校验；业务规则在 Service 校验。

### 5.2 接口文档

必须沿用项目已有 Swagger / Knife4j / OpenAPI 注解风格。

禁止同一项目混用 Swagger 2 和 OpenAPI 3。

Swagger 2 / Knife4j 常见注解：

```java
@Api
@ApiOperation
@ApiParam
@ApiModel
@ApiModelProperty
```

OpenAPI 3 常见注解：

```java
@Tag
@Operation
@Parameter
@Schema
```

要求接口文档能展示：

- 接口分组
- 接口名称
- 接口说明
- 请求参数
- 响应参数
- 必要校验规则

Controller 方法不需要写大段 JavaDoc；Service 接口方法必须写 JavaDoc。

---

## 6. 日志与安全

### 6.1 日志

- 使用 `@Slf4j` 或项目统一日志方式。
- 错误日志必须带异常对象。
- 关键业务日志包含定位标识，如 orderNo、taskId、requestId、traceId、messageId。
- 第三方调用失败记录请求摘要和响应摘要，不记录完整敏感报文。
- 禁止记录密码、密钥、token、完整身份证号、完整手机号、银行卡号等。
- 用户输入进入日志前注意长度限制和换行/控制字符，避免日志注入。

### 6.2 API 安全

开发接口时必须考虑：

- 鉴权和权限/数据归属校验是否已有统一机制。
- 不信任客户端传入的 userId、tenantId、orgId、price、status 等敏感业务字段。
- 查询详情、修改、删除、导出必须校验数据归属或权限。
- 错误响应不暴露堆栈、SQL、内部路径、密钥、第三方原始敏感错误。
- 导出、列表、模糊查询必须考虑最大范围、分页、权限过滤。
- 文件上传必须检查大小、类型、扩展名、存储路径，不能信任原始文件名。
- 下载文件必须防止路径穿越。

---

## 7. 事务、一致性与幂等

### 7.1 事务

`@Transactional` 应放在 Service 实现类的 public 方法上。

要求：

- 多表写入、批量更新、状态流转必须考虑事务。
- 查询方法通常不加事务；如项目习惯可使用 `readOnly = true`。
- 本地数据库事务中不要执行耗时第三方 HTTP 调用，除非有明确业务原因。
- 本地 DB + 远端 API 组合必须说明一致性策略：先远端后本地、本地后补偿、重试、对账、最终一致、幂等键等。
- 不依赖同类内部方法调用触发事务；Spring AOP 事务只会拦截经过代理的调用。
- 需要 checked exception 回滚时使用项目约定的 `rollbackFor`。

示例：

```java
@Transactional(rollbackFor = Exception.class)
@Override
public void confirmOrder(XxxConfirmRequest request) {
    // status check + multi-table write
}
```
---

## 8. SQL、数据库与性能

### 8.1 SQL 安全

- 禁止 `select *`。
- 查询列必须明确。
- `update` / `delete` 必须有明确 `where`。
- 批量更新/删除必须校验入参非空和业务范围。
- 动态排序、动态字段、动态表名必须白名单。
- 禁止拼接未经校验的用户输入到 SQL。
- 表名、字段名默认小写下划线，除非项目已有其他规范。

### 8.2 查询性能

- 列表、详情、导出组装子数据时避免 N+1 查询。
- 禁止循环中逐条查数据库。
- 优先批量 `in` 查询，再按主键分组成 `Map`。
- 大量 `in` 参数必须分批，通常每批 500 或 1000，具体按项目约定。
- 大列表和导出必须考虑分页、分批、最大时间范围、最大导出量。
- 复杂统计可用 SQL 聚合，但 SQL 必须清晰可维护。

---

## 9. BigDecimal 与时间

### 9.1 BigDecimal

- 金额、数量、重量、面积、比例等精度字段必须使用 `BigDecimal`。
- 禁止使用 `Double` / `Float` 表示精度字段。
- 比较使用 `compareTo`，不要用 `equals`。
- 除法必须指定 scale 和 `RoundingMode`。
- 注意 null 防御，避免直接 `request.getQty().compareTo(...)` 触发 NPE；优先依赖 Validation，再在业务关键处防御。

```java
if (qty.compareTo(BigDecimal.ZERO) <= 0) {
    throw new BusinessException(ErrorCode.BAD_REQUEST, "数量必须大于0");
}

BigDecimal avgQty = totalQty.divide(count, 6, RoundingMode.HALF_UP);
```

### 9.2 时间

- 时间类型沿用项目约定：`LocalDateTime`、`Date`、时间戳等。
- 新代码优先使用项目已有时间工具和序列化格式。
- 时间范围查询注意开始/结束边界，避免漏数或重复。
- 跨时区场景必须明确时区来源。

---

## 10. 枚举、状态机与常量

- 状态、类型、来源、渠道、业务动作等固定值必须优先使用枚举。
- 禁止业务代码散落裸数字、裸字符串。
- 枚举通常包含 `code`、`desc`，并沿用项目已有接口。
- 状态流转必须校验当前状态是否合法。
- 复杂或不符合直觉的状态流转必须注释说明业务原因。
- 常量类只放通用常量，不承载业务流程。

---

## 11. 配置、工具类与复用

### 11.1 配置

- 不要把环境差异、密钥、URL、开关硬编码在代码中。
- 配置项沿用项目已有 `application.yml`、配置中心、`@ConfigurationProperties` 或常量管理方式。
- 密钥、token、密码不得提交到代码。
- 新增配置必须考虑默认值、说明和环境差异。

### 11.2 工具类

若项目已引入 Hutool，且项目风格允许，通用能力优先使用 Hutool：

- `StrUtil`：字符串
- `CollUtil`：集合
- `ObjectUtil`：对象
- `DateUtil`：日期
- `JSONUtil`：JSON
- `BeanUtil`：Bean 拷贝
- `MapUtil`：Map
- `FileUtil`：文件
- `IdUtil`：ID
- `DigestUtil`：摘要

规则：

- 禁止重复新增 Hutool、JDK、Spring、MyBatis-Plus 已有能力。
- 工具类必须无状态、通用。
- 不允许把业务逻辑塞进工具类。
- 项目已有统一工具类时优先沿用。

---

## 12. Maven 验证要求

所有项目均按 Maven 项目处理。Codex 不提交代码，不主动新增或运行测试用例，除非用户明确要求。

修改后必须尽量运行与改动相关的最小验证，优先选择不会执行测试用例的检查命令：

- 编译检查
- 静态检查
- 格式检查

无法运行时，必须说明原因。

---

## 13. 常见禁止事项

禁止生成或保留：

```java
@Autowired
private XxxService xxxService;
```

```java
@PutMapping
@DeleteMapping
@PatchMapping
```

```java
System.out.println();
e.printStackTrace();
```

```sql
select * from xxx_table
```

```java
if (condition) doSomething();
```

```java
return "参数错误";
```

```java
private Double amount;
private Float qty;
```

禁止行为：

- 过度封装简单逻辑
- 新增无关模板代码
- 新增重复工具方法
- Controller 写业务流程
- Service 直接写 HTTP 调用细节
- Entity 直接作为请求/响应
- 混用 Swagger 2 和 OpenAPI 3
- 动态 SQL 直接拼接用户输入
- 日志打印敏感信息
- 本地事务中随意包裹慢远程调用

---

## 14. 输出格式

完成任务时，回复应包含：

- 修改了哪些文件
- 变更了什么业务行为或技术行为
- 做了哪些验证：Maven 编译检查、静态检查、格式检查等
- 未验证的内容和原因
- 任何项目特定假设或需要用户确认的点

不要粘贴大段未变化代码，除非用户明确要求。
不要提交代码或建议提交信息，除非用户明确要求。
