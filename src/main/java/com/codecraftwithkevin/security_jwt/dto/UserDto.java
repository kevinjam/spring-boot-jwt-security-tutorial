package com.codecraftwithkevin.security_jwt.dto;

import java.util.List;

public record UserDto(String username, String password, String lastname, String firstname, List<String> roles) {
}
