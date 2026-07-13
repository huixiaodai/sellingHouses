# 项目防重复错误记录

## LocalDateTime JSON 反序列化格式错误

### 报错现象

后端创建预约时报错：

```text
org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error:
Cannot deserialize value of type `java.time.LocalDateTime` from String "2026-07-15T11:02:00"
...
Text '2026-07-15T11:02:00' could not be parsed at index 10
...
through reference chain:
com.sellinghouses.salescontrol.module.appointment.dto.AppointmentCreateDTO["appointmentTime"]
```

### 根因

项目后端在 `JacksonConfig` 中统一配置了 `LocalDateTime` 格式：

```text
yyyy-MM-dd HH:mm:ss
```

因此接口入参不能传 ISO 格式 `2026-07-15T11:02:00`。字符 `T` 会导致 Jackson 按项目格式解析失败。

### 必须遵守

- 前端提交所有 `LocalDateTime` 字段时，必须使用空格格式：`yyyy-MM-dd HH:mm:ss`。
- 禁止提交带 `T` 的格式：`yyyy-MM-ddTHH:mm:ss`。
- 小程序、管理端、接口文档、测试数据要保持同一种时间格式。

### 正确示例

```json
{
  "appointmentTime": "2026-07-15 11:02:00"
}
```

### 错误示例

```json
{
  "appointmentTime": "2026-07-15T11:02:00"
}
```

### 本次修正

- 小程序预约看房提交参数已改为 `${date} ${time}:00`。
- 后续新增 `LocalDateTime` 请求字段时，先检查 `JacksonConfig` 的全局格式，不要按浏览器/JavaScript 默认 ISO 字符串直接提交。
