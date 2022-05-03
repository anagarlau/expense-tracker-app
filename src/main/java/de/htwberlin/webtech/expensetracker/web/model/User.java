package de.htwberlin.webtech.expensetracker.web.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long uid;

    private String email;



}
