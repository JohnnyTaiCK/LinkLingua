package com.linklingua.service;

import com.linklingua.dto.LanguageTagDTO;
import com.linklingua.entity.LanguageTag;
import com.linklingua.vo.LangVO;

import java.util.List;

/**
 * Language tag business logic interface.
 *
 * @author LinkLingua
 */
public interface LanguageTagService {

    /**
     * Creates a language tag.
     */
    LanguageTag create(LanguageTagDTO dto);

    /**
     * Updates a language tag by id.
     */
    LanguageTag update(Integer id, LanguageTagDTO dto);

    /**
     * Deletes a language tag by id.
     */
    void delete(Integer id);

    /**
     * Gets a language tag by id.
     */
    LanguageTag getById(Integer id);

    /**
     * Lists all language tags.
     */
    List<LangVO> list();
}
