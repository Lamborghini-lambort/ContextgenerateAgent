package com.agent.scheduler.entity;

import com.agent.common.TaskStatus;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("ai_marketing_task")
public class TaskDO {
    @TableId(type = IdType.ASSIGN_UUID)
    private String taskId;
    private String userId;
    private String productInfo;
    private String sceneType;
    private String intent;
    private String keywords;
    private String content;
    private Integer qualityScore;
    private String reviewResult;
    private String formattedContent;
    private Integer status;
    private String failReason;
    private Integer retryCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public TaskDO() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.retryCount = 0;
        this.status = TaskStatus.INIT.getCode();
        this.qualityScore = 0;
    }
}
