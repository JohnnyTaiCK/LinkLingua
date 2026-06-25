package com.linklingua.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String userName;
    private String token;
}
