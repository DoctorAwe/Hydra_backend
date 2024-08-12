package com.example.model_predict.format.receive;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Dr.Awe
 * @date 2024-07-25 22:56
 */
@Data
@AllArgsConstructor
public class TaskForm {
    private String image_url;
    private String task_id;
}
