package com.coding24.badmintonsystem_1.exception;

import com.coding24.badmintonsystem_1.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "文件大小超过限制，请上传小于10MB的文件。");
        return "redirect:/equipment-rentals";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ApiResponse<Object> response = new ApiResponse<>(1, errors, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
