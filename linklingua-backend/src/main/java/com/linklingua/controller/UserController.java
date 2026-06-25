package com.linklingua.controller;

import com.linklingua.common.Result;
import com.linklingua.dto.UserDTO;
import com.linklingua.service.UserService;
import com.linklingua.vo.LoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User authentication controller (login / signup).
 *
 * @author LinkLingua
 */
@Tag(name = "User Module", description = "User login and signup endpoints")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "User login", description = "Authenticate the user and return a JWT token")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody UserDTO dto) {
        log.info("User login: {}", dto.getUsername());
        return Result.success("logged in successfully", userService.login(dto));
    }

    @Operation(summary = "User signup", description = "Register a new user account")
    @PostMapping("/signup")
    public Result<Void> signup(@Valid @RequestBody UserDTO dto) {
        log.info("User signup: {}", dto.getUsername());
        userService.signup(dto);
        return Result.success("successfully signed up!", null);
    }
}
