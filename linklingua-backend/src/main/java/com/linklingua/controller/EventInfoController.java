package com.linklingua.controller;

import com.linklingua.common.PageResult;
import com.linklingua.common.Result;
import com.linklingua.dto.EventInfoDTO;
import com.linklingua.dto.EventPageQueryDTO;
import com.linklingua.entity.EventInfo;
import com.linklingua.service.EventInfoService;
import com.linklingua.vo.EventDetailVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Event management controller (core module).
 *
 * @author LinkLingua
 */
@Tag(name = "Event Management", description = "Language exchange event CRUD and query endpoints")
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventInfoController {

    private static final Logger log = LoggerFactory.getLogger(EventInfoController.class);

    @Autowired
    private EventInfoService eventInfoService;

    @Operation(summary = "Create event", description = "Create a new language exchange event")
    @PostMapping
    public Result create(@Valid @RequestBody EventInfoDTO dto) {
        log.info("Creating event with data: {}", dto);
        eventInfoService.create(dto);
        return Result.success();
    }

    @Operation(summary = "Update event", description = "Update an existing event by id")
    @PutMapping()
    public Result update(@Valid @RequestBody EventInfoDTO dto) {
        log.info("Updating event with data: {}", dto);
        eventInfoService.update(dto);
        return Result.success();
    }

    @Operation(summary = "Delete event", description = "Delete an event by id")
    @DeleteMapping
    public Result delete(
            @RequestParam List<Long> ids) {
        eventInfoService.deleteBatch(ids);
        return Result.success();
    }

    @Operation(summary = "Get event", description = "Get an event by id")
    @GetMapping("/{id}")
    public Result<EventDetailVO> getById(
            @Parameter(description = "Event id", example = "1") @PathVariable Integer id) {
        return Result.success(eventInfoService.getById(id));
    }

    @Operation(summary = "Get events", description = "get a list of events")
    @GetMapping("/page")
    public Result<PageResult> page(EventPageQueryDTO eventPageQueryDTO) {
        log.info("page request of events: {}", eventPageQueryDTO);
        return Result.success(eventInfoService.page(eventPageQueryDTO));
    }
}
