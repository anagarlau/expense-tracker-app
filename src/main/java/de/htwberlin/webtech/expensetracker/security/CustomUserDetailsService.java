package de.htwberlin.webtech.expensetracker.security;

import de.htwberlin.webtech.expensetracker.persistence.entities.UserEntity;
import de.htwberlin.webtech.expensetracker.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Service
 /*Spring-provided service to validate user*/
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }



    //checks credentials against the database
     @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity existingUser = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("No Username for email " + email + " found."  ));
         //Arg 3 = empty arraylist because 0 roles defined
        return new org.springframework.security.core.userdetails.User(existingUser.getEmail(), existingUser.getPassword(), new ArrayList<>());
    }
}
