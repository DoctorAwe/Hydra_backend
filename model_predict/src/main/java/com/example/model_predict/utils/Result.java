package com.example.model_predict.utils;

import com.example.model_predict.utils.Error.ErrorData;
import com.example.model_predict.utils.Error.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private int code;
    private String msg;
    private T data;

    public static <E>Result<E> success(E data){
        return new Result<E>(0,"success",data);
    }
    public static <E>Result<E> success(String msg, E data){
        return new Result<E>(0,msg ,data);
    }
    public static <E>Result<E> success(String msg){
        return new Result<E>(0,msg ,null);
    }
    public static <E>Result<E> success(){ return new Result<E>(0,"success",null); }
    public static <E>Result<E> error(String msg){ return new Result<E>(1,msg,null); }
    public static Result<ErrorData> error(ErrorType error){
        return new Result<ErrorData>(1,"error", error.getError());
    }

}
