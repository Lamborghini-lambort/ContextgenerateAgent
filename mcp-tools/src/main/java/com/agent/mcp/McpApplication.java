package com.agent.mcp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class McpApplication {
    public static void main(String[] args) {
        SpringApplication.run(McpApplication.class, args);
        System.out.println("MCP 工具中心启动成功！端口：8081");
    }
}
