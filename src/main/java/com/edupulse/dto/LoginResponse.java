package com.edupulse.dto;

public record LoginResponse(String token, String type, UserSummaryDto user) {
}
