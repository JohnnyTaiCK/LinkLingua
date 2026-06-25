package com.linklingua.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.linklingua.common.BusinessException;
import com.linklingua.common.PageResult;
import com.linklingua.common.ResultCode;
import com.linklingua.dto.EventInfoDTO;
import com.linklingua.dto.EventPageQueryDTO;
import com.linklingua.entity.EventInfo;
import com.linklingua.mapper.CityMapper;
import com.linklingua.mapper.EventInfoMapper;
import com.linklingua.mapper.LanguageTagMapper;
import com.linklingua.mapper.UserMapper;
import com.linklingua.service.EventInfoService;
import com.linklingua.util.BeanConvertUtil;
import com.linklingua.vo.EventDetailVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Implementation of the event business logic.
 *
 * <p>Validates the referenced city, language and publisher before persisting, so foreign-key
 * relationships stay consistent.</p>
 *
 * @author LinkLingua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EventInfoServiceImpl implements EventInfoService {

    /** Default max participant count when not provided. */
    private static final int DEFAULT_MAX_PARTICIPANT = 50;

    /** Default event status (1 = Upcoming) when not provided. */
    private static final int DEFAULT_EVENT_STATUS = 1;  //1 upcoming, 2 ongoing, 3 completed

    /** Default page number when not provided. */
    private static final int DEFAULT_PAGE = 1;

    /** Default page size when not provided. */
    private static final int DEFAULT_PAGE_SIZE = 10;

    @Autowired
    private EventInfoMapper eventInfoMapper;

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private LanguageTagMapper languageTagMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(EventInfoDTO dto) {
        validateReferences(dto);

        EventInfo event = new EventInfo();
        BeanUtils.copyProperties(dto, event);
        // The id from the DTO must never be honoured on create.
        event.setId(null);
        if (event.getMaxParticipant() == null) {
            event.setMaxParticipant(DEFAULT_MAX_PARTICIPANT);
        }
        if (event.getEventStatus() == null) {
            event.setEventStatus(DEFAULT_EVENT_STATUS);
        }

        checkEventTime(event.getEventStartTime());

        LocalDateTime now = LocalDateTime.now();
        event.setCreateTime(now);
        event.setUpdateTime(now);
        // The publisher is treated as both creator and updater.
        event.setCreatedBy(dto.getPublishUserId());
        event.setUpdatedBy(dto.getPublishUserId());

        eventInfoMapper.insert(event);
        log.debug("Event created successfully, id={}", event.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(EventInfoDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED.getCode(), "Event id must not be null");
        }
        EventInfo existing = eventInfoMapper.selectById(dto.getId());
        if (existing == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "Event does not exist, id=" + dto.getId());
        }

        validateReferences(dto);
        checkEventTime(dto.getEventStartTime());

        EventInfo event = new EventInfo();
        BeanUtils.copyProperties(dto, event);

        if (event.getMaxParticipant() == null) {
            event.setMaxParticipant(existing.getMaxParticipant());
        }
        // Keep the existing status when the request does not provide a new one.
        if (event.getEventStatus() == null) {
            event.setEventStatus(existing.getEventStatus());
        }
        event.setUpdateTime(LocalDateTime.now());
        event.setUpdatedBy(dto.getPublishUserId());

        eventInfoMapper.updateById(event);
        log.debug("Event updated successfully, id={}", event.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED.getCode(), "ids must not be empty");
        }
        eventInfoMapper.deleteBatch(ids);
        log.debug("Deleted {} event(s): {}", ids.size(), ids);
    }

    @Override
    public EventDetailVO getById(Integer id) {
        EventDetailVO vo = eventInfoMapper.selectDetailById(id);
        if (vo == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "Event does not exist, id=" + id);
        }
        return vo;
    }

    @Override
    public PageResult page(EventPageQueryDTO query) {
        int pageNum = query.getPage() == null ? DEFAULT_PAGE : query.getPage();
        int pageSize = query.getPageSize() == null ? DEFAULT_PAGE_SIZE : query.getPageSize();

        if (query.getTimeRange() != null) {
            if (query.getTimeRange().equals("today")) {
                LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
                LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);
                query.setStartTime(start);
                query.setEndTime(end);
            }else if (query.getTimeRange().equals("week")) {
                LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
                LocalDateTime end = start.plusDays(7).with(LocalTime.MAX);
                query.setStartTime(start);
                query.setEndTime(end);
            }else if (query.getTimeRange().equals("month")) {
                LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
                LocalDateTime end = start.plusDays(30).with(LocalTime.MAX);
                query.setStartTime(start);
                query.setEndTime(end);
            }
        }

        PageHelper.startPage(pageNum, pageSize);
        Page<EventDetailVO> page = (Page<EventDetailVO>) eventInfoMapper.pageQuery(query);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * Validates that the referenced city, language tag and publisher all exist.
     */
    private void validateReferences(EventInfoDTO dto) {
        if (cityMapper.selectById(dto.getCityId()) == null) {
            throw new BusinessException("City does not exist, id=" + dto.getCityId());
        }
        if (languageTagMapper.selectById(dto.getLangId()) == null) {
            throw new BusinessException("Language tag does not exist, id=" + dto.getLangId());
        }
        if (userMapper.selectById(dto.getPublishUserId()) == null) {
            throw new BusinessException("Publisher user does not exist, id=" + dto.getPublishUserId());
        }
    }

    private void checkEventTime(LocalDateTime eventStartTime) {
        LocalDateTime minValidTime = LocalDateTime.now().plusHours(2).withSecond(0).withNano(0);
        if (eventStartTime != null && eventStartTime.isBefore(minValidTime)) {
            throw new BusinessException(400, "present time or past time is not allowed for event start time");
        }
    }
}
