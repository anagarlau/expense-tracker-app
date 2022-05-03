package de.htwberlin.webtech.expensetracker.web.controller;

import de.htwberlin.webtech.expensetracker.web.model.User;
import de.htwberlin.webtech.expensetracker.web.model.UserManipulationRequest;
import de.htwberlin.webtech.expensetracker.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> saveUser(@RequestBody UserManipulationRequest userReq) throws URISyntaxException {
        User newUser = this.userService.createUser(userReq);
        if(newUser !=null){
            URI uri = new URI("/api/v1/users/" + newUser.getUid());
            return ResponseEntity.created(uri).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/users/{uid}")
    public ResponseEntity<User> fetchUserById(@PathVariable Long uid){
        User userById = this.userService.findUserById(uid);
        return (userById != null) ?  ResponseEntity.ok(userById) : ResponseEntity.notFound().build();
    }
}
