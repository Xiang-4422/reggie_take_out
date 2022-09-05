package com.xy.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j                          //可以直接使用log变量，来输出日志
@SpringBootApplication          //SpringBoot启动类
@ServletComponentScan           //扫描Servlet，Filter，Listener
@EnableTransactionManagement    //开启事务
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class,args);
        log.info("项目启动成功...");
    }
}
