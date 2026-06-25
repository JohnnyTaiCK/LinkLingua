package com.linklingua.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityVO {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String cityName;
}
