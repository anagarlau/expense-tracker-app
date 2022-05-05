package de.htwberlin.webtech.expensetracker.web.service;

import de.htwberlin.webtech.expensetracker.exceptions.ItemAlreadyExists;
import de.htwberlin.webtech.expensetracker.exceptions.ResourceNotFound;
import de.htwberlin.webtech.expensetracker.persistence.entities.UserEntity;
import de.htwberlin.webtech.expensetracker.persistence.repository.UserRepository;
import de.htwberlin.webtech.expensetracker.web.model.User;
import de.htwberlin.webtech.expensetracker.web.model.UserManipulationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder encoder;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder=encoder;
    }




    public User createUser(UserManipulationRequest userReq){
        //TODO: handle duplicate emails ✔
        //TODO: upon registration-> default wallet❓
        if(userRepository.existsByEmail(userReq.getEmail())){
            throw new ItemAlreadyExists("There is already an account under " + userReq.getEmail());
        }
        UserEntity userEntity = this.mapToUserEntity(userReq);
        userEntity.setPassword(encoder.encode(userEntity.getPassword()));
        UserEntity savedEntity = this.userRepository.save(userEntity);
        if(savedEntity != null && savedEntity.getUid()>0) return new User(savedEntity.getUid(), savedEntity.getEmail());
        else return null;
    }

    /*get currently logged-in user*/
    public User getLoggedInUser(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found for email " + email));
        return mapToUser(userEntity);
    }

    public UserEntity getLoggedInUserEntity(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found for email " + email));
        return userEntity;
    }

//    public User findUserById(Long uid){
//        Optional<UserEntity> userById = this.userRepository.findById(uid);
//        return userById.map(userEntity -> mapToUser(userEntity)).orElseThrow(()-> new ResourceNotFound("User" + uid +  " not found "));
//
//    }

    private UserEntity mapToUserEntity(UserManipulationRequest userReq){
        return new UserEntity(userReq.getEmail(), userReq.getPassword());
    }

    private User mapToUser(UserEntity user){
        return new User(user.getUid(), user.getEmail());
    }
}
