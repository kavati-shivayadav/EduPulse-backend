package com.edupulse.dto;

public record ApiResponse(boolean success, String message, Object data) {
}
