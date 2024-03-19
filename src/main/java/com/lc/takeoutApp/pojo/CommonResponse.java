package com.lc.takeoutApp.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommonResponse<T> {
    private Integer code;
    private String message;
    private T data;

    public static <E> CommonResponse<E> success(E data){
        return new CommonResponse<>(0, "操作成功", data);
    }

    public static CommonResponse success(){
        return new CommonResponse(0, "操作成功", null);
    }

    public static CommonResponse error(String message){
        return new CommonResponse(1, message, null);
    }

    public static CommonResponse error(Integer code, String message){
        return new CommonResponse(code, message, null);
    }
}
