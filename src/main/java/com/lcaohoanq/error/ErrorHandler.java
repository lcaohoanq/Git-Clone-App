package com.lcaohoanq.error;

public class ErrorHandler extends Exception{

    private String message;
    private int statusCode;

    public ErrorHandler(String message) {
        super(message);
    }

    public ErrorHandler(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
