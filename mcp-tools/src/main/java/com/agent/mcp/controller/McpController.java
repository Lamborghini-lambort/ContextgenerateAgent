package com.agent.mcp.controller;

<<<<<<< HEAD
import com.agent.common.Result;
import com.alibaba.fastjson2.JSONObject;
=======
import com.agent.mcp.client.OpenAiClient;
import com.agent.mcp.common.Result;
import lombok.RequiredArgsConstructor;
>>>>>>> 25110adb6b6aa7e25fd44551bf95318917820961
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

<<<<<<< HEAD
@RestController
@RequestMapping("/mcp")
public class McpController {

    @PostMapping("/intent")
    public Result<String> analyzeIntent(@RequestBody JSONObject params) {
        try {
            String productInfo = params.getString("productInfo");
            String sceneType = params.getString("sceneType");
            String intent = String.format("""
                    【意图分析结果】
                    - 推广产品：%s
                    - 目标场景：%s
                    - 核心需求：生成符合场景风格的营销文案
                    - 目标受众：18-35 岁年轻消费群体
                    - 风格要求：活泼、有吸引力、突出核心卖点
                    """, productInfo, sceneType);
            return Result.success(intent);
        } catch (Exception e) {
            return Result.fail("意图理解失败：" + e.getMessage());
        }
    }

    @PostMapping("/keyword")
    public Result<String> mineKeyword(@RequestBody JSONObject params) {
        try {
            String productInfo = params.getString("productInfo");
            String keywords = "智能手机，长续航，5000 万像素，2025 新款，性价比，拍照手机，快充";
            return Result.success(keywords);
        } catch (Exception e) {
            return Result.fail("关键词挖掘失败：" + e.getMessage());
        }
    }

    @PostMapping("/generate")
    public Result<String> generateText(@RequestBody JSONObject params) {
        try {
            String productInfo = params.getString("productInfo");
            String keywords = params.getString("keywords");
            String sceneType = params.getString("sceneType");
            String content = String.format("""
                    🔥 %s太香了！%s
                    核心卖点全拉满：%s
                    不管是日常使用还是拍照打卡，都能满足你的所有需求～
                    性价比直接拉满，闭眼冲就完事！
                    """, sceneType, productInfo, keywords);
            return Result.success(content);
        } catch (Exception e) {
            return Result.fail("文案生成失败：" + e.getMessage());
        }
    }

    @PostMapping("/quality")
    public Result<Integer> checkQuality(@RequestBody JSONObject params) {
        try {
            String content = params.getString("content");
            int score = 92;
            return Result.success(score);
        } catch (Exception e) {
            return Result.fail("质量检测失败：" + e.getMessage());
        }
    }

    @PostMapping("/format")
    public Result<String> formatContent(@RequestBody JSONObject params) {
        try {
            String content = params.getString("content");
            String sceneType = params.getString("sceneType");
            String formattedContent;
            if ("小红书".equals(sceneType)) {
                formattedContent = "✨ 姐妹们！这款手机真的绝了！\n\n" + content + "\n\n#小红书种草 #数码好物 #手机推荐";
            } else if ("抖音".equals(sceneType)) {
                formattedContent = "🔥 家人们！新款手机来了！\n\n" + content + "\n\n#抖音好物 #数码科技 #新品上市";
            } else {
                formattedContent = "【电商推广文案】\n\n" + content + "\n\n#电商爆款 #性价比手机 #现货速发";
            }
            return Result.success(formattedContent);
        } catch (Exception e) {
            return Result.fail("格式排版失败：" + e.getMessage());
        }
    }

    @PostMapping("/review")
    public Result<String> reviewText(@RequestBody JSONObject params) {
        try {
            String content = params.getString("content");
            JSONObject reviewResult = new JSONObject();
            reviewResult.put("safe", true);
            reviewResult.put("sensitiveWord", "无");
            reviewResult.put("score", 98);
            reviewResult.put("suggestion", "内容合规，可直接发布");
            return Result.success(reviewResult.toString());
        } catch (Exception e) {
            return Result.fail("内容审核失败：" + e.getMessage());
        }
    }
}
=======
import java.util.Map;

@RestController
@RequestMapping("/mcp")
@RequiredArgsConstructor
public class McpController {
    private final OpenAiClient openAiClient;

    // 生成营销文案（MCP 工具1）
    @PostMapping("/generateText")
    public Result<String> generateText(@RequestBody Map<String, Object> params) {
        try {
            String prompt = (String) params.get("prompt");
            double temperature = params.containsKey("temperature") ? (double) params.get("temperature") : 0.7;
            String content = openAiClient.generateText(prompt, temperature);
            return Result.success(content);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    // 内容审核（MCP 工具2）
    @PostMapping("/reviewText")
    public Result<Map<String, Object>> reviewText(@RequestBody Map<String, Object> params) {
        try {
            String content = (String) params.get("content");
            // 简化版审核逻辑（实际可替换成第三方审核 API）
            String[] sensitiveWords = {"违规", "违法", "虚假"};
            boolean isCompliant = true;
            String reason = "无敏感词";
            for (String word : sensitiveWords) {
                if (content.contains(word)) {
                    isCompliant = false;
                    reason = "包含敏感词：" + word;
                    break;
                }
            }
            Map<String, Object> result = Map.of(
                    "is_compliant", isCompliant,
                    "reason", reason
            );
            return Result.success(result);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }
}
>>>>>>> 25110adb6b6aa7e25fd44551bf95318917820961
