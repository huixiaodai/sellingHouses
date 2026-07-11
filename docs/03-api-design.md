# 智慧售楼管理系统 - 三端接口设计文档

本文档面向三个前端调用方：

- 管理后台：Vue3 网页端，主要调用 `/api/admin` 和通用查询接口。
- 销售端：uni-app 跨平台端，主要调用 `/api/sales` 和通用查询接口。
- 用户端：uni-app 跨平台端，主要调用 `/api/user` 和通用查询接口。

销售端和用户端必须优先使用 uni-app 跨平台 API，禁止直接使用 `wx.xxx` 等微信专有 API。确需平台专有能力时，必须标注“【微信小程序专用】”。

## 1. 接口规范

### 1.1 基础信息

- 后端基础地址：`http://localhost:8080`
- API 前缀：`/api`
- 管理端接口前缀：`/api/admin`，由 Vue 管理后台调用
- 销售端接口前缀：`/api/sales`，由 uni-app 销售端调用
- 用户端接口前缀：`/api/user`，由 uni-app 用户端调用
- WebSocket 地址：`ws://localhost:8080/ws/sale-control?token={jwtToken}`
- 请求数据格式：`application/json`
- 响应数据格式：`application/json`

### 1.2 请求方式约束

| 场景 | 请求方式 |
| --- | --- |
| 查询列表、详情、统计 | `GET` |
| 登录、注册、退出 | `POST` |
| 新增、修改、删除 | `POST` |
| 状态变更、价格变更 | `POST` |
| 收藏、取消收藏 | `POST` |
| 预约、取消预约 | `POST` |

> 项目 Controller 只使用 `@GetMapping` 和 `@PostMapping`。

### 1.3 统一请求头

登录、注册接口不需要 Token，其余接口默认需要 Token。

| Header | 必填 | 说明 |
| --- | --- | --- |
| `Authorization` | 是 | `Bearer {token}` |
| `Content-Type` | 是 | `application/json` |

### 1.4 统一响应结构

```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": 1783670400000
}
```

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `code` | Integer | 业务状态码，`200` 表示成功 |
| `message` | String | 响应信息 |
| `data` | Object | 响应数据 |
| `timestamp` | Long | 响应时间戳 |

