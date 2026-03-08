package com.agent.scheduler;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.statemachine.config.EnableStateMachine;

@SpringBootApplication
@EnableRetry       // 启用重试
@MapperScan("com.agent.scheduler.mapper")
public class SchedulerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SchedulerApplication.class, args);
        System.out.println("调度Agent启动成功！端口：8080");
    }
}