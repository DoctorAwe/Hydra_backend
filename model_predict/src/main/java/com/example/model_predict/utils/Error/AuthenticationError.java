package com.example.model_predict.utils.Error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Dr.Awe
 * @date 2024-07-25 17:21
 */

@Getter
@AllArgsConstructor
public enum AuthenticationError implements ErrorType{
    AUTHENTICATION_FAILED(new ErrorData("200"," ")),
    EXISTED_USERNAME(new ErrorData("201"," ")),
    INVALID_USERNAME(new ErrorData("202"," "));


    private final ErrorData error;


}
