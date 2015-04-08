package com.acid.findme.exception;

public class JSONParserException extends Exception {
    private String reason;

    public JSONParserException()
    {
        setReason("");
    }

    public String getReason() {
        return reason;
    }

    public boolean hasReason() {
        return reason != null;
    }

    public void setReason(String reason) {
        if(reason == null)
            this.reason = "";
        else
            this.reason = reason;
    }
}
