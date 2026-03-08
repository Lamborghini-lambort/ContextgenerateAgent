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
public class IntentAgent {
    private final RestTemplate restTemplate = new RestTemplate();
    private final RedisTemplate<String, String> redisTemplate;

    private static final String MCP_URL = "http://localhost:8081/mcp/intent";

    @PostMapping("/intent")
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public Result<String> analyzeIntent(@RequestBody JSONObject params) {
        String taskId = params.getString("taskId");
        log.info("意图理解 Agent 执行任务：{}", taskId);
        try {
            Result<String> result = restTemplate.postForObject(MCP_URL, params, Result.class);
            if (result.getCode() == 200) {
                redisTemplate.opsForValue().set("intent:" + taskId, result.getData(), 3600);
                return result;
            } else {
                return Result.fail(result.getMsg());
            }
        } catch (Exception e) {
            log.error("意图理解 Agent 执行失败：{}", e.getMessage());
            return Result.fail("意图理解失败：" + e.getMessage());
        }
    }
}
