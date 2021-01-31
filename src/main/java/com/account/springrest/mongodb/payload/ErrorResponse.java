package com.account.springrest.mongodb.payload;

/**
 * Created by Jose Corominas on 1/27/21.
 */
public class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
