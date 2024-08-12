package com.example.model_predict.controller;

import com.example.model_predict.configuration.RabbitMQConfig;
import com.example.model_predict.format.receive.TaskForm;
import com.example.model_predict.format.receive.TaskResult;
import com.example.model_predict.utils.JsonUtils;
import jakarta.validation.constraints.Null;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.model_predict.utils.Result;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@CrossOrigin
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private Map<String, SseEmitter> taskEmitters = new ConcurrentHashMap<>();


    @PostMapping("/send")
    public String sendMessage(@RequestBody TaskForm data) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, JsonUtils.toJson(data));
        return "Message sent!";
    }


    @GetMapping("/subscribe")
    public SseEmitter startTask() {
        // 创建一个新的任务标识符
        String taskId = UUID.randomUUID().toString();

        // 创建一个新的SseEmitter
        SseEmitter emitter = new SseEmitter();
        taskEmitters.put(taskId, emitter);

        try {
            emitter.send(SseEmitter.event().name("taskId").data(taskId));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    @PostMapping("/start")
    public Result<Null> start(@RequestBody TaskResult taskResult) {
        try {
            SseEmitter emitter = taskEmitters.get(taskResult.getTask_id());
            if (emitter != null) {
                emitter.send(SseEmitter.event().name("taskStart").data("0"));
                return Result.success();

            } else {
                return Result.error("error");
            }
        }
        catch (RuntimeException |IOException e){
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/complete")
    public Result<Null> complete(@RequestBody TaskResult taskResult) {
        try {
            SseEmitter emitter = taskEmitters.get(taskResult.getTask_id());
            if (emitter != null) {
                emitter.send(SseEmitter.event().name("complete").data(taskResult.getOutputs()));
                emitter.complete();
                taskEmitters.remove(taskResult.getTask_id());
                return Result.success();

            } else {
                return Result.error("error");
            }
        }
        catch (RuntimeException |IOException e){
            return Result.error(e.getMessage());
        }

    }


}
