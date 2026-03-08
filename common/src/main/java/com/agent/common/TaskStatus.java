package com.agent.common;

import lombok.Getter;

@Getter
public enum TaskStatus {
    INIT("初始化", 0),
    INTENT_ANALYZING("意图理解中", 1),
    INTENT_SUCCESS("意图理解成功", 2),
    KEYWORD_MINING("关键词挖掘中", 3),
    KEYWORD_SUCCESS("关键词挖掘成功", 4),
    GENERATING("文案生成中", 5),
    GENERATE_SUCCESS("文案生成成功", 6),
    QUALITY_CHECKING("质量检测中", 7),
    QUALITY_SUCCESS("质量检测成功", 8),
    FORMATTING("格式排版中", 9),
    FORMAT_SUCCESS("格式排版成功", 10),
    REVIEWING("内容审核中", 11),
    REVIEW_SUCCESS("内容审核成功", 12),
    PUBLISHED("任务完成", 13),
    FAILED("任务失败", 14);

    private final String desc;
    private final int code;

    TaskStatus(String desc, int code) {
        this.desc = desc;
        this.code = code;
    }

    public static TaskStatus getByCode(int code) {
        for (TaskStatus status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return INIT;
    }
}
