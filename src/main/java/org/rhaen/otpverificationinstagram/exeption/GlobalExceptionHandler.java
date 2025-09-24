package org.rhaen.otpverificationinstagram.exeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. DTO Validatsiya xatolari uchun (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Validation failed");
        responseBody.put("errors", errors);

        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    // 2. Biznes mantiq xatolari uchun (masalan, email/username bandligi)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalStateException ex) {
        log.warn("Biznes mantiq xatoligi yuz berdi: {}", ex.getMessage());
        return new ResponseEntity<>(createErrorResponse(ex.getMessage()), HttpStatus.CONFLICT); // 409 Conflict
    }

    // 3. Login jarayonidagi xatoliklar uchun
    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<Map<String, String>> handleAuthenticationException(Exception ex) {
        log.warn("Autentifikatsiya xatoligi: {}", ex.getMessage());
        return new ResponseEntity<>(createErrorResponse("Email yoki parol noto'g'ri."), HttpStatus.UNAUTHORIZED); // 401 Unauthorized
    }

    // 4. Boshqa kutilmagan Runtime xatoliklar uchun
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        log.error("Kutilmagan RuntimeException yuz berdi: ", ex);
        return new ResponseEntity<>(createErrorResponse("Kutilmagan xatolik yuz berdi. Iltimos, keyinroq urinib ko'ring."), HttpStatus.BAD_REQUEST); // 400 Bad Request
    }

    // 5. Eng umumiy, barcha qolgan xatoliklar uchun
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        log.error("Jiddiy ichki xatolik yuz berdi: ", ex);
        return new ResponseEntity<>(createErrorResponse("Serverda ichki xatolik yuz berdi."), HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
    }

    // Xato xabarlarini standart formatda yaratish uchun yordamchi metod
    private Map<String, String> createErrorResponse(String message) {
        return Map.of("error", message);
    }
}
