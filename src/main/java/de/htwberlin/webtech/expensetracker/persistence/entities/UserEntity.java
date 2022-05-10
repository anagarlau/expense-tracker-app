package de.htwberlin.webtech.expensetracker.persistence.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name="users")

public class UserEntity extends BaseEntity{



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(nullable = false)
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Please provide a valid mail address")
    private String email;

    @Column(nullable = false)
    @NotBlank(message="Password must not be blank")
    @Size(min=3, message="Password must be at least 3 characters long")
    private String password;

    public UserEntity(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
