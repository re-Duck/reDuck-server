package reduck.reduck.global.entity;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class Response<T> implements Serializable{
    private T data;
    private ResponseStatus status;
    private String message;

    private Response(T data, ResponseStatus status, String message) {
        this.data = data;
        this.status = status;
        this.message = message;

    }

    public static <T> Response<T> errorResponse(String message) {
        return new Response<>(null, ResponseStatus.ERROR, message);
    }

    public static <T> Response<T> successResponse(T data) {
        return new Response<>(data, ResponseStatus.SUCCESS, null);
    }
}

enum ResponseStatus {
    SUCCESS, ERROR
}