package com.sellinghouses.salescontrol.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("智慧售楼管理系统接口文档")
                        .version("1.0.0")
                        .description("智慧售楼管理系统后端接口，服务于 Vue 管理后台、uni-app 销售端和 uni-app 用户端"));
    }
}
