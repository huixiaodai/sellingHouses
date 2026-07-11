# 智慧售楼管理系统 - 项目目录结构

## 架构定位

本项目采用前后端分离架构，包含三个端：

- 管理后台：`Vue3 + Vite + Element Plus`，面向超级管理员，运行在网页端。
- 销售端：`uni-app + Vue3`，面向销售人员，优先使用跨平台 API。
- 用户端：`uni-app + Vue3`，面向购房用户，优先使用跨平台 API。

管理后台不再放入小程序或 uni-app 目录；销售端和用户端继续保持跨平台写法，禁止直接使用 `wx.xxx` 等微信专有 API。确需平台专有能力时，必须标注“【微信小程序专用】”。

## 根目录

```text
SellingHouses/
├── backend/
│   └── smart-sales-control/
├── admin-web/
├── uni-app/
│   ├── user-app/
│   └── sales-app/
├── docs/
│   └── 03-api-design.md
├── sql/
│   └── 02-database-schema.sql
└── 01-project-structure.md
```

## 后端 Spring Boot 目录

```text
backend/smart-sales-control/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── sellinghouses/
│   │   │           └── salescontrol/
│   │   │               ├── SalesControlApplication.java
│   │   │               ├── common/
│   │   │               │   ├── config/
│   │   │               │   ├── constant/
│   │   │               │   ├── context/
│   │   │               │   ├── enums/
│   │   │               │   ├── exception/
│   │   │               │   ├── interceptor/
│   │   │               │   ├── result/
│   │   │               │   └── util/
│   │   │               ├── module/
│   │   │               │   ├── auth/
│   │   │               │   ├── user/
│   │   │               │   ├── role/
│   │   │               │   ├── building/
│   │   │               │   ├── room/
│   │   │               │   ├── favorite/
│   │   │               │   ├── appointment/
│   │   │               │   ├── notice/
│   │   │               │   ├── statistics/
│   │   │               │   ├── log/
│   │   │               │   └── websocket/
│   │   │               └── support/
│   │   │                   └── aspect/
│   │   └── resources/
│   │       ├── application.yml
│   │       └── mapper/
│   └── test/
│       └── java/
```

后端分层规范：

- `controller`：只负责参数接收、基础校验、调用 Service、返回统一结果。
- `service`：负责业务编排、权限校验、状态流转、事务控制。
- `mapper`：负责数据库访问，SQL 字段显式列出，禁止 `select *`。
- `entity`：只映射数据库表，不直接作为接口入参或响应。
- `dto`：接口请求对象。
- `vo`：接口响应对象。

## 管理后台 Vue 目录

```text
admin-web/
├── package.json
├── vite.config.js
├── index.html
├── src/
│   ├── main.js
│   ├── App.vue
│   ├── api/
│   │   ├── auth.js
│   │   ├── building.js
│   │   ├── room.js
│   │   ├── sales.js
│   │   ├── user.js
│   │   ├── appointment.js
│   │   ├── notice.js
│   │   ├── statistics.js
│   │   └── operationLog.js
│   ├── components/
│   │   ├── StatusTag.vue
│   │   ├── PageTable.vue
│   │   └── SearchForm.vue
│   ├── router/
│   │   └── index.js
│   ├── store/
│   │   ├── auth.js
│   │   └── app.js
│   ├── utils/
│   │   ├── request.js
│   │   ├── token.js
│   │   └── permission.js
│   └── views/
│       ├── login/
│       ├── dashboard/
│       ├── building/
│       ├── room/
│       ├── sales/
│       ├── user/
│       ├── appointment/
│       ├── notice/
│       ├── statistics/
│       └── operation-log/
└── public/
```

管理后台调用 `/api/admin` 和通用查询接口，统一通过 `src/utils/request.js` 封装 Axios，请求自动携带 Token，响应统一处理错误。

## uni-app 用户端目录

