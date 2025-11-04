package com.saltaTech.user.application.controller;

import com.saltaTech.user.application.service.interfaces.UserService;
import com.saltaTech.user.domain.dto.request.UserCreateRequest;
import com.saltaTech.user.domain.dto.response.RegisteredUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    public UserController (UserService userService){
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity<RegisteredUser> createUser(
            @Valid @RequestBody UserCreateRequest request,
            HttpServletRequest httpRequest
    ) {
        RegisteredUser createdUser = userService.createUser(request);
        String baseUrl = httpRequest.getRequestURL().toString();
        URI newLocation = URI.create(baseUrl + "/" + createdUser.user().id());
        return ResponseEntity.created(newLocation).body(createdUser);
    }
}
