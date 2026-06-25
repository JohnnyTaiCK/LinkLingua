package com.linklingua.service;

import com.linklingua.dto.UserDTO;
import com.linklingua.vo.LoginVO;

/**
 * User business logic interface.
 *
 * @author LinkLingua
 */
public interface UserService {

    /**
     * Authenticates a user and issues a login token.
     *
     * @param dto the login credentials (username + password)
     * @return the logged-in user info plus a JWT token
     */
    LoginVO login(UserDTO dto);

    /**
     * Registers a new user account.
     *
     * @param dto the signup request (username + password)
     */
    void signup(UserDTO dto);
}
