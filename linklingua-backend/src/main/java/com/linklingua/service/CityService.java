package com.linklingua.service;

import com.linklingua.dto.CityDTO;
import com.linklingua.entity.City;
import com.linklingua.vo.CityVO;

import java.util.List;

/**
 * City business logic interface.
 *
 * @author LinkLingua
 */
public interface CityService {

    /**
     * Creates a city.
     *
     * @param dto the create request
     * @return the created city
     */
    City create(CityDTO dto);

    /**
     * Updates a city by id.
     *
     * @param id  the city id
     * @param dto the update request
     * @return the updated city
     */
    City update(Integer id, CityDTO dto);

    /**
     * Deletes a city by id.
     *
     * @param id the city id
     */
    void delete(Integer id);

    /**
     * Gets a city by id.
     *
     * @param id the city id
     * @return the city
     */
    City getById(Integer id);

    /**
     * Lists all cities.
     *
     * @return the list of cities
     */
    List<CityVO> list();
}
