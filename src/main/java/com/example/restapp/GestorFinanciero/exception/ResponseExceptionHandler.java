package com.example.restapp.GestorFinanciero.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;

@RestControllerAdvice
public class ResponseExceptionHandler {
    public static final String tipeE = "errorType";


    @ExceptionHandler(ModelNotFoundException.class)
    public ProblemDetail handleModelNotFoundException(ModelNotFoundException ex, WebRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Model Not Found");
        pd.setType(URI.create(request.getDescription(false)));
        pd.setProperty(tipeE, "ModelNotFound");
        return pd;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        pd.setTitle("Bad Request");
        pd.setType(URI.create(request.getDescription(false)));
        pd.setProperty(tipeE, "InvalidArgument");
        return pd;
    }

    @ExceptionHandler(ArithmeticException.class)
    public ProblemDetail handleArithmeticException(ArithmeticException ex, WebRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_ACCEPTABLE, ex.getMessage());
        pd.setTitle("Arithmetic Error");
        pd.setType(URI.create(request.getDescription(false)));
        pd.setProperty(tipeE, "MathError");
        return pd;
    }


    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception ex, WebRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        pd.setTitle("Internal Server Error");
        pd.setType(URI.create(request.getDescription(false)));
        pd.setProperty(tipeE, "UnexpectedError");
        return pd;
    }
}
