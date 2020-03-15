package com.triagechat.controller.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.triagechat.controller.mapper.ErrorMapper;
import com.triagechat.dto.response.ErrorDto;
import com.triagechat.service.validation.BadRequestException;
import com.triagechat.service.validation.InternalServerException;
import com.triagechat.service.validation.NotFoundException;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class ExceptionControllerAdvisor {

    private final ErrorMapper errorMapper;

    @ExceptionHandler({ MethodArgumentNotValidException.class, BadRequestException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @Order(value = 0)
    public ErrorDto badRequest(Exception ex) {
        return buildErrorResponse(ex);
    }

    @ExceptionHandler({ AccessDeniedException.class })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    @Order(value = 1)
    public ErrorDto unAuthorizedHandler(Exception ex) {
        return buildErrorResponse(ex);
    }

    @ExceptionHandler({ NotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @Order(value = 2)
    public ErrorDto notFoundHandler(Exception ex) {
        return buildErrorResponse(ex);
    }

    @ExceptionHandler({ InternalServerException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @Order(value = 3)
    public ErrorDto internalServerErrorHandler(Exception ex) {
        return buildErrorResponse(ex);
    }

    @ExceptionHandler({ Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @Order
    public ErrorDto defaultHandler(Exception ex) {
        return buildErrorResponse(ex);
    }

    private ErrorDto buildErrorResponse(Exception ex) {
        try {
            ErrorDto errorResponse;
            if (ex == null) {
                errorResponse = this.errorMapper.buildErrorForNullException();
            } else if (ex instanceof MethodArgumentNotValidException) {
                MethodArgumentNotValidException payloadConstraintException = (MethodArgumentNotValidException) ex;
                errorResponse = this.errorMapper.buildErrorForPayloadConstraintException(payloadConstraintException);
            } else {
                errorResponse = this.errorMapper.buildErrorForAnyOtherExceptionType(ex);
            }

            log.error("Error happened with errorResponse={}.", errorResponse, ex);
            return errorResponse;
        } catch (Exception e) {
            ErrorDto errorResponse = this.errorMapper.buildErrorForAnyOtherExceptionType(e);
            log.error("General error happened with errorResponse={}.", errorResponse, e);
            return errorResponse;
        }
    }

}