### 1.5 分页响应结构

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [],
    "pageNo": 1,
    "pageSize": 10,
    "total": 100
  },
  "timestamp": 1783670400000
}
```

### 1.6 通用错误码

| 错误码 | 说明 |
| --- | --- |
| `200` | 成功 |
| `400` | 参数错误 |
| `401` | 未登录或 Token 无效 |
| `403` | 无权限 |
| `404` | 数据不存在 |
| `409` | 业务状态冲突 |
| `500` | 系统异常 |

### 1.7 角色编码

| 编码 | 角色 |
| --- | --- |
| `1` | 超级管理员 |
| `2` | 销售 |
| `3` | 购房用户 |

### 1.8 房源状态编码

| 编码 | 状态 | 展示颜色 |
| --- | --- | --- |
| `1` | 待售 | 绿色 |
| `2` | 已预订 | 黄色 |
| `3` | 已售 | 红色 |
| `4` | 不可售 | 灰色 |

## 2. 登录模块

### 2.1 用户登录

- URL：`POST /api/auth/login`
- 权限：公开
- 说明：三个角色共用登录接口，登录成功后前端根据 `role` 跳转不同首页。

请求参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `username` | String | 是 | 登录账号 |
| `password` | String | 是 | 登录密码 |

响应数据：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `token` | String | JWT Token |
| `userId` | Long | 用户ID |
| `username` | String | 登录账号 |
| `realName` | String | 真实姓名 |
| `role` | Integer | 角色 |
| `homePath` | String | 登录后首页路径 |

### 2.2 用户注册

- URL：`POST /api/auth/register`
- 权限：公开
- 说明：仅注册购房用户，销售账号由管理员创建。

请求参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `username` | String | 是 | 登录账号 |
| `password` | String | 是 | 登录密码 |
| `realName` | String | 是 | 真实姓名 |
| `phone` | String | 是 | 手机号 |
| `wxOpenid` | String | 否 | 微信 OpenID |

响应数据：`null`

### 2.3 当前登录用户

- URL：`GET /api/auth/current`
- 权限：登录用户

响应数据：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `userId` | Long | 用户ID |
| `username` | String | 登录账号 |
| `realName` | String | 真实姓名 |
| `phone` | String | 脱敏手机号 |
| `role` | Integer | 角色 |
| `status` | Integer | 状态 |

### 2.4 退出登录

- URL：`POST /api/auth/logout`
- 权限：登录用户
- 说明：服务端单体无 Redis，退出由前端清理 Token；接口保留用于记录日志。

响应数据：`null`

## 3. 楼盘模块

### 3.1 新增楼盘

- URL：`POST /api/admin/estate/create`
- 权限：超级管理员

请求参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `name` | String | 是 | 楼盘名称 |
| `city` | String | 是 | 城市 |
| `district` | String | 否 | 区域 |
| `address` | String | 是 | 详细地址 |
| `developer` | String | 否 | 开发商 |
| `saleOfficePhone` | String | 否 | 售楼处电话 |
| `coverImage` | String | 否 | 封面图 |
| `description` | String | 否 | 楼盘简介 |
| `status` | Integer | 是 | 状态：1在售，2待售，3售罄，4下架 |

响应数据：`id`

### 3.2 修改楼盘

- URL：`POST /api/admin/estate/update`
- 权限：超级管理员

请求参数：同新增楼盘，额外包含：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | Long | 是 | 楼盘ID |

响应数据：`null`

### 3.3 删除楼盘

- URL：`POST /api/admin/estate/delete`
- 权限：超级管理员

请求参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | Long | 是 | 楼盘ID |

响应数据：`null`

### 3.4 楼盘列表

- URL：`GET /api/estate/page`
- 权限：登录用户

查询参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `pageNo` | Integer | 否 | 页码，默认1 |
| `pageSize` | Integer | 否 | 每页条数，默认10 |
| `keyword` | String | 否 | 楼盘名称关键字 |
| `city` | String | 否 | 城市 |
| `status` | Integer | 否 | 楼盘状态 |

响应数据：`PageResult<EstateListVO>`

### 3.5 楼盘详情

- URL：`GET /api/estate/detail`
- 权限：登录用户

查询参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | Long | 是 | 楼盘ID |

响应数据：`EstateDetailVO`

## 4. 楼栋与单元模块

### 4.1 新增楼栋

- URL：`POST /api/admin/building/create`
- 权限：超级管理员

请求参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `estateId` | Long | 是 | 楼盘ID |
| `name` | String | 是 | 楼栋名称 |
| `buildingNo` | String | 是 | 楼栋编号 |
| `floorCount` | Integer | 是 | 总楼层 |
| `unitCount` | Integer | 是 | 单元数量 |

响应数据：`id`

### 4.2 楼栋列表

- URL：`GET /api/building/list`
- 权限：登录用户

查询参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `estateId` | Long | 是 | 楼盘ID |

响应数据：`List<BuildingListVO>`

### 4.3 新增单元

- URL：`POST /api/admin/building-unit/create`
- 权限：超级管理员

请求参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `buildingId` | Long | 是 | 楼栋ID |
| `unitName` | String | 是 | 单元名称 |
| `unitNo` | String | 是 | 单元编号 |
| `floorCount` | Integer | 是 | 单元楼层数 |
| `roomCountPerFloor` | Integer | 是 | 每层房源数 |

响应数据：`id`

### 4.4 单元列表

- URL：`GET /api/building-unit/list`
- 权限：登录用户

查询参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `buildingId` | Long | 是 | 楼栋ID |

响应数据：`List<BuildingUnitVO>`

## 5. 房源模块

### 5.1 新增房源

- URL：`POST /api/admin/room/create`
- 权限：超级管理员

请求参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `estateId` | Long | 是 | 楼盘ID |
| `buildingId` | Long | 是 | 楼栋ID |
| `unitId` | Long | 是 | 单元ID |
| `roomNo` | String | 是 | 房号 |
| `floorNo` | Integer | 是 | 楼层 |
| `houseType` | String | 否 | 户型 |
| `orientation` | String | 否 | 朝向 |
| `constructionArea` | BigDecimal | 是 | 建筑面积 |
| `usableArea` | BigDecimal | 否 | 套内面积 |
| `totalPrice` | BigDecimal | 是 | 总价 |
| `unitPrice` | BigDecimal | 是 | 单价 |
| `status` | Integer | 是 | 房源状态 |
| `description` | String | 否 | 房源说明 |

响应数据：`id`

### 5.2 修改房源

- URL：`POST /api/admin/room/update`
- 权限：超级管理员

请求参数：同新增房源，额外包含：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | Long | 是 | 房源ID |

响应数据：`null`

### 5.3 删除房源

- URL：`POST /api/admin/room/delete`
- 权限：超级管理员

请求参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | Long | 是 | 房源ID |

响应数据：`null`

### 5.4 修改房源状态

- URL：`POST /api/admin/room/update-status`
- 权限：超级管理员
- 说明：更新成功后触发 WebSocket 推送。

请求参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `roomId` | Long | 是 | 房源ID |
| `status` | Integer | 是 | 房源状态 |

响应数据：`null`

### 5.5 修改房源价格

- URL：`POST /api/admin/room/update-price`
- 权限：超级管理员

请求参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `roomId` | Long | 是 | 房源ID |
| `totalPrice` | BigDecimal | 是 | 总价 |
| `unitPrice` | BigDecimal | 是 | 单价 |

响应数据：`null`

### 5.6 房源销控图

- URL：`GET /api/room/sale-control`
- 权限：登录用户

查询参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `buildingId` | Long | 是 | 楼栋ID |
| `unitId` | Long | 否 | 单元ID |

响应数据：`List<RoomSaleControlVO>`

### 5.7 房源详情

- URL：`GET /api/room/detail`
- 权限：登录用户

查询参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | Long | 是 | 房源ID |

响应数据：`RoomDetailVO`

### 5.8 房源搜索

- URL：`GET /api/room/page`
- 权限：登录用户

查询参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `pageNo` | Integer | 否 | 页码 |
| `pageSize` | Integer | 否 | 每页条数 |
| `estateId` | Long | 否 | 楼盘ID |
| `buildingId` | Long | 否 | 楼栋ID |
| `unitId` | Long | 否 | 单元ID |
| `floorNo` | Integer | 否 | 楼层 |
| `roomNo` | String | 否 | 房号 |
| `status` | Integer | 否 | 房源状态 |

响应数据：`PageResult<RoomDetailVO>`

## 6. 收藏模块

### 6.1 收藏房源

- URL：`POST /api/user/favorite/add`
- 权限：购房用户

请求参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `roomId` | Long | 是 | 房源ID |

响应数据：`null`

### 6.2 取消收藏

- URL：`POST /api/user/favorite/cancel`
- 权限：购房用户

请求参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `roomId` | Long | 是 | 房源ID |

响应数据：`null`

### 6.3 我的收藏

- URL：`GET /api/user/favorite/page`
- 权限：购房用户

查询参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `pageNo` | Integer | 否 | 页码 |
| `pageSize` | Integer | 否 | 每页条数 |

响应数据：`PageResult<FavoriteRoomVO>`

## 7. 预约模块

### 7.1 预约看房

- URL：`POST /api/user/appointment/create`
- 权限：购房用户

请求参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `estateId` | Long | 是 | 楼盘ID |
| `roomId` | Long | 否 | 意向房源ID |
| `appointmentTime` | LocalDateTime | 是 | 预约时间 |
| `contactName` | String | 是 | 联系人 |
| `contactPhone` | String | 是 | 联系电话 |
| `remark` | String | 否 | 备注 |

响应数据：`id`

### 7.2 取消预约

- URL：`POST /api/user/appointment/cancel`
- 权限：购房用户

请求参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | Long | 是 | 预约ID |
| `cancelReason` | String | 否 | 取消原因 |

响应数据：`null`

### 7.3 我的预约

- URL：`GET /api/user/appointment/page`
- 权限：购房用户

查询参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `pageNo` | Integer | 否 | 页码 |
| `pageSize` | Integer | 否 | 每页条数 |
| `status` | Integer | 否 | 预约状态 |

响应数据：`PageResult<AppointmentVO>`

### 7.4 管理员查看预约

- URL：`GET /api/admin/appointment/page`
- 权限：超级管理员

查询参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `pageNo` | Integer | 否 | 页码 |
| `pageSize` | Integer | 否 | 每页条数 |
| `estateId` | Long | 否 | 楼盘ID |
| `salesUserId` | Long | 否 | 销售ID |
| `status` | Integer | 否 | 预约状态 |

响应数据：`PageResult<AppointmentVO>`

### 7.5 销售查看预约

- URL：`GET /api/sales/appointment/page`
- 权限：销售

查询参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `pageNo` | Integer | 否 | 页码 |
| `pageSize` | Integer | 否 | 每页条数 |
| `status` | Integer | 否 | 预约状态 |

响应数据：`PageResult<AppointmentVO>`

### 7.6 修改预约状态

- URL：`POST /api/admin/appointment/update-status`
- 权限：超级管理员

请求参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | Long | 是 | 预约ID |
| `status` | Integer | 是 | 预约状态 |
| `salesUserId` | Long | 否 | 分配销售ID |

响应数据：`null`

### 7.7 客户跟进

- URL：`POST /api/sales/customer-follow/create`
- 权限：销售

请求参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `appointmentId` | Long | 否 | 预约ID |
| `customerUserId` | Long | 是 | 客户ID |
| `followType` | Integer | 是 | 跟进方式 |
| `followContent` | String | 是 | 跟进内容 |
| `nextFollowTime` | LocalDateTime | 否 | 下次跟进时间 |

响应数据：`id`

## 8. 公告模块

### 8.1 发布公告

- URL：`POST /api/admin/notice/create`
- 权限：超级管理员

请求参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `title` | String | 是 | 公告标题 |
| `content` | String | 是 | 公告内容 |
| `targetRole` | Integer | 是 | 可见角色：0全部，2销售，3购房用户 |

响应数据：`id`

### 8.2 删除公告

- URL：`POST /api/admin/notice/delete`
- 权限：超级管理员

请求参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | Long | 是 | 公告ID |

响应数据：`null`

### 8.3 公告列表

- URL：`GET /api/notice/page`
- 权限：销售、购房用户、超级管理员

查询参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `pageNo` | Integer | 否 | 页码 |
| `pageSize` | Integer | 否 | 每页条数 |

响应数据：`PageResult<NoticeVO>`

### 8.4 公告详情

- URL：`GET /api/notice/detail`
- 权限：销售、购房用户、超级管理员

查询参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | Long | 是 | 公告ID |

响应数据：`NoticeVO`

## 9. 用户与销售账号管理

### 9.1 创建销售账号

- URL：`POST /api/admin/sales/create`
- 权限：超级管理员

请求参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `username` | String | 是 | 登录账号 |
| `password` | String | 是 | 登录密码 |
| `realName` | String | 是 | 销售姓名 |
| `phone` | String | 是 | 手机号 |

响应数据：`id`

### 9.2 用户列表

- URL：`GET /api/admin/user/page`
- 权限：超级管理员

查询参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `pageNo` | Integer | 否 | 页码 |
| `pageSize` | Integer | 否 | 每页条数 |
| `keyword` | String | 否 | 账号、姓名、手机号 |
| `role` | Integer | 否 | 角色 |
| `status` | Integer | 否 | 状态 |

响应数据：`PageResult<UserVO>`

### 9.3 修改用户状态

- URL：`POST /api/admin/user/update-status`
- 权限：超级管理员

请求参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `userId` | Long | 是 | 用户ID |
| `status` | Integer | 是 | 状态：1启用，2禁用 |

响应数据：`null`

## 10. 数据统计

### 10.1 管理员总览统计

- URL：`GET /api/admin/statistics/overview`
- 权限：超级管理员

响应数据：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `totalRoomCount` | Long | 总房源数 |
| `soldRoomCount` | Long | 已售房源数 |
| `availableRoomCount` | Long | 待售房源数 |
| `reservedRoomCount` | Long | 已预订房源数 |
| `appointmentCount` | Long | 预约数量 |
| `userCount` | Long | 用户数量 |
| `salesCount` | Long | 销售数量 |

### 10.2 销售工作台统计

- URL：`GET /api/sales/statistics/overview`
- 权限：销售

响应数据：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `appointmentCount` | Long | 分配给当前销售的预约数 |
| `pendingAppointmentCount` | Long | 待跟进预约数 |
| `followCount` | Long | 跟进记录数 |

## 11. 操作日志

### 11.1 操作日志列表

- URL：`GET /api/admin/operation-log/page`
- 权限：超级管理员

查询参数：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `pageNo` | Integer | 否 | 页码 |
| `pageSize` | Integer | 否 | 每页条数 |
| `operatorName` | String | 否 | 操作人 |
| `moduleName` | String | 否 | 模块名 |
| `operationType` | String | 否 | 操作类型 |

响应数据：`PageResult<OperationLogVO>`

## 12. WebSocket 实时房源状态同步

### 12.1 连接地址

```text
ws://localhost:8080/ws/sale-control?token={jwtToken}
```

### 12.2 连接权限

- 超级管理员：可连接
- 销售：可连接
- 购房用户：可连接
- 未登录用户：拒绝连接

### 12.3 房源状态更新消息

管理后台调用 `POST /api/admin/room/update-status` 成功后，服务端向在线的销售端和用户端推送消息。管理后台也可以连接 WebSocket 接收同一消息，用于多窗口或多管理员页面同步。

消息格式：

```json
{
  "type": "ROOM_STATUS_UPDATE",
  "roomId": 1,
  "status": 2
}
```

字段说明：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `type` | String | 消息类型 |
| `roomId` | Long | 房源ID |
| `status` | Integer | 最新房源状态 |

### 12.4 前端处理规则

1. 前端收到 `ROOM_STATUS_UPDATE` 消息。
2. 判断当前页面是否存在对应 `roomId`。
3. 只更新该房源的 `status` 和状态颜色。
4. 不重新请求整个列表，不刷新整个页面。
5. 若当前页面不存在该 `roomId`，忽略本次消息。

## 13. 权限矩阵

| 模块 | 超级管理员 | 销售 | 购房用户 |
| --- | --- | --- | --- |
| 登录 | 是 | 是 | 是 |
| 注册 | 否 | 否 | 是 |
| 楼盘查询 | 是 | 是 | 是 |
| 楼盘管理 | 是 | 否 | 否 |
| 楼栋查询 | 是 | 是 | 是 |
| 楼栋管理 | 是 | 否 | 否 |
| 房源查询 | 是 | 是 | 是 |
| 房源管理 | 是 | 否 | 否 |
| 修改房源价格 | 是 | 否 | 否 |
| 修改房源状态 | 是 | 否 | 否 |
| 收藏房源 | 否 | 否 | 是 |
| 预约看房 | 否 | 否 | 是 |
| 查看预约 | 是 | 是 | 是，仅本人 |
| 客户跟进 | 否 | 是 | 否 |
| 公告发布 | 是 | 否 | 否 |
| 公告查看 | 是 | 是 | 是 |
| 用户管理 | 是 | 否 | 否 |
| 销售管理 | 是 | 否 | 否 |
| 数据统计 | 是 | 是，销售范围 | 否 |
| 操作日志 | 是 | 否 | 否 |

## 14. 后续开发映射

当前项目已经存在 Spring Boot 基础工程和 JWT 登录认证相关代码，后续开发应基于现有工程增量补齐模块，不倒回重建项目。推荐阶段顺序如下：

1. 架构与文档校准：统一管理后台为 Vue 网页端，修正目录、接口文档、SQL 口径。
2. 数据库 ER 图与 SQL：补齐角色、多角色关联、楼栋图片、单元、审计字段、索引。
3. RESTful 接口设计：确认三端接口、权限矩阵、统一响应和 WebSocket 消息。
4. Spring Boot 基础框架：复用当前已有基础能力继续完善。
5. JWT 登录认证：完善三端登录、当前用户、退出、权限拦截。
6. Vue 管理后台开发。
7. uni-app 用户端开发。
8. uni-app 销售端开发。
9. WebSocket 实时同步。
10. 联调测试。

下一步进入后端模块开发时，优先复用并完善以下已有能力：

- `pom.xml`
- `application.yml`
- `SalesControlApplication`
- `Result`
- `PageResult`
- `ErrorCode`
- `BusinessException`
- `GlobalExceptionHandler`
- `JwtUtil`
- `JwtAuthInterceptor`
- `UserRoleEnum`
- `UserStatusEnum`
- `User`
- `UserMapper`
- `UserMapper.xml`
- `AuthController`
- `AuthService`
- `AuthServiceImpl`
- `LoginRequest`
- `RegisterRequest`
- `LoginVO`