```text
uni-app/user-app/
├── package.json
├── pages.json
├── manifest.json
├── App.vue
├── main.js
├── api/
│   ├── auth.js
│   ├── building.js
│   ├── room.js
│   ├── favorite.js
│   ├── appointment.js
│   └── notice.js
├── components/
│   ├── StatusBadge.vue
│   ├── RoomCard.vue
│   └── EmptyState.vue
├── hooks/
│   └── useRoomSocket.js
├── pages/
│   ├── login/
│   ├── register/
│   ├── home/
│   ├── building-list/
│   ├── building-detail/
│   ├── sale-control/
│   ├── room-detail/
│   ├── favorite/
│   ├── appointment-create/
│   ├── appointment-list/
│   ├── notice/
│   └── profile/
├── static/
│   ├── icons/
│   └── images/
├── store/
│   ├── auth.js
│   └── room.js
└── utils/
    ├── request.js
    ├── route.js
    ├── storage.js
    └── validator.js
```

## uni-app 销售端目录

```text
uni-app/sales-app/
├── package.json
├── pages.json
├── manifest.json
├── App.vue
├── main.js
├── api/
│   ├── auth.js
│   ├── building.js
│   ├── room.js
│   ├── appointment.js
│   ├── notice.js
│   └── statistics.js
├── components/
│   ├── StatusBadge.vue
│   ├── RoomCard.vue
│   └── EmptyState.vue
├── hooks/
│   └── useRoomSocket.js
├── pages/
│   ├── login/
│   ├── home/
│   ├── building-list/
│   ├── sale-control/
│   ├── room-detail/
│   ├── appointment-list/
│   ├── appointment-detail/
│   ├── notice/
│   └── profile/
├── static/
│   ├── icons/
│   └── images/
├── store/
│   ├── auth.js
│   └── room.js
└── utils/
    ├── request.js
    ├── route.js
    ├── storage.js
    └── validator.js
```

uni-app 端统一使用：

- `uni.request`
- `uni.navigateTo`
- `uni.redirectTo`
- `uni.switchTab`
- `uni.showToast`
- `uni.showModal`
- `uni.setStorageSync`
- `uni.getStorageSync`
- `uni.uploadFile`
- `uni.connectSocket`
- `uni.onSocketMessage`

## 分阶段开发顺序

0. 架构与文档校准：统一管理后台为 Vue 网页端，修正目录、接口文档、SQL 口径。
1. 项目目录结构：确认后端、管理后台、用户端、销售端目录。
2. 数据库 ER 图与 SQL：补齐 `role`、`user_role`、`building_image`、`unit`、审计字段和索引。
3. RESTful 接口设计：确认三端接口前缀、权限、请求响应、WebSocket 消息格式。
4. Spring Boot 基础框架：基于当前已有工程增量开发，不倒回重建。
5. JWT 登录认证：完善管理员、销售、用户登录和权限校验。
6. Vue 管理后台开发：登录、Dashboard、楼盘、楼栋、房源、销售、用户、公告、预约、统计、日志。
7. uni-app 用户端开发：首页、楼盘、房源、收藏、预约、公告、我的。
8. uni-app 销售端开发：首页、楼盘、房源、客户预约、公告、个人中心。
9. WebSocket 实时同步：管理员修改房源状态后，销售端和用户端只更新对应 `roomId`。
10. 联调测试：接口、权限、状态同步、兼容性、异常流程。

每完成一个阶段后等待确认，再进入下一阶段。

## 命名约定

- 后端基础包名：`com.sellinghouses.salescontrol`
- 后端工程名：`smart-sales-control`
- 管理后台目录：`admin-web`
- 用户端目录：`uni-app/user-app`
- 销售端目录：`uni-app/sales-app`
- 接口统一前缀：`/api`
- 管理端接口前缀：`/api/admin`
- 销售端接口前缀：`/api/sales`
- 用户端接口前缀：`/api/user`
- 通用查询接口使用 `GET`
- 新增、修改、删除、状态变更均使用 `POST`
- 数据库表名使用小写下划线
- Java 类名按模块分为 `Entity`、`Request`、`VO`、`Mapper`、`Service`、`Controller`
