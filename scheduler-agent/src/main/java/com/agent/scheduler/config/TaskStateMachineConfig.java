package com.agent.scheduler.config;

import com.agent.common.TaskStatus;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachine // 核心注解：启用状态机
public class TaskStateMachineConfig extends StateMachineConfigurerAdapter<TaskStatus, Object> {

    @Override
    public void configure(StateMachineStateConfigurer<TaskStatus, Object> states) throws Exception {
        states
                .withStates()
                .initial(TaskStatus.INIT)
                .states(EnumSet.allOf(TaskStatus.class))
                .end(TaskStatus.PUBLISHED)
                .end(TaskStatus.FAILED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<TaskStatus, Object> transitions) throws Exception {
        transitions
                // 初始化 → 意图理解
                .withExternal()
                .source(TaskStatus.INIT).target(TaskStatus.INTENT_ANALYZING)
                .event("ANALYZE_INTENT")
                // 意图理解成功 → 关键词挖掘
                .and()
                .withExternal()
                .source(TaskStatus.INTENT_SUCCESS).target(TaskStatus.KEYWORD_MINING)
                .event("MINE_KEYWORD")
                // 关键词挖掘成功 → 文案生成
                .and()
                .withExternal()
                .source(TaskStatus.KEYWORD_SUCCESS).target(TaskStatus.GENERATING)
                .event("GENERATE")
                // 文案生成成功 → 质量检测
                .and()
                .withExternal()
                .source(TaskStatus.GENERATE_SUCCESS).target(TaskStatus.QUALITY_CHECKING)
                .event("CHECK_QUALITY")
                // 质量检测成功 → 格式排版
                .and()
                .withExternal()
                .source(TaskStatus.QUALITY_SUCCESS).target(TaskStatus.FORMATTING)
                .event("FORMAT")
                // 格式排版成功 → 内容审核
                .and()
                .withExternal()
                .source(TaskStatus.FORMAT_SUCCESS).target(TaskStatus.REVIEWING)
                .event("REVIEW")
                // 内容审核成功 → 任务完成
                .and()
                .withExternal()
                .source(TaskStatus.REVIEW_SUCCESS).target(TaskStatus.PUBLISHED)
                .event("PUBLISH")
                // 所有环节失败 → 任务失败
                .and()
                .withExternal()
                .source(TaskStatus.INTENT_ANALYZING).target(TaskStatus.FAILED)
                .event("FAILED")
                .and()
                .withExternal()
                .source(TaskStatus.KEYWORD_MINING).target(TaskStatus.FAILED)
                .event("FAILED")
                .and()
                .withExternal()
                .source(TaskStatus.GENERATING).target(TaskStatus.FAILED)
                .event("FAILED")
                .and()
                .withExternal()
                .source(TaskStatus.QUALITY_CHECKING).target(TaskStatus.FAILED)
                .event("FAILED")
                .and()
                .withExternal()
                .source(TaskStatus.FORMATTING).target(TaskStatus.FAILED)
                .event("FAILED")
                .and()
                .withExternal()
                .source(TaskStatus.REVIEWING).target(TaskStatus.FAILED)
                .event("FAILED");
    }
}
