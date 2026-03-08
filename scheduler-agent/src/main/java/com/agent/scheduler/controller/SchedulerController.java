package com.agent.scheduler.controller;

<<<<<<< HEAD
import com.agent.common.Result;
import com.agent.scheduler.entity.TaskDO;
import com.agent.scheduler.service.TaskService;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
=======
import com.agent.scheduler.client.A2aClient;
import com.agent.scheduler.common.Result;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
>>>>>>> 25110adb6b6aa7e25fd44551bf95318917820961
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

<<<<<<< HEAD
@RestController
@RequestMapping("/a2a/scheduler")
@RequiredArgsConstructor
@Slf4j
public class SchedulerController {
    private final TaskService taskService;

    @PostMapping("/start-task")
    public Result<JSONObject> startTask(@RequestBody JSONObject params) {
        try {
=======
import java.util.UUID;

@RestController
@RequestMapping("/a2a/scheduler")
@RequiredArgsConstructor
public class SchedulerController {
    private final A2aClient a2aClient;

    @Value("${agent.generator.url}")
    private String generatorAgentUrl;

    @Value("${agent.review.url}")
    private String reviewAgentUrl;

    // 接收用户任务
    @PostMapping("/task")
    public Result<JSONObject> receiveTask(@RequestBody JSONObject params) {
        try {
            // 1. 生成唯一任务 ID
            String taskId = "task_" + UUID.randomUUID().toString().substring(0, 8);
>>>>>>> 25110adb6b6aa7e25fd44551bf95318917820961
            String userId = params.getString("user_id");
            String productInfo = params.getString("product_info");
            String sceneType = params.getString("scene_type");

<<<<<<< HEAD
            if (userId == null || productInfo == null || sceneType == null) {
                return Result.fail("参数缺失：user_id、product_info、scene_type 不能为空");
            }

            String taskId = taskService.startTask(userId, productInfo, sceneType);
            
            // 立即查询任务状态以便调试
            TaskDO task = taskService.getTask(taskId);
            log.info("任务 [{}] 当前状态：status={}, failReason={}", 
                    taskId, task.getStatus(), task.getFailReason());
            
            return Result.success(JSONObject.of(
                    "task_id", taskId,
                    "msg", "多 Agent 流水线任务已启动",
                    "status", "processing",
                    "current_status_code", task.getStatus()
            ));
        } catch (Exception e) {
            log.error("启动任务失败：{}", e.getMessage(), e);
            return Result.fail("启动任务失败：" + e.getMessage());
        }
    }

    @PostMapping("/task-status")
    public Result<TaskDO> getTaskStatus(@RequestBody JSONObject params) {
        try {
            log.info("收到查询任务状态请求：{}", params);
            
            // 兼容 taskId 和 task_id 两种命名
            String taskId = params.getString("task_id");
            if (taskId == null || taskId.trim().isEmpty()) {
                taskId = params.getString("taskId");  // 尝试驼峰命名
            }
            log.info("提取的 task_id: {}", taskId);
            
            if (taskId == null || taskId.trim().isEmpty()) {
                log.error("task_id 为空或 null");
                return Result.fail("参数缺失：task_id 不能为空");
            }
            
            log.info("开始查询任务 [{}] 的状态", taskId);
            Result<TaskDO> result = taskService.getTaskStatus(taskId);
            log.info("查询任务 [{}] 状态完成，结果：{}", taskId, result);
            return result;
        } catch (Exception e) {
            log.error("查询任务状态失败：{}", e.getMessage(), e);
            return Result.fail("查询任务状态失败：" + e.getMessage());
        }
    }
}
=======
            // 2. 构建 A2A 任务，发送给文案生成 Agent
            JSONObject generateTask = new JSONObject();
            generateTask.put("protocol", "A2A/1.0");
            generateTask.put("task_id", taskId);
            generateTask.put("from", "scheduler_agent");
            generateTask.put("to", "generator_agent");
            
            JSONObject taskContent = new JSONObject();
            taskContent.put("input", JSONObject.of(
                    "user_id", userId,
                    "product_info", productInfo,
                    "scene_type", sceneType
            ));
            taskContent.put("priority", 1);
            taskContent.put("timeout", 30000);
            generateTask.put("task", taskContent);

            // 调用生成 Agent
            JSONObject generateResp = a2aClient.sendTask(
                    generatorAgentUrl + "/a2a/generator/task",
                    generateTask
            );
            if (!generateResp.getBoolean("success")) {
                return Result.fail("文案生成失败：" + generateResp.getString("error"));
            }
            String content = generateResp.getJSONObject("data").getString("content");

            // 3. 构建 A2A 任务，发送给审核 Agent
            JSONObject reviewTask = new JSONObject();
            reviewTask.put("protocol", "A2A/1.0");
            reviewTask.put("task_id", taskId);
            reviewTask.put("from", "scheduler_agent");
            reviewTask.put("to", "review_agent");
            
            JSONObject reviewTaskContent = new JSONObject();
            reviewTaskContent.put("input", JSONObject.of("content", content));
            reviewTaskContent.put("priority", 1);
            reviewTaskContent.put("timeout", 10000);
            reviewTask.put("task", reviewTaskContent);

            // 调用审核 Agent
            JSONObject reviewResp = a2aClient.sendTask(
                    reviewAgentUrl + "/a2a/review/task",
                    reviewTask
            );
            if (!reviewResp.getBoolean("success")) {
                return Result.fail("内容审核失败：" + reviewResp.getString("error"));
            }
            JSONObject reviewResult = reviewResp.getJSONObject("data");

            // 4. 汇总结果
            JSONObject finalResult = new JSONObject();
            finalResult.put("task_id", taskId);
            finalResult.put("content", content);
            finalResult.put("review_result", reviewResult);
            finalResult.put("user_id", userId);

            return Result.success(finalResult);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }
}
>>>>>>> 25110adb6b6aa7e25fd44551bf95318917820961
