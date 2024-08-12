package com.codecraftwithkevin.security_jwt.service;

import com.codecraftwithkevin.security_jwt.dto.UserDto;
import com.codecraftwithkevin.security_jwt.entity.Role;
import com.codecraftwithkevin.security_jwt.entity.User;
import com.codecraftwithkevin.security_jwt.repository.UserRepository;
import com.codecraftwithkevin.security_jwt.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User register(UserDto userDto) {
        List<String> roles = userDto.roles();
        if (roles == null || roles.isEmpty()) {
            roles = List.of(Role.USER.name());
        }
        return userRepository.save(mapToUser(userDto, roles, passwordEncoder));
    }

    private static User mapToUser(UserDto userDto, List<String> roles, PasswordEncoder passwordEncoder) {
        return User.builder()
                .username(userDto.username())
                .password(passwordEncoder.encode(userDto.password()))
                .firstname(userDto.firstname())
                .lastname(userDto.lastname())
                .roles(roles)
                .build();
    }

    public UserResponse authentication(UserDto userDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.username(), userDto.password()));
        String token = jwtService.generateToken(userDto.username());
        return new UserResponse(token);
    }
}
