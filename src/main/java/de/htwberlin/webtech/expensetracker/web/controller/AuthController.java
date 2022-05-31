package de.htwberlin.webtech.expensetracker.web.controller;

import de.htwberlin.webtech.expensetracker.security.CustomUserDetailsService;
import de.htwberlin.webtech.expensetracker.security.JWTResponse;
import de.htwberlin.webtech.expensetracker.security.JWTUtil;
import de.htwberlin.webtech.expensetracker.web.model.User;
import de.htwberlin.webtech.expensetracker.web.model.UserManipulationRequest;
import de.htwberlin.webtech.expensetracker.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
public class AuthController {
    private  UserService userService;
    private AuthenticationManager authenticationManager;
    private CustomUserDetailsService customUserDetailsService;
    private JWTUtil jwtUtil;


    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService, JWTUtil jwtUtil){
        this.userService = userService;
        this.authenticationManager=authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtil = jwtUtil;
    }






    @PostMapping (value = "/login")
    public ResponseEntity<JWTResponse> login(@RequestBody UserManipulationRequest loginRequest) throws Exception {
        /*private method */
        authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        /*generate token with user details from customuserdetailsservice*/
        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getEmail());
        final String token = jwtUtil.generateToken(userDetails);
        /*return token*/
        return ResponseEntity.ok().body(new JWTResponse(token));
    }



    @PostMapping("/register")
    public ResponseEntity<JWTResponse> saveUser(@RequestBody UserManipulationRequest userReq) throws Exception {
        User newUser = this.userService.createUser(userReq);
        if(newUser !=null){
        //  return ResponseEntity.ok(newUser);
            return this.login(userReq);
        }
        return ResponseEntity.badRequest().build();
    }

    /*helper method*/
    private void authenticate(String email, String password) throws Exception {
        try{
            /*authenticating the user*/
         authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                   email, password));
        }catch(DisabledException e){
            throw new Exception("User " + email + " disabled");
        }catch(BadCredentialsException ex){
            throw new Exception("Invalid credentials");
        }
    }
}
