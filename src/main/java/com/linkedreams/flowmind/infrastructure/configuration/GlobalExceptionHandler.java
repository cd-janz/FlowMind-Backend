package com.linkedreams.flowmind.infrastructure.configuration;

import com.linkedreams.flowmind.infrastructure.models.Response;
import com.linkedreams.flowmind.infrastructure.utils.ResponseUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseUtil.error("Invalid argument provided", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Map<String, List<String>>>> handleValidationError(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errorsMap = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(
                                FieldError::getDefaultMessage,
                                Collectors.filtering(Objects::nonNull, Collectors.toList())
                        )
                ));
        return ResponseUtil.error(
                "Validation failed",
                errorsMap,
                "One or more fields failed validation checks",
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Response<Map<String, List<String>>>> handleWebFluxValidationError(WebExchangeBindException ex) {

        Map<String, List<String>> errorsMap = ex.getFieldErrors().stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(
                                FieldError::getDefaultMessage,
                                Collectors.filtering(Objects::nonNull, Collectors.toList())
                        )
                ));

        return ResponseUtil.error(
                "Validation failed (WebFlux)",
                errorsMap,
                "One or more fields failed validation checks for reactive request",
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<Response<Map<String, List<String>>>> handleServerWebInput(ServerWebInputException ex) {
        ex.getMostSpecificCause();
        String message = ex.getMostSpecificCause().getMessage();

        Pattern pattern = Pattern.compile("(?<=\\[\\\\?\")\\w+(?=\"])");
        Matcher matcher = pattern.matcher(message);

        String field = "unknown";
        if (matcher.find()) {
            field = matcher.group();
        }

        String cleanMsg;
        String lowerCaseMessage = message.toLowerCase();

        if (lowerCaseMessage.contains("missing") || lowerCaseMessage.contains("null")) {
            cleanMsg = "Field is required";
        } else if (message.contains("Cannot deserialize")) {
            cleanMsg = "Invalid data type";
        } else {
            cleanMsg = "Invalid value";
        }

        Map<String, List<String>> fieldErrors = Map.of(field, List.of(cleanMsg));

        return ResponseUtil.error(
                "Invalid request body",
                fieldErrors,
                "Input error on field '" + field + "': " + cleanMsg,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Response<Void>> handleUnreadableHttpMessage(HttpMessageNotReadableException ex) {
        ex.getMostSpecificCause();
        String detailMessage = ex.getMostSpecificCause().getMessage();

        return ResponseUtil.error(
                "Malformed request body",
                "The request body is invalid or has incorrect data types: " + detailMessage,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Response<Void>> handleSecurityException(SecurityException ex) {
        return ResponseUtil.error("Authentication error", ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<Response<Void>> handleAuthenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException ex) {
        return ResponseUtil.error("Authentication error", ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Response<Void>> handleAuthorizationDeniedException() {
        return ResponseUtil.error("Authorization denied", "You don't have permissions to access this resource", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Response<Void>> handleExpiredJwtException() {
        return ResponseUtil.error("Token expired", "The JWT token has expired or is invalid", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Response<Void>> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseUtil.error("Access denied", ex.getMessage(), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Response<Void>> handleDuplicateKeyException(DuplicateKeyException ex) {
        String rawMessage = ex.getMessage();
        String cleanMessage = "Duplicate entry detected";
        Pattern pattern = Pattern.compile("duplicate key value violates unique constraint\\s+\".+?\"");
        Matcher matcher = pattern.matcher(rawMessage);
        if (matcher.find()) {
            cleanMessage = matcher.group();
        }
        return ResponseUtil.error("Data conflict", cleanMessage, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Void>> handleGenericException(Exception ex) {
        logger.error("Unhandled exception", ex);
        return ResponseUtil.error("Unexpected internal error", "An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}