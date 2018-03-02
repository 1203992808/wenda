package com.example.dao;

import com.example.model.Comment;
import com.example.model.Feed;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.List;
@Mapper
@Component
public interface FeedDAO {

    String TABLE_NAME = " feed ";
    String INSERT_FIELDS = " type, created_date, user_id, data ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{type},#{createdDate},#{userId},#{data})"})
    int addFeed(Feed feed);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where id=#{id}"})
    Feed selectFeedById(@Param("id") int id);

    List<Feed> selectUserFeeds(@Param("maxId") int maxId,@Param("userIds")List<Integer> userIds,@Param("count")int count);


}
