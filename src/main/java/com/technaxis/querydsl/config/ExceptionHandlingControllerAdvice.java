package com.technaxis.querydsl.config;

import com.technaxis.querydsl.dto.Response;
import com.technaxis.querydsl.exceptions.BadRequestException;
import com.technaxis.querydsl.exceptions.ForbiddenException;
import com.technaxis.querydsl.exceptions.NotFoundException;
import com.technaxis.querydsl.exceptions.TooManyRequestsException;
import com.technaxis.querydsl.exceptions.ValidationCollectorException;
import com.technaxis.querydsl.exceptions.ValidationException;
import com.technaxis.querydsl.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice(annotations = RestController.class)
public class ExceptionHandlingControllerAdvice {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.debug("{}, cause: {}", exception.toString(), ExceptionUtils.getShortStackTrace(exception));
        return getValidationErrorResponse(exception.getLocalizedMessage(), exception.getBindingResult());
    }

    @ExceptionHandler({BindException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response validationExceptionHandler(BindException exception) {
        log.debug("{}, cause: {}", exception.toString(), ExceptionUtils.getShortStackTrace(exception));
        return getValidationErrorResponse(exception.getLocalizedMessage(), exception.getBindingResult());
    }

    private Response getValidationErrorResponse(String localizedMessage, BindingResult bindingResult) {
        return new Response<>(Response.Error.builder()
                .message(localizedMessage)
                .type(Response.Error.Type.VALIDATION_ERROR)
                .details(bindingResult
                        .getFieldErrors()
                        .stream()
                        .map(fieldError -> Response.Error.Detail.builder()
                                .message(fieldError.getDefaultMessage())
                                .type(Response.Error.Detail.Type.NOT_CORRECT)
                                .target(fieldError.getField())
                                .build())
                        .collect(Collectors.toList()))
                .build());
    }

    @ExceptionHandler(ValidationCollectorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Response validationCollectorException(ValidationCollectorException exception, HttpServletRequest request) {
        log.debug("{}, cause: {}", exception.toString(), ExceptionUtils.getShortStackTrace(exception));
        return new Response(Response.Error.builder()
                .message(exception.getLocalizedMessage())
                .type(Response.Error.Type.VALIDATION_ERROR)
                .details(exception
                        .getErrors()
                        .stream()
                        .map(errorInfo -> Response.Error.Detail.builder()
                                .target(errorInfo.getIncorrectValue())
                                .message(errorInfo.getMessage())
                                .type(Response.Error.Detail.Type.NOT_CORRECT)
                                .build())
                        .collect(Collectors.toList()))
                .build());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Response<Object> requestBodyException(HttpMessageNotReadableException exception, HttpServletRequest request) {
        log.warn(String.format("Failed to read %s request %s body", request.getMethod(), request.getRequestURI()), exception);
        return new Response<>(Response.Error.builder()
                .message("Invalid param")
                .type(Response.Error.Type.BAD_REQUEST)
                .build());
    }

    @ExceptionHandler({ForbiddenException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public Response<Object> forbiddenException(ForbiddenException exception, HttpServletRequest request) {
        log.debug("{}, cause: {}", exception.toString(), ExceptionUtils.getShortStackTrace(exception));
        return new Response<>(Response.Error.builder()
                .message(exception.getMessage())
                .type(Response.Error.Type.FORBIDDEN)
                .build());
    }

    @ExceptionHandler({TooManyRequestsException.class})
    public ResponseEntity<Response<Object>> tooManyRequestsException(TooManyRequestsException exception,
                                                                     HttpServletRequest request) {
        log.debug("{}, cause: {}", exception.toString(), ExceptionUtils.getShortStackTrace(exception));
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .header(HttpHeaders.RETRY_AFTER, exception.getRetryAfter().toString())
                .body(new Response<>(Response.Error.builder()
                        .type(Response.Error.Type.TOO_MANY_REQUESTS)
                        .build()));
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Response<Object> notFoundException(NotFoundException exception, HttpServletRequest request) {
        log.debug("{}, cause: {}", exception.toString(), ExceptionUtils.getShortStackTrace(exception));
        return new Response<>(Response.Error.builder()
                .message(exception.getMessage())
                .type(Response.Error.Type.NOT_FOUND)
                .build());
    }

    @ExceptionHandler({EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Response<Object> notFoundException(EntityNotFoundException exception, HttpServletRequest request) {
        log.debug("{}, cause: {}", exception.toString(), ExceptionUtils.getShortStackTrace(exception));
        return new Response<>(Response.Error.builder()
                .message(exception.getMessage())
                .type(Response.Error.Type.NOT_FOUND)
                .build());
    }

    @ExceptionHandler({AuthenticationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Response<Object> authException(AuthenticationException exception, HttpServletRequest request) {
        log.debug("{}, cause: {}", exception.toString(), ExceptionUtils.getShortStackTrace(exception));
        return new Response<>(Response.Error.builder()
                .message("Not authorized")
                .type(Response.Error.Type.NOT_AUTHORIZED)
                .build());
    }

    @ExceptionHandler({ValidationException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response validationException(ValidationException exception) {
        log.debug("{}, cause: {}", exception.toString(), ExceptionUtils.getShortStackTrace(exception));
        return new Response<>(Response.Error.builder()
                .message(exception.getMessage())
                .type(Response.Error.Type.VALIDATION_ERROR)
                .detail(Response.Error.Detail.builder()
                        .message(exception.getMessage())
                        .type(Response.Error.Detail.Type.NOT_CORRECT)
                        .target(exception.getField())
                        .build())
                .build());
    }

    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Response<Object> badRequestException(BadRequestException exception, HttpServletRequest request) {
        log.debug("{}, cause: {}", exception.toString(), ExceptionUtils.getShortStackTrace(exception));
        return new Response<>(Response.Error.builder()
                .message(exception.getMessage())
                .type(Response.Error.Type.BAD_REQUEST)
                .build());
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Response<Object> illegalArgumentException(IllegalArgumentException exception, HttpServletRequest request) {
        log.debug("{}, cause: {}", exception.toString(), ExceptionUtils.getShortStackTrace(exception));
        return new Response<>(Response.Error.builder()
                .message(exception.getMessage())
                .type(Response.Error.Type.BAD_REQUEST)
                .build());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Response serverException(Exception exception, HttpServletRequest request) {
        log.error(String.format("Failed to process %s request %s", request.getMethod(), request.getRequestURI()), exception);
        return new Response<>(Response.Error.builder()
                .message("Server failed to process request")
                .type(Response.Error.Type.REQUEST_FAILED)
                .build());
    }
}