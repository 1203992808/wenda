package com.example.dao;

import com.example.model.Comment;
import com.example.model.Message;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface MessageDAO {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, content,has_read, created_date, conversation_id ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{fromId},#{toId},#{content},#{hasRead},#{createdDate},#{conversationId})"})
    int addMessage(Message message);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where conversation_id=#{conversationId} order by created_date desc limit #{offset},#{limit}"})
    List<Message> selectConversationDetail(@Param("conversationId") String conversationId
    ,@Param("offset")int offset,@Param("limit")int limit);

    @Select({"select",INSERT_FIELDS,",count(id) as id from (select * from  ",TABLE_NAME," where from_id=#{userId} or to_id=#{userId} order by id desc)tt group by conversation_id order by created_date desc limit #{offset},#{limit}"})

  // @Select({"select ", INSERT_FIELDS, " ,count(id) as id from ( select * from ", TABLE_NAME, " where from_id=#{userId} or to_id=#{userId} order by id desc) tt group by conversation_id  order by created_date desc limit #{offset}, #{limit}"})
    List<Message> selectConversationList(@Param("userId") int conversationId
            ,@Param("offset")int offset,@Param("limit")int limit);

   @Select({"select count(id) from ",TABLE_NAME,"where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}"})
   int getConversationUnreadCount(@Param("userId") int userId,@Param("conversationId")String conversationId);
   @Update({"update ",TABLE_NAME,"set has_read=1 where conversation_id=#{conversationId}"})
   void setConversationHasread(@Param("conversationId")String conversationId);



}
