package com.tpe.service;

import com.tpe.domain.Role;
import com.tpe.domain.User;
import com.tpe.domain.enums.UserRole;
import com.tpe.dto.RegisterRequest;
import com.tpe.exception.ConflictException;
import com.tpe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;


    @Autowired
    private PasswordEncoder passwordEncoder;

    public void save(RegisterRequest registerRequest) {

        if(userRepository.existsByUserName(registerRequest.getUserName())){
            throw new ConflictException("Username is already in use");
        }

        User newUser = new User();
        newUser.setUserName(registerRequest.getUserName());
        newUser.setFistName(registerRequest.getFirstName());
        newUser.setLastName(registerRequest.getLastName());

        //newUser.setPassword(registerRequest.getPassword());
        //we are setting encoded password not the plain text
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        //default role of new user will be "Student"
        Role role =roleService.getRoleByType(UserRole.ROLE_STUDENT);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        newUser.setRoles(roles);

        userRepository.save(newUser);
    }
}
