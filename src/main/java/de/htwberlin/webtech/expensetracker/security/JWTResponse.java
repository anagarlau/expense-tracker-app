package de.htwberlin.webtech.expensetracker.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JWTResponse {
     private String jwtToken;

}
