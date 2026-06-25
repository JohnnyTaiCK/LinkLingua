package com.linklingua.controller;

import com.linklingua.common.Result;
import com.linklingua.dto.CityDTO;
import com.linklingua.entity.City;
import com.linklingua.service.CityService;
import com.linklingua.vo.CityVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * City management controller.
 *
 * @author LinkLingua
 */
@Tag(name = "City Management", description = "City dictionary CRUD endpoints")
@RestController
@RequestMapping("/api/city")
@RequiredArgsConstructor
public class CityController {

    @Autowired
    private CityService cityService;

    @Operation(summary = "List cities", description = "List all cities")
    @GetMapping
    public Result<List<CityVO>> list() {
        List<CityVO> cityList = cityService.list();
        return Result.success(cityList);
    }
}
