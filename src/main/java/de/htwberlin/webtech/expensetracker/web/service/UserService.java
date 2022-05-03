package de.htwberlin.webtech.expensetracker.web.service;

import de.htwberlin.webtech.expensetracker.exceptions.ResourceNotFound;
import de.htwberlin.webtech.expensetracker.persistence.entities.UserEntity;
import de.htwberlin.webtech.expensetracker.persistence.repository.UserRepository;
import de.htwberlin.webtech.expensetracker.web.model.User;
import de.htwberlin.webtech.expensetracker.web.model.UserManipulationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User createUser(UserManipulationRequest userReq){
        //TODO: handle duplicate emails
        //TODO: upon registration-> default wallet
        UserEntity userEntity = this.mapToUserEntity(userReq);
        UserEntity savedEntity = this.userRepository.save(userEntity);
        if(savedEntity != null && savedEntity.getUid()>0) return this.mapToUser(savedEntity);
        else return null;
    }

    public User findUserById(Long uid){
        Optional<UserEntity> userById = this.userRepository.findById(uid);
        return userById.map(userEntity -> mapToUser(userEntity)).orElseThrow(()-> new ResourceNotFound("User" + uid +  " not found "));

    }

    private UserEntity mapToUserEntity(UserManipulationRequest userReq){
        return new UserEntity(userReq.getEmail(), userReq.getPassword());
    }

    private User mapToUser(UserEntity user){
        return new User(user.getUid(), user.getEmail(), user.getPassword());
    }
}
