package com.linklingua.mapper;

import com.linklingua.dto.EventPageQueryDTO;
import com.linklingua.entity.EventInfo;
import com.linklingua.vo.EventDetailVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Event Mapper interface (annotation-based SQL).
 *
 * @author LinkLingua
 */
@Mapper
public interface EventInfoMapper {

    /**
     * Inserts a new event and writes back the generated id.
     */
    @Insert("""
            INSERT INTO event_info
                (title, description, location, event_start_time, max_participant,
                 city_id, lang_id, publish_user_id, event_status, image,
                 create_time, update_time, created_by, updated_by)
            VALUES
                (#{title}, #{description}, #{location}, #{eventStartTime}, #{maxParticipant},
                 #{cityId}, #{langId}, #{publishUserId}, #{eventStatus}, #{image},
                 #{createTime}, #{updateTime}, #{createdBy}, #{updatedBy})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(EventInfo eventInfo);

    /**
     * Updates an event by id (audit fields create_time / created_by are not modified).
     */
    @Update("""
            UPDATE event_info SET
                title = #{title},
                description = #{description},
                location = #{location},
                event_start_time = #{eventStartTime},
                max_participant = #{maxParticipant},
                city_id = #{cityId},
                lang_id = #{langId},
                publish_user_id = #{publishUserId},
                event_status = #{eventStatus},
                image = #{image},
                update_time = #{updateTime},
                updated_by = #{updatedBy}
            WHERE id = #{id}
            """)
    int updateById(EventInfo eventInfo);

    /**
     * Deletes a batch of events by id.
     */
    @Delete("""
            <script>
            DELETE FROM event_info WHERE id IN
            <foreach collection="ids" item="id" open="(" separator="," close=")">
                #{id}
            </foreach>
            </script>
            """)
    int deleteBatch(@Param("ids") List<Long> ids);

    /**
     * Selects a raw event entity by id (used for existence checks).
     */
    @Select("SELECT * FROM event_info WHERE id = #{id}")
    EventInfo selectById(Integer id);

    /**
     * Selects an event by id joined with the city and language dictionaries,
     * mapped straight to {@link EventDetailVO} (includes cityName / langName).
     */
    @Select("""
            SELECT e.id, e.title, e.description, e.location, e.event_start_time,
                   e.max_participant, e.city_id, c.city_name, e.lang_id, l.lang_name,
                   e.publish_user_id, e.event_status, e.image
            FROM event_info e
            LEFT JOIN city c ON e.city_id = c.id
            LEFT JOIN language_tag l ON e.lang_id = l.id
            WHERE e.id = #{id}
            """)
    EventDetailVO selectDetailById(Integer id);

    /**
     * Paginated event search joined with the city and language dictionaries.
     *
     * <p>Pagination is applied by PageHelper, so no LIMIT clause is present here.
     * Optional filters: city name, language name and a minimum start time.</p>
     */
    @Select("""
            <script>
            SELECT e.id, e.title, e.description, e.location, e.event_start_time,
                   e.max_participant, e.city_id, c.city_name, e.lang_id, l.lang_name,
                   e.publish_user_id, e.event_status, e.image
            FROM event_info e
            LEFT JOIN city c ON e.city_id = c.id
            LEFT JOIN language_tag l ON e.lang_id = l.id
            <where>
                <if test="keyword != null and keyword != ''"> AND e.title LIKE CONCAT('%', #{keyword}, '%') </if>
                <if test="city != null and city != ''"> AND c.city_name = #{city} </if>
                <if test="language != null and language != ''"> AND l.lang_name = #{language} </if>
                <if test="timeRange != null and timeRange != ''"> AND e.event_start_time &gt;= #{startTime} 
                AND e.event_start_time &lt;= #{endTime} </if>
            </where>
            ORDER BY e.event_start_time DESC
            </script>
            """)
    List<EventDetailVO> pageQuery(EventPageQueryDTO query);
}
