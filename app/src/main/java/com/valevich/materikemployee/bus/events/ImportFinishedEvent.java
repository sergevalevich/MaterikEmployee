package com.valevich.materikemployee.bus.events;

public class ImportFinishedEvent {
    private String message;

    public ImportFinishedEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
