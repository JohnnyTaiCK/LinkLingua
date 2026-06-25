package com.linklingua.controller;

import com.linklingua.common.Result;
import com.linklingua.service.LanguageTagService;
import com.linklingua.vo.LangVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Language tag management controller.
 *
 * @author LinkLingua
 */
@Tag(name = "Language Management", description = "Language dictionary endpoints")
@RestController
@RequestMapping("/api/language")
@RequiredArgsConstructor
public class LanguageTagController {

    @Autowired
    private LanguageTagService languageTagService;

    @Operation(summary = "List languages", description = "Get the list of available languages")
    @GetMapping("/list")
    public Result<List<LangVO>> list() {
        return Result.success(languageTagService.list());
    }
}
