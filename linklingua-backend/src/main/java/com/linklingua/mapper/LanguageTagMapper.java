package com.linklingua.mapper;

import com.linklingua.entity.LanguageTag;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Language tag Mapper interface (annotation-based SQL).
 *
 * @author LinkLingua
 */
@Mapper
public interface LanguageTagMapper {

    /**
     * Inserts a new language tag and writes back the generated id.
     */
    @Insert("INSERT INTO language_tag (lang_name) VALUES (#{langName})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(LanguageTag languageTag);

    /**
     * Updates a language tag by id.
     */
    @Update("UPDATE language_tag SET lang_name = #{langName} WHERE id = #{id}")
    int updateById(LanguageTag languageTag);

    /**
     * Deletes a language tag by id.
     */
    @Delete("DELETE FROM language_tag WHERE id = #{id}")
    int deleteById(Integer id);

    /**
     * Selects a language tag by id.
     */
    @Select("SELECT id, lang_name FROM language_tag WHERE id = #{id}")
    LanguageTag selectById(Integer id);

    /**
     * Selects all language tags.
     */
    @Select("SELECT id, lang_name FROM language_tag ORDER BY id")
    List<LanguageTag> selectAll();
}
