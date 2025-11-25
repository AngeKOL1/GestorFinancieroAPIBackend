package com.example.restapp.GestorFinanciero.exception;

import java.time.LocalDateTime;

public record CustomErrorRecord(
        LocalDateTime datetime,
        String message,
        String details
) {
}
