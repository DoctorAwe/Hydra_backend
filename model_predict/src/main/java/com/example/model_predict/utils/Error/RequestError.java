package com.example.model_predict.utils.Error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Dr.Awe
 * @date 2024-07-25 17:20
 */
@Getter
@AllArgsConstructor
public enum RequestError implements ErrorType{
    INVALID_PATH(new ErrorData("100"," ")),
    INVALID_PARAMETERS(new ErrorData("101"," "));

    private final ErrorData error;

}
