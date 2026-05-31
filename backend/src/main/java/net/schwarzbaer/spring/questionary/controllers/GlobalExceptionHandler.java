package net.schwarzbaer.spring.questionary.controllers;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import net.schwarzbaer.spring.questionary.models.errors.ErrorMessage;
import net.schwarzbaer.spring.questionary.models.errors.WrongDefinitionStructureException;
import tools.jackson.core.JacksonException;
import tools.jackson.core.exc.JacksonIOException;
import tools.jackson.core.exc.StreamReadException;
import tools.jackson.databind.DatabindException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler
{
    @ExceptionHandler({
        JacksonIOException.class,
        StreamReadException.class,
        DatabindException.class,
        JacksonException.class
    })
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ErrorMessage handleExceptionsFromJackson(Exception ex)
    {
        return createErrorMessageAndDoLog(ex);
    }

    @ExceptionHandler(WrongDefinitionStructureException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ErrorMessage handleException(WrongDefinitionStructureException ex)
    {
        return createErrorMessageAndDoLog(ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ErrorMessage handleException(IllegalArgumentException ex)
    {
        return createErrorMessageAndDoLog(ex);
    }

	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorMessage handleException(NoSuchElementException ex)
	{
		return createErrorMessageAndDoLog(ex);
	}

	private static ErrorMessage createErrorMessageAndDoLog(Exception ex)
	{
		String message = "%s: %s".formatted(ex.getClass().getSimpleName(), ex.getMessage());
		log.error(message);
		return new ErrorMessage(message);
	}
}
