# 个人财务管理系统

## 技术栈
- 后端：Java 1.8 + Spring Boot 2.7 + MyBatis-Plus + MySQL 8.0
- 前端：Vue 3 + Vue Router + Axios + ECharts

## 项目结构
```
├── finance-backend/  # 后端项目
├── finance-frontend/ # 前端项目
└── README.md         # 项目说明
```

## 配置与启动

### 1. 数据库配置
1. 启动 MySQL 服务
2. 执行 `finance-backend/src/main/resources/schema.sql` 创建数据库和表结构
3. 修改 `finance-backend/src/main/resources/application.yml` 中的数据库连接信息（如果需要）

### 2. 启动后端
```bash
# 进入后端目录
cd finance-backend

# 构建并运行
mvn spring-boot:run
```

### 3. 启动前端
```bash
# 进入前端目录
cd finance-frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

### 4. 访问系统
- 前端地址：http://localhost:3000
- 后端 API 地址：http://localhost:8080/api

## 功能说明

### 核心功能
1. **用户认证**：注册、登录
2. **收支记录管理**：添加、编辑、删除收支记录，支持分类、备注、日期选择
3. **财务分类管理**：自定义收入和支出分类
4. **统计分析**：月度/年度收支统计，使用 ECharts 展示图表

### API 接口
- `POST /api/user/register` - 用户注册
- `POST /api/user/login` - 用户登录
- `GET /api/records/user/{userId}` - 获取用户的收支记录
- `POST /api/records` - 添加收支记录
- `PUT /api/records/{id}` - 更新收支记录
- `DELETE /api/records/{id}` - 删除收支记录
- `GET /api/categories/user/{userId}` - 获取用户的财务分类
- `POST /api/categories` - 添加财务分类
- `PUT /api/categories/{id}` - 更新财务分类
- `DELETE /api/categories/{id}` - 删除财务分类
- `GET /api/statistics/monthly/{userId}/{year}/{month}` - 获取月度统计数据
- `GET /api/statistics/yearly/{userId}/{year}` - 获取年度统计数据

## 注意事项
- 本项目为最小可行产品（MVP），仅保证基础功能可运行
- 未实现复杂的权限控制和高并发优化
- 数据库连接使用默认配置：用户名 root，密码 123456，数据库名 finance_db