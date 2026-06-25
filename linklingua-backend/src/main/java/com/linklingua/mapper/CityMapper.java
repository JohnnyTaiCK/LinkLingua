package com.linklingua.mapper;

import com.linklingua.entity.City;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * City Mapper interface (annotation-based SQL).
 *
 * <p>Underscore-to-camelCase mapping is enabled globally, so result columns are mapped
 * automatically (e.g. {@code city_name} -> {@code cityName}).</p>
 *
 * @author LinkLingua
 */
@Mapper
public interface CityMapper {

    /**
     * Inserts a new city and writes back the generated id.
     */
    @Insert("INSERT INTO city (city_name) VALUES (#{cityName})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(City city);

    /**
     * Updates a city by id.
     */
    @Update("UPDATE city SET city_name = #{cityName} WHERE id = #{id}")
    int updateById(City city);

    /**
     * Deletes a city by id.
     */
    @Delete("DELETE FROM city WHERE id = #{id}")
    int deleteById(Integer id);

    /**
     * Selects a city by id.
     */
    @Select("SELECT id, city_name FROM city WHERE id = #{id}")
    City selectById(Integer id);

    /**
     * Selects all cities.
     */
    @Select("SELECT id, city_name FROM city ORDER BY id")
    List<City> selectAll();
}
