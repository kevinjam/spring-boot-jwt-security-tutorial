package com.codecraftwithkevin.security_jwt.controller;

import com.codecraftwithkevin.security_jwt.dto.UserDto;
import com.codecraftwithkevin.security_jwt.entity.User;
import com.codecraftwithkevin.security_jwt.response.UserResponse;
import com.codecraftwithkevin.security_jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.accepted().body(userService.findAllUsers());
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserDto userDto){
        return ResponseEntity.ok().body(userService.register(userDto));
    }

    @PostMapping("/authentication")
    public ResponseEntity<UserResponse> authentication(@RequestBody UserDto userDto){
        return ResponseEntity.ok().body(userService.authentication(userDto));
    }
}
