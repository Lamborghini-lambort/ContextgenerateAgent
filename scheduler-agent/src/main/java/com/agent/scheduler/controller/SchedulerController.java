package com.agent.scheduler.controller;

import com.agent.common.Result;
import com.agent.scheduler.entity.TaskDO;
import com.agent.scheduler.service.TaskService;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/a2a/scheduler")
@RequiredArgsConstructor
@Slf4j
public class SchedulerController {
    private final TaskService taskService;

    @PostMapping("/start-task")
    public Result<JSONObject> startTask(@RequestBody JSONObject params) {
        try {
            String userId = params.getString("user_id");
            String productInfo = params.getString("product_info");
            String sceneType = params.getString("scene_type");

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
