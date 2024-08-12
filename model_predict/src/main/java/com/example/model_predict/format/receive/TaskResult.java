package com.example.model_predict.format.receive;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * @author Dr.Awe
 * @date 2024-07-26 13:31
 */
@Data
@AllArgsConstructor
public class TaskResult {
    private String task_id;
    private String secret;
    private String outputs;
}
