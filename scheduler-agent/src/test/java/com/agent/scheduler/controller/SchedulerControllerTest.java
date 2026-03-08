package com.agent.scheduler.controller;

import com.alibaba.fastjson2.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

/**
 * SchedulerController 测试类
 */
@SpringBootTest
public class SchedulerControllerTest {

    @Autowired
    private SchedulerController schedulerController;

    private MockMvc mockMvc;

    /**
     * 测试启动任务接口
     */
    @Test
    public void testStartTask() throws Exception {
        if (mockMvc == null) {
            mockMvc = webAppContextSetup(null).build();
        }

        JSONObject params = new JSONObject();
        params.put("user_id", "test_user_001");
        params.put("product_info", "这是一款高性能无线蓝牙耳机，具有主动降噪功能");
        params.put("scene_type", "电商详情页");

        String response = mockMvc.perform(MockMvcRequestBuilders
                .post("/a2a/scheduler/start-task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(params.toJSONString()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        System.out.println("=== 启动任务响应 ===");
        System.out.println(response);
        
        JSONObject result = JSONObject.parseObject(response);
        assert result.getInteger("code") == 200 : "任务启动应该成功";
    }

    /**
     * 测试查询任务状态接口
     */
    @Test
    public void testGetTaskStatus() throws Exception {
        if (mockMvc == null) {
            mockMvc = webAppContextSetup(null).build();
        }

        // 先创建一个任务
        JSONObject startParams = new JSONObject();
        startParams.put("user_id", "test_user_002");
        startParams.put("product_info", "智能手表测试产品");
        startParams.put("scene_type", "社交媒体");
        
        String startResponse = mockMvc.perform(MockMvcRequestBuilders
                .post("/a2a/scheduler/start-task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(startParams.toJSONString()))
                .andReturn()
                .getResponse()
                .getContentAsString();
        
        JSONObject startResult = JSONObject.parseObject(startResponse);
        String taskId = startResult.getJSONObject("data").getString("task_id");
        
        // 查询任务状态
        JSONObject queryParams = new JSONObject();
        queryParams.put("task_id", taskId);

        String statusResponse = mockMvc.perform(MockMvcRequestBuilders
                .post("/a2a/scheduler/task-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(queryParams.toJSONString()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        System.out.println("=== 任务状态响应 ===");
        System.out.println(statusResponse);
        
        JSONObject result = JSONObject.parseObject(statusResponse);
        assert result.getInteger("code") == 200 : "查询状态应该成功";
    }
}
