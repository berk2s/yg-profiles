package com.yataygecisle.preference.profiles.web.model;

import lombok.Getter;

@Getter
public enum ErrorType {
    INVALID_REQUEST("invalid_request"),
    SERVER_ERROR("server_error"),
    NOT_FOUND("not_found");

    String err;

    ErrorType(String err) {
        this.err = err;
    }
}
