package de.htwberlin.webtech.expensetracker.web.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserManipulationRequest {

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Please provide a valid mail address")
    private String email;

    @NotBlank(message="Password must not be blank")
    @Size(min=3, message="Password must be at least 3 characters long")
    private String password;
}
