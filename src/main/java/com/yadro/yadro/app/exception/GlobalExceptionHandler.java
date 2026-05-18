package com.yadro.yadro.app.exception;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PersonNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handlePersonNotFound(PersonNotFoundException ex) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("status", 404);
        mav.addObject("message", ex.getMessage());
        mav.addObject("details", "Пользователь с таким ID не найден в базе данных.");
        return mav;
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleEmptyResult(EmptyResultDataAccessException ex) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("status", 404);
        mav.addObject("message", "Запись не найдена");
        mav.addObject("details", "В базе данных нет записи с таким идентификатором.");
        return mav;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("status", 400);
        mav.addObject("message", "Неверный формат параметра");
        mav.addObject("details", "Ожидался числовой идентификатор, получено: " + ex.getValue());
        return mav;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleIllegalArgument(IllegalArgumentException ex) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("status", 400);
        mav.addObject("message", "Некорректный запрос");
        mav.addObject("details", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleGenericException(Exception ex) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("status", 500);
        mav.addObject("message", "Внутренняя ошибка сервера");
        mav.addObject("details", ex.getMessage());
        ex.printStackTrace();
        return mav;
    }
}