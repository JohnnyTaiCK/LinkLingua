package com.linklingua.service.impl;

import com.linklingua.common.BusinessException;
import com.linklingua.common.ResultCode;
import com.linklingua.dto.CityDTO;
import com.linklingua.entity.City;
import com.linklingua.mapper.CityMapper;
import com.linklingua.service.CityService;
import com.linklingua.util.BeanConvertUtil;
import com.linklingua.vo.CityVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of the city business logic.
 *
 * @author LinkLingua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    @Autowired
    private CityMapper cityMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public City create(CityDTO dto) {
        City city = BeanConvertUtil.convert(dto, City::new);
        cityMapper.insert(city);
        log.debug("City created successfully, id={}", city.getId());
        return city;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public City update(Integer id, CityDTO dto) {
        City city = getById(id);
        city.setCityName(dto.getCityName());
        cityMapper.updateById(city);
        return city;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        if (cityMapper.deleteById(id) == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "City does not exist, id=" + id);
        }
    }

    @Override
    public City getById(Integer id) {
        City city = cityMapper.selectById(id);
        if (city == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "City does not exist, id=" + id);
        }
        return city;
    }

    @Override
    public List<CityVO> list() {
        return cityMapper.selectAll().stream()
                .map(city -> CityVO.builder()
                        .id(city.getId())
                        .cityName(city.getCityName())
                        .build())
                .toList();
    }
}
