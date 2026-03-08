package com.agent.worker.agent;

import com.agent.common.Result;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/worker")
@RequiredArgsConstructor
@Slf4j
public class FormatAgent {
    private final RestTemplate restTemplate = new RestTemplate();
    private final RedisTemplate<String, String> redisTemplate;

    private static final String MCP_URL = "http://localhost:8081/mcp/format";

    @PostMapping("/format")
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public Result<String> formatContent(@RequestBody JSONObject params) {
        String taskId = params.getString("taskId");
        log.info("格式排版 Agent 执行任务：{}", taskId);
        try {
            Result<String> result = restTemplate.postForObject(MCP_URL, params, Result.class);
            if (result.getCode() == 200) {
                redisTemplate.opsForValue().set("format:" + taskId, result.getData(), 3600);
                return result;
            } else {
                return Result.fail(result.getMsg());
            }
        } catch (Exception e) {
            log.error("格式排版 Agent 执行失败：{}", e.getMessage());
            return Result.fail("格式排版失败：" + e.getMessage());
        }
    }
}
