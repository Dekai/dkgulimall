package com.dk.gulimall.product.exception;

import com.dk.common.exception.BizCodeEnume;
import com.dk.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice(basePackages = "com.dk.gulimall.product.controller")
public class GuliMallExceptionControllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e) {
        log.error("Validation error{}, exception type{}", e.getMessage(), e.getClass());
        BindingResult bindingResult = e.getBindingResult();

        Map<String, String> errorMap = new HashMap<>();

        bindingResult.getFieldErrors().forEach(fieldError -> {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return R.error(BizCodeEnume.VALID_EXCEPTION.getCode(), BizCodeEnume.VALID_EXCEPTION.getMsg()).put("data", errorMap);
    }

    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable) {
        log.error("System Error",throwable);
        return R.error(BizCodeEnume.UNKNOWN_EXCEPTION.getCode(), BizCodeEnume.UNKNOWN_EXCEPTION.getMsg());
    }
}
