# Recipe
Design and implementation of recipe sharing system based on SpringBoot

本系统后端开发采用Java作为开发语言；以SpringBoot+MybatisPlus为作为后端开发框架实现业务处理和流程控制；前端开发采用Vue、ElementUI、Vant 框架，结合HTML+CSS+JavaScript 分别实现微信小程序用户端页面开发、管理员系统页面开发。

数据库设计时设置可拓展字段很好地考虑了系统的可拓展和可维护性， 采用MySQL 持久化存储库+Redis 缓存方式确保能够保存用户有效信息的同时， 尽可能地减轻服务器端和持久化数据库的压力。 

## 环境

### 硬件环境

本系统开发运行对云端服务器和本地客户端的硬件要求：  客户小程序端为装好微信的手机。  

服务器设备名称是阿里云轻量应用服务器，设备系统是Alibaba Cloud Linux  3.2104 LTS 64 位，内存容量为2 GiB，系统盘为40GiB。  

平台管理员端设备系统：Windows，设备名称是LAPTOP-I009IFU0，CPU为 Intel(R) Core(TM) i7-8565U，内存容量为 8GB，硬盘容量为 256GB。  

### 软件环境

描述本软件开发所使用的计算机软件及版本，包括： 

 ① 操作系统：Windows 11  

 ② 数据库系统：MySQL 8.0.16  

③ 开发平台及工具：IntelliJ IDEA、Visual Studio Code、微信开发者工具  

④ 通信协议：HTTP2.0、HTTPS  

⑤ 其他软件：Navicat Premium（数据库管理）、RDM（Redis可视化工具）

## 主要技术 

- 本系统的关键技术包括使用Sa-token权限认证框架用于完成用户认证服务； 
- 使用Redis缓存技术以减小数据库的访存压力及提高系统性能；
- 使用Vue.js渐进式框架完成前端用户界面；
- 使用SpringBoot、MyBatis-plus 完成请求分发、后端 业务处理、数据库访问等操作。

## 功能结构图

![功能结构图](E:\work\graduate\功能结构图.png)

## 数据库表设计

![image-20241128193157410](C:\Users\86138\AppData\Roaming\Typora\typora-user-images\image-20241128193157410.png)

## 运行效果

一些效果图在show目录文件可以查看~

Recipe_wx.mp4是对微信小程序端的演示；

RecipeAdmin.mp4是对web管理系统端的演示。