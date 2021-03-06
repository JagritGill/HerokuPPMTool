package com.Jagritgill.ppmtool.services;


import com.Jagritgill.ppmtool.domain.User;
import com.Jagritgill.ppmtool.exceptions.UsernameAlreadyExistsException;
import com.Jagritgill.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser (User newUser){

        try{
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            //Username has to be Unique(custom exception)
            newUser.setUsername(newUser.getUsername());
            //Make sure that password and confirmPassword match
            //We don't persist or show the confirmPassword
            return userRepository.save(newUser);

        }catch (Exception e){
            throw new UsernameAlreadyExistsException("Username '"+newUser.getUsername()+"' already exists");
        }


    }





}
