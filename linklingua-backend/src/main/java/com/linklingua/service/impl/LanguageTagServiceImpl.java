package com.linklingua.service.impl;

import com.linklingua.common.BusinessException;
import com.linklingua.common.ResultCode;
import com.linklingua.dto.LanguageTagDTO;
import com.linklingua.entity.LanguageTag;
import com.linklingua.mapper.LanguageTagMapper;
import com.linklingua.service.LanguageTagService;
import com.linklingua.util.BeanConvertUtil;
import com.linklingua.vo.LangVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of the language tag business logic.
 *
 * @author LinkLingua
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LanguageTagServiceImpl implements LanguageTagService {

    @Autowired
    private LanguageTagMapper languageTagMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LanguageTag create(LanguageTagDTO dto) {
        LanguageTag languageTag = BeanConvertUtil.convert(dto, LanguageTag::new);
        languageTagMapper.insert(languageTag);
        log.debug("Language tag created successfully, id={}", languageTag.getId());
        return languageTag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LanguageTag update(Integer id, LanguageTagDTO dto) {
        LanguageTag languageTag = getById(id);
        languageTag.setLangName(dto.getLangName());
        languageTagMapper.updateById(languageTag);
        return languageTag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        if (languageTagMapper.deleteById(id) == 0) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "Language tag does not exist, id=" + id);
        }
    }

    @Override
    public LanguageTag getById(Integer id) {
        LanguageTag languageTag = languageTagMapper.selectById(id);
        if (languageTag == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "Language tag does not exist, id=" + id);
        }
        return languageTag;
    }

    @Override
    public List<LangVO> list() {
        return languageTagMapper.selectAll().stream()
                .map(tag -> LangVO.builder()
                        .id(tag.getId())
                        .languageName(tag.getLangName())
                        .build())
                .toList();
    }
}
