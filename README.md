# open-api-server

## 1.0.0

### docker build
```
mvn clean package docker:build
```

### docker-compose
```
version: '2'
services:
    open-api-server:
        image: registry.cn-beijing.aliyuncs.com/touchfound/open-api-server:1.0.0-beta
        volumes:
            # 日志目录 主机:容器
            - /logs:/logs
        environment:
            # dev/uat/pro 默认dev
            - spring.cloud.config.profile=dev
        ports:
            # 映射端口 主机：容器
            - "8080:8080"
        container_name: open-api-server

```