package com.rd.aicodegenerator.monitor;

import dev.langchain4j.model.chat.listener.ChatModelErrorContext;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import dev.langchain4j.model.output.TokenUsage;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Component
public class AiModelMonitorListener implements ChatModelListener {

    // 用于存储请求开始的时间
    private static final String REQUEST_START_TIME_KEY = "request_start_time";

    // 用户监控上下文传递（请求和响应事件的触发不是同一个线程）
    private static final String MONITOR_CONTEXT_KEY = "monitor_context";

    @Resource
    private AiModelMetricsCollector aiModelMetricsCollector;

    /**
     * 请求处理
     * @param requestContext
     */
    @Override
    public void onRequest(ChatModelRequestContext requestContext) {
        // 获取当前时间戳
        requestContext.attributes().put(REQUEST_START_TIME_KEY, Instant.now());
        // 从监控上下文获取信息
        MonitorContext monitorContext = MonitorContextHolder.getContext();
        String userId = monitorContext.getUserId();
        String appId = monitorContext.getAppId();
        requestContext.attributes().put(MONITOR_CONTEXT_KEY, monitorContext);
        // 获取模型名称
        String modelName = requestContext.chatRequest().modelName();
        // 记录请求指标
        aiModelMetricsCollector.recordRequest(userId, appId, modelName, "started");
    }

    /**
     * 响应处理
     * @param responseContext
     */
    @Override
    public void onResponse(ChatModelResponseContext responseContext) {
        // 从属性中获取监控信息
        Map<Object, Object> attributes = responseContext.attributes();
        // 从监控上下文获取信息
        MonitorContext monitorContext = MonitorContextHolder.getContext();
        String userId = monitorContext.getUserId();
        String appId = monitorContext.getAppId();
        // 获取模型名称
        String modelName = responseContext.chatRequest().modelName();
        // 记录请求指标
        aiModelMetricsCollector.recordRequest(userId, appId, modelName, "success");
        // 记录响应时间
        recordResponseTime(attributes, userId, appId, modelName);
        // 记录 Token 使用情况
        recordTokenUsage(responseContext, userId, appId, modelName);
    }

    /**
     * 错误处理
     * @param errorContext
     */
    @Override
    public void onError(ChatModelErrorContext errorContext) {
        // 从监控上下文中获取信息
        MonitorContext monitorContext = MonitorContextHolder.getContext();
        String userId = monitorContext.getUserId();
        String appId = monitorContext.getAppId();
        // 获取模型名称
        String modelName = errorContext.chatRequest().modelName();
        String errorMessage = errorContext.error().getMessage();
        // 记录失败请求
        aiModelMetricsCollector.recordRequest(userId, appId, modelName, "error");
        aiModelMetricsCollector.recordError(userId, appId, modelName, errorMessage);
        // 记录响应时间
        Map<Object, Object> attributes = errorContext.attributes();
        recordResponseTime(attributes, userId, appId, modelName);
    }

    /**
     * 记录响应时间
     * @param attributes
     * @param userId
     * @param appId
     * @param modelName
     */
    private void recordResponseTime(Map<Object, Object> attributes, String userId, String appId, String modelName) {
        Instant startTime = (Instant) attributes.get(REQUEST_START_TIME_KEY);
        Duration responseTime = Duration.between(startTime, Instant.now());
        aiModelMetricsCollector.recordResponseTime(userId, appId, modelName, responseTime);
    }

    /**
     * 记录 Token 使用情况
     * @param responseContext
     * @param userId
     * @param appId
     * @param modelName
     */
    private void recordTokenUsage(ChatModelResponseContext responseContext, String userId, String appId, String modelName) {
        TokenUsage tokenUsage = responseContext.chatResponse().metadata().tokenUsage();
        if (tokenUsage != null) {
            aiModelMetricsCollector.recordTokenUsage(userId, appId, modelName, "input", tokenUsage.inputTokenCount());
            aiModelMetricsCollector.recordTokenUsage(userId, appId, modelName, "output", tokenUsage.outputTokenCount());
            aiModelMetricsCollector.recordTokenUsage(userId, appId, modelName, "total", tokenUsage.totalTokenCount());
        }
    }
}
