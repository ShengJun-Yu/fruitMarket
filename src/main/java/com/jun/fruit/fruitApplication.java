package com.jun.fruit;

import javafx.application.Application;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author : Bojack
 * @date : Created in 10:35 2023.02.06
 */
//日志
@Slf4j
//启动注解
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement//因为DishServiceImpl要操控两个表，开启事务注解扫描
public class fruitApplication {
    public static void main(String[] args) {
        SpringApplication.run(fruitApplication.class, args);
        log.info("水果超市启动成功！");
    }
}
