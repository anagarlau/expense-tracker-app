package de.htwberlin.webtech.expensetracker.web.controller;

import de.htwberlin.webtech.expensetracker.web.model.User;
import de.htwberlin.webtech.expensetracker.web.model.UserManipulationRequest;
import de.htwberlin.webtech.expensetracker.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class AuthController {
    private  UserService userService;
    private AuthenticationManager authenticationManager;


    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager){
        this.userService = userService;
        this.authenticationManager=authenticationManager;

    }






    @RequestMapping (value = "/login", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH})
    public ResponseEntity<Void> login(@RequestBody UserManipulationRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<User> saveUser(@RequestBody UserManipulationRequest userReq) {
        User newUser = this.userService.createUser(userReq);
        if(newUser !=null){
          return ResponseEntity.ok(newUser);
        }
        return ResponseEntity.badRequest().build();
    }
}
