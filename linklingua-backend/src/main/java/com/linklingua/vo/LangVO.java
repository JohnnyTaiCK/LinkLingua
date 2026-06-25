package com.linklingua.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LangVO {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /** Language name (matches the {@code languageName} field in the API contract). */
    private String languageName;
}
