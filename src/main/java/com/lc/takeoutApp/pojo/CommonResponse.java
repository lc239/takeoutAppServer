package com.lc.takeoutApp.pojo;

import com.fasterxml.jackson.annotation.JsonView;
import com.lc.takeoutApp.view.View;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonView(View.class)
public class CommonResponse<T> {
    private Integer code;
    private String message;
    private T data;

    public static <E> CommonResponse<E> success(E data){
        return new CommonResponse<>(0, "操作成功", data);
    }

    public static CommonResponse<Void> success(){
        return new CommonResponse<>(0, "操作成功", null);
    }

    public static <E> CommonResponse<E> error(String message){
        return new CommonResponse<>(1, message, null);
    }

    public static <E> CommonResponse<E> error(Integer code, String message){
        return new CommonResponse<>(code, message, null);
    }
}
