package com.linklingua.service.impl;

import com.linklingua.common.BusinessException;
import com.linklingua.common.ResultCode;
import com.linklingua.dto.UserDTO;
import com.linklingua.entity.User;
import com.linklingua.mapper.UserMapper;
import com.linklingua.service.UserService;
import com.linklingua.util.JwtUtil;
import com.linklingua.vo.LoginVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the user business logic.
 *
 * <p>Note: passwords are stored and compared as plain text to stay consistent with the seed
 * data. Hashing them (e.g. BCrypt) is strongly recommended for production.</p>
 *
 * @author LinkLingua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public LoginVO login(UserDTO dto) {
        User user = userMapper.selectByUsername(dto.getUsername());
        // Use the same message for "user not found" and "wrong password" to avoid leaking which one failed.
        if (user == null || !user.getPassword().equals(dto.getPassword())) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "Invalid username or password");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("userName", user.getUsername());
        String token = jwtUtil.createToken(claims);

        log.debug("User logged in, id={}", user.getId());
        return LoginVO.builder()
                .id(user.getId().longValue())
                .userName(user.getUsername())
                .token(token)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void signup(UserDTO dto) {
        if (userMapper.selectByUsername(dto.getUsername()) != null) {
            throw new BusinessException("Username already exists: " + dto.getUsername());
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setCreateTime(LocalDateTime.now());
        userMapper.insert(user);
        log.debug("User signed up, id={}", user.getId());
    }
}
