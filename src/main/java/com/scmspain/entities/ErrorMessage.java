package com.scmspain.entities;

public class ErrorMessage {

    private String message;
    private String exceptionClass;

    public ErrorMessage(String message, String exceptionClass) {
	super();
	this.message = message;
	this.exceptionClass = exceptionClass;
    }

    public ErrorMessage() {
	super();
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public String getExceptionClass() {
	return exceptionClass;
    }

    public void setExceptionClass(String exceptionClass) {
	this.exceptionClass = exceptionClass;
    }
}
