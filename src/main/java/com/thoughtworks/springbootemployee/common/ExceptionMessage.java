package com.thoughtworks.springbootemployee.common;

public enum ExceptionMessage {
    ILLEGALOPRATION("Illegal Operation!"),
    NO_SUCH_DATA("No Such Data!"),
    IllegalArgumentException("Illegal argument!");

    private String errorMsg;

    ExceptionMessage(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
