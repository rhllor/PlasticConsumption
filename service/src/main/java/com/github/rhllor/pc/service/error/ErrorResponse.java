package com.github.rhllor.pc.service.error;

import java.util.List;

public class ErrorResponse<T> {
    private List<T> errorMessage;

    public ErrorResponse(List<T> errorModel) {
        this.errorMessage = errorModel;
    }

    public List<T> getErrorMessage() {
        return this.errorMessage;
    }
}
