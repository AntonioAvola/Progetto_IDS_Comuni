package com.unicam;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleResponseStatusException(ResponseStatusException ex) {
        String reason = ex.getReason();
        Map<String, String> errorResponse;

        try {
            // Tenta di interpretare il motivo come JSON
            errorResponse = new ObjectMapper().readValue(reason, new TypeReference<>() {});
        } catch (Exception e) {
            // Se fallisce, restituisci il motivo come messaggio di errore generico
            errorResponse = Map.of("message", reason);
        }

        return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
    }
}

