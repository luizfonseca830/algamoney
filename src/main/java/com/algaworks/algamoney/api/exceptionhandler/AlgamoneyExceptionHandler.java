package com.algaworks.algamoney.api.exceptionhandler;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class AlgamoneyExceptionHandler extends ResponseEntityExceptionHandler {

    private MessageSource messageSource;

    public AlgamoneyExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String mensagemUsuário = messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
        String mensagemDensenvolvedor = ex.getCause().toString();

        List<Erro> erros = Arrays.asList(new Erro(mensagemUsuário, mensagemDensenvolvedor));
        return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<Erro> erros = criarListaDeErros(ex.getBindingResult());
        return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
    }

    private List<Erro> criarListaDeErros(BindingResult bindingResult) {
        List<Erro> erros = new ArrayList<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String mensagemUsuário = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
            String mensagemDensenvolvedor = fieldError.toString();
            erros.add(new Erro(mensagemUsuário, mensagemDensenvolvedor));
        }
        return erros;
    }

    public static class Erro {
        private String mensagemUsuario;
        private String mensagemDensenvolvedor;

        public Erro(String mensagemUsuario, String mensagemDensenvolvedor) {
            this.mensagemUsuario = mensagemUsuario;
            this.mensagemDensenvolvedor = mensagemDensenvolvedor;
        }

        public String getMensagemUsuario() {
            return mensagemUsuario;
        }

        public void setMensagemUsuario(String mensagemUsuario) {
            this.mensagemUsuario = mensagemUsuario;
        }

        public String getMensagemDensenvolvedor() {
            return mensagemDensenvolvedor;
        }

        public void setMensagemDensenvolvedor(String mensagemDensenvolvedor) {
            this.mensagemDensenvolvedor = mensagemDensenvolvedor;
        }
    }
}
