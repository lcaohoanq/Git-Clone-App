package com.lcaohoanq.constant;

public enum HttpStatusCode {
    OK(200),
    CREATED(201),
    ACCEPTED(202),
    NO_CONTENT(204),
    UNPROCESSABLE_ENTITY(422),
    UNAUTHORIZED(401),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500),
    BAD_REQUEST(400),
    FORBIDDEN(403);

    private final int code;

    HttpStatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}