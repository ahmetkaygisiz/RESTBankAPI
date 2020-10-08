package com.restbank.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse<T> {
    private String message;
    private Info info;

    private T data;

    public GenericResponse(String message){
        this.message = message;
    }

    public GenericResponse(Info info, T data){
        this.info = info;
        this.data = data;
    }
}
