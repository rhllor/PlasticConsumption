package com.github.rhllor.pc.service.error;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ApiAdvice {
    
    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorResponse<String> employeeNotFoundHandler(NotFoundException ex) {
        List<String> lstError = new ArrayList<>();
        lstError.add(ex.getMessage());

        return new ErrorResponse<String>(lstError);
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse<ErrorModel> handleBindingErrors(MethodArgumentNotValidException ex) {

        List<ErrorModel> errorMessages = ex.getBindingResult().getFieldErrors().stream()
            .map(err -> new ErrorModel(err.getField(), err.getRejectedValue(), err.getDefaultMessage()))
            .distinct()
            .collect(Collectors.toList());

        return new ErrorResponse<ErrorModel>(errorMessages);
    }
}
