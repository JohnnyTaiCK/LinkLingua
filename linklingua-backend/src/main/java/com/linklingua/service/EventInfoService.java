package com.linklingua.service;

import com.linklingua.common.PageResult;
import com.linklingua.dto.EventInfoDTO;
import com.linklingua.dto.EventPageQueryDTO;
import com.linklingua.entity.EventInfo;
import com.linklingua.vo.EventDetailVO;

import java.util.List;

/**
 * Event business logic interface.
 *
 * @author LinkLingua
 */
public interface EventInfoService {

    /**
     * Creates an event.
     *
     * @param dto the create request
     * @return the created event
     */
    void create(EventInfoDTO dto);

    /**
     * Updates an event by id.
     *
     * @param dto the update request
     * @return the updated event
     */
    void update(EventInfoDTO dto);

    /**
     * Deletes an event by id.
     *
     * @param ids the event ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * Gets an event by id.
     *
     * @param id the event id
     * @return the event
     */
    EventDetailVO getById(Integer id);

    /**
     * Lists events filtered by optional city, language and status.
     *
     * @return the matching events
     */
    PageResult page(EventPageQueryDTO eventPageQueryDTO);
}
