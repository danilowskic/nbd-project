package com.danilowskic.nbd_project.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFound(TaskNotFoundException ex) {
        log.error(ex.getMessage());

        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", ex.getMessage());
        mav.addObject("status", HttpStatus.NOT_FOUND.value());
        mav.addObject("errorType", "Not Found");
        return mav;
    }

    @ExceptionHandler(ForbiddenActionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handleForbidden(ForbiddenActionException ex) {
        log.error(ex.getMessage());

        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", ex.getMessage());
        mav.addObject("status", HttpStatus.FORBIDDEN.value());
        mav.addObject("errorType", "Forbidden");
        return mav;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleGeneralError(Exception ex) {
        log.error(ex.getMessage());

        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", "Wystąpił nieoczekiwany błąd serwera");
        mav.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        mav.addObject("errorType", "Internal Server Error");
        return mav;
    }
}
