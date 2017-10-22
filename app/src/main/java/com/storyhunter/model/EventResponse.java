package com.storyhunter.model;

import java.io.Serializable;

public class EventResponse implements Serializable {
    public final int status_code;
    public final String description;
    public final String photo_url;

    public EventResponse(final int status_code, final String description, final String photo_url) {
        this.status_code = status_code;
        this.description = description;
        this.photo_url = photo_url;
    }
}