package de.htwberlin.webtech.expensetracker.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JWTResponse {
    private final String jwtToken;
}
