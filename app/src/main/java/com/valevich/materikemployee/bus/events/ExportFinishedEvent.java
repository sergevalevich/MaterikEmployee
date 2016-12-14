package com.valevich.materikemployee.bus.events;

public class ExportFinishedEvent {
    private boolean isSuccessFull;
    private String message;

    public ExportFinishedEvent(boolean isSuccessFull, String message) {
        this.isSuccessFull = isSuccessFull;
        this.message = message;
    }

    public boolean isSuccessFull() {
        return isSuccessFull;
    }

    public String getMessage() {
        return message;
    }
}
