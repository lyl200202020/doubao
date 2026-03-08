# 客户运营管理后台

## 项目简介

基于 Vue3 + Vite + TypeScript + Element Plus 的中后台管理系统

## 技术栈

- **框架**: Vue3.4 + Vite5
- **语言**: TypeScript
- **UI组件**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router
- **HTTP**: Axios
- **样式**: SCSS

## 项目结构

```
src/
├── api/                  # API接口
│   ├── auth.ts          # 认证接口
│   └── sys/             # 系统模块接口
├── assets/              # 静态资源
│   └── styles/          # 全局样式
├── components/          # 组件
│   ├── Basic/           # 基础组件
│   ├── Business/        # 业务组件
│   └── Layout/         # 布局组件
├── composables/         # 组合式函数
├── directives/          # 指令
├── router/              # 路由
│   ├── routes/         # 路由配置
│   └── guard/          # 路由守卫
├── store/              # 状态管理
│   └── modules/         # Store模块
├── utils/              # 工具函数
└── views/              # 页面视图
    ├── login/          # 登录页
    ├── dashboard/      # 首页
    ├── sys/           # 系统管理
    ├── auth/          # 授权管理
    └── crm/           # 客户管理
```

## 开发指南

### 安装依赖

```bash
npm install
```

### 启动开发服务器

```bash
npm run dev
```

项目将在 http://localhost:3000 启动

### 构建生产版本

```bash
npm run build
```

### 代码检查

```bash
npm run lint
```

## 功能特性

- ✅ 登录/退出
- ✅ 动态路由
- ✅ 权限控制
- ✅ Token管理
- ✅ 面包屑导航
- ✅ 侧边栏菜单
- ✅ 用户下拉菜单

## 默认账号

- 用户名: admin
- 密码: admin123

## 接口配置

项目默认代理到后端服务 http://localhost:8080

如需修改，请修改 vite.config.ts 中的 proxy 配置
