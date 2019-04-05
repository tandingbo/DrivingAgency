# DrivingAgency

驾校代理小程序后端

## 技术包括

1.  Spring Boot 2.1.3

2.  Spring Data Jpa

3.  Spring Data Redis

4.  Spring Data Mongodb

5.  Spring Boot Mail

6.  Swagger

7.  Quartz

8.  JWT Token

9.  FastDFS

## 要点
1.  热点数据入Redis,提高SQL的查询性能
2.  认证采用Jwt Token+Refresh Token+黑名单策略,提高用户体验
3.  权限管理采用规范的RBAC权限模型
4.  点赞、排行等数据入Redis,每天进行一次Quartz任务调度,Redis中数据落库
5.  评论、发布公告等信息入Mongodb，便于存储、操作

