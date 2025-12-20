
# miniBili Deployment Guide

本项目包含 **管理端** 与 **用户端** 的前后端完整代码。本文档将指导你如何在本地或单台服务器上完成环境搭建与项目启动。
只有前端和单服务喔

## 📂 1. 目录结构说明 (Directory Structure)

项目主要分为后端服务 (`miniBili-server`) 和前端项目 (`miniBili-front`) 两大部分：

- **`miniBili-server/`** (后端工程 - Maven)
  - `common`: 公共模块（工具类、通用配置）。
  - `miniBili-admin`: **管理端**后端服务。
  - `miniBili-web`: **用户端**后端服务。
  
- **`miniBili-front/`** (前端工程 - Node.js)
  - `easylive-front-admin`: **管理端**前端代码。
  - `easylive-front-web`: **用户端**前端代码。

---

## 🛠 2. 环境准备 (Prerequisites)

在运行项目之前，请确保本地已安装以下基础环境和中间件。建议按照实验指导书要求，安装在本地环境。

### 基础环境
- **Java JDK**: 1.8 或更高版本
- **Maven**: 用于构建后端项目
- **Node.js**: 用于构建前端项目 

### 中间件 (Middleware)
请确保以下服务已安装并启动：
1.  **MySQL**: 关系型数据库
2.  **Redis**: 缓存服务
3.  **RocketMQ**: 消息队列
4.  **Elasticsearch**: 搜索引擎
5.  **FFmpeg**: 视频处理

---

## ⚙️ 3. 部署与启动 (Deployment)

### 第一步：数据库初始化

1.  在 MySQL 中创建数据库（通常库名需参考 SQL 文件中的定义）。
2.  执行根目录下的 `sql.sql` 脚本，导入所需的表结构和初始数据。

### 第二步：后端配置与启动 (Backend)

**1. 修改配置文件**
请根据你的本地环境（数据库密码、中间件地址等），分别修改以下两个服务的配置文件：

- **管理端配置**: 
  `miniBili-server/miniBili-admin/src/main/resources/application.yml`
- **用户端配置**: 
  `miniBili-server/miniBili-web/src/main/resources/application.yml`

> **注意**: 请重点检查 MySQL 账号密码、Redis 端口、RocketMQ 及 Elasticsearch 的连接地址是否正确。

**2. 编译公共模块**
由于 `admin`和 `web` 依赖 `common` 模块，首次运行前建议在 `miniBili-server` 目录下先安装依赖：
```bash
cd miniBili-server
mvn clean install
```

**3. 启动服务**
找到以下启动类并运行（推荐使用 IDE 如 IntelliJ IDEA）：

- **启动管理端**:
  运行 `miniBili-server/miniBili-admin/src/main/java/com/miniBili/admin/miniBiliAdminApplication.java`
- **启动用户端**:
  运行 `miniBili-server/miniBili-web/src/main/java/com/miniBili/web/miniBiliWebApplication.java`

---

### 第三步：前端配置与启动 (Frontend)

前端分为两个独立的项目，需要分别启动。

**1. 启动管理端前端**
```bash
# 进入管理端目录
cd miniBili-front/easylive-front-admin

# 下载依赖
npm install

# 启动开发环境
npm run dev
```

**2. 启动用户端前端**
```bash
# 进入用户端目录
cd miniBili-front/easylive-front-web

# 下载依赖
npm install

# 启动开发环境
npm run dev
```

---

## ✅ 4. 访问项目

项目启动成功后，通常可以通过以下地址访问（具体端口请查看控制台输出）：

- **管理端页面**: `http://localhost:端口号` (具体端口见 admin 前端控制台)
- **用户端页面**: `http://localhost:端口号` (具体端口见 web 前端控制台)
```

