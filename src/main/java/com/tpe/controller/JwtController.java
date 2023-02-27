package com.tpe.controller;

import com.tpe.dto.LoginRequest;
import com.tpe.dto.RegisterRequest;
import com.tpe.security.JwtUtils;
import com.tpe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping
public class JwtController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    //********* Register Method ***********

    @PostMapping("/register")
    public ResponseEntity<String> registerUser( @Valid @RequestBody RegisterRequest registerRequest){
        userService.save(registerRequest);
        String message = "User has been registered successfully";
        return new ResponseEntity<>(message, HttpStatus.CREATED);

    }

    //********* Login Method ***********

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest){

        //authenticating user
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUserName(),
                loginRequest.getPassword()
        ));
        //if user is authenticated we need to create jwt
        String token = jwtUtils.createToken(authentication);
        return new ResponseEntity<>(token, HttpStatus.CREATED);
    }


}
