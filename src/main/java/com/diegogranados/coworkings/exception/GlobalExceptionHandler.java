package com.diegogranados.coworkings.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, Object>> handleValidationErrors(
                        MethodArgumentNotValidException ex,
                        HttpServletRequest request) {

                Map<String, String> fieldErrors = new HashMap<>();
                for (FieldError error : ex.getBindingResult().getFieldErrors()) {
                        fieldErrors.put(error.getField(), error.getDefaultMessage());
                }

                Map<String, Object> body = buildErrorBody(
                                HttpStatus.BAD_REQUEST,
                                "Error de validación en los campos enviados",
                                request.getRequestURI());
                body.put("errors", fieldErrors);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }

        @ExceptionHandler(EmailAlreadyExistsException.class)
        public ResponseEntity<Map<String, Object>> handleEmailAlreadyExists(
                        EmailAlreadyExistsException ex,
                        HttpServletRequest request) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(buildErrorBody(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI()));
        }

        @ExceptionHandler(SedeNotFoundException.class)
        public ResponseEntity<Map<String, Object>> handleSedeNotFound(
                        SedeNotFoundException ex,
                        HttpServletRequest request) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(buildErrorBody(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI()));
        }

        @ExceptionHandler(SedeNombreDuplicadoException.class)
        public ResponseEntity<Map<String, Object>> handleSedeNombreDuplicado(
                        SedeNombreDuplicadoException ex,
                        HttpServletRequest request) {

                return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(buildErrorBody(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI()));
        }

        @ExceptionHandler(SedeDireccionDuplicadaException.class)
        public ResponseEntity<Map<String, Object>> handleSedeDireccionDuplicada(
                        SedeDireccionDuplicadaException ex,
                        HttpServletRequest request) {

                return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(buildErrorBody(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI()));
        }

        @ExceptionHandler(OperadorNoEncontradoException.class)
        public ResponseEntity<Map<String, Object>> handleOperadorNoEncontrado(
                        OperadorNoEncontradoException ex,
                        HttpServletRequest request) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(buildErrorBody(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI()));
        }

        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<Map<String, Object>> handleBadCredentials(
                        BadCredentialsException ex,
                        HttpServletRequest request) {

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(buildErrorBody(HttpStatus.UNAUTHORIZED, "Credenciales inválidas",
                                                request.getRequestURI()));
        }

        @ExceptionHandler(UsernameNotFoundException.class)
        public ResponseEntity<Map<String, Object>> handleUserNotFound(
                        UsernameNotFoundException ex,
                        HttpServletRequest request) {

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(buildErrorBody(HttpStatus.UNAUTHORIZED, ex.getMessage(),
                                                request.getRequestURI()));
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<Map<String, Object>> handleAccessDenied(
                        AccessDeniedException ex,
                        HttpServletRequest request) {

                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body(buildErrorBody(HttpStatus.FORBIDDEN,
                                                "No tienes permisos para realizar esta acción",
                                                request.getRequestURI()));
        }

        @ExceptionHandler(PersonaConIngresoActivoException.class)
        public ResponseEntity<Map<String, Object>> handlePersonaConIngresoActivo(
                        PersonaConIngresoActivoException ex,
                        HttpServletRequest request) {

                return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(buildErrorBody(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI()));
        }

        @ExceptionHandler(SedeCapacidadExcedidaException.class)
        public ResponseEntity<Map<String, Object>> handleSedeCapacidadExcedida(
                        SedeCapacidadExcedidaException ex,
                        HttpServletRequest request) {

                return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(buildErrorBody(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI()));
        }

        @ExceptionHandler(IngresoActivoNoEncontradoException.class)
        public ResponseEntity<Map<String, Object>> handleIngresoActivoNoEncontrado(
                        IngresoActivoNoEncontradoException ex,
                        HttpServletRequest request) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(buildErrorBody(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI()));
        }

        @ExceptionHandler(OperadorNoAsignadoASedeException.class)
        public ResponseEntity<Map<String, Object>> handleOperadorNoAsignadoASede(
                        OperadorNoAsignadoASedeException ex,
                        HttpServletRequest request) {

                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body(buildErrorBody(HttpStatus.FORBIDDEN, ex.getMessage(), request.getRequestURI()));
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<Map<String, Object>> handleIllegalArgument(
                        IllegalArgumentException ex,
                        HttpServletRequest request) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(buildErrorBody(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI()));
        }

        @ExceptionHandler(IllegalStateException.class)
        public ResponseEntity<Map<String, Object>> handleIllegalState(
                        IllegalStateException ex,
                        HttpServletRequest request) {

                return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(buildErrorBody(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI()));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<Map<String, Object>> handleGenericException(
                        Exception ex,
                        HttpServletRequest request) {

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(buildErrorBody(HttpStatus.INTERNAL_SERVER_ERROR,
                                                "Error interno del servidor", request.getRequestURI()));
        }

        private Map<String, Object> buildErrorBody(HttpStatus status, String message, String path) {
                Map<String, Object> body = new HashMap<>();
                body.put("timestamp", LocalDateTime.now().toString());
                body.put("status", status.value());
                body.put("error", status.getReasonPhrase());
                body.put("message", message);
                body.put("path", path);
                return body;
        }
}
