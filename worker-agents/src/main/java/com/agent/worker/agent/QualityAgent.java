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
public class QualityAgent {
    private final RestTemplate restTemplate = new RestTemplate();
    private final RedisTemplate<String, String> redisTemplate;

    private static final String MCP_URL = "http://localhost:8081/mcp/quality";

    @PostMapping("/quality")
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public Result<Integer> checkQuality(@RequestBody JSONObject params) {
        String taskId = params.getString("taskId");
        log.info("质量检测 Agent 执行任务：{}", taskId);
        try {
            Result<Integer> result = restTemplate.postForObject(MCP_URL, params, Result.class);
            if (result.getCode() == 200) {
                redisTemplate.opsForValue().set("quality:" + taskId, result.getData().toString(), 3600);
                return result;
            } else {
                return Result.fail(result.getMsg());
            }
        } catch (Exception e) {
            log.error("质量检测 Agent 执行失败：{}", e.getMessage());
            return Result.fail("质量检测失败：" + e.getMessage());
        }
    }
}
