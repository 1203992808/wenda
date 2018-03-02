package com.example.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.example.async.EventHandler;
import com.example.async.EventModel;
import com.example.async.EventType;
import com.example.model.*;
import com.example.service.*;
import com.example.util.JedisAdapter;
import com.example.util.RedisKeyUtil;
import com.example.util.WwwUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FeedHandler implements EventHandler {

    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    @Autowired
    FeedService feedService;
    @Autowired
    FollowService followService;
    @Autowired
    JedisAdapter jedisAdapter;

    private String buildFeedData(EventModel eventModel){
        Map<String,String> map=new HashMap<>();
        User user=userService.getUser(eventModel.getActorId());
        if(user==null){
            return null;
        }
        map.put("userName",user.getName());
        map.put("userId",String.valueOf(user.getId()));
        map.put("userHead",user.getHeadUrl());
        if(eventModel.getType()==EventType.COMMENT||(eventModel.getType()==EventType.FOLLOW&&eventModel.getEntityType()==EntityType.ENTITY_QUESTION)){
            Question question=questionService.getQuestionById(eventModel.getEntityId());
            if(question==null){
                return null;
            }
            map.put("questionId",String.valueOf(question.getId()));
            map.put("questionTitle",question.getTitle());
            return JSONObject.toJSONString(map);

        }
        return null;

    }
    @Override
    public void doHandle(EventModel eventModel) {

        Feed feed=new Feed();
        eventModel.setActorId(eventModel.getActorId());
        feed.setUserId(eventModel.getActorId());
        feed.setCreatedDate(new Date());
        feed.setType(eventModel.getType().getValue());
        feed.setData(buildFeedData(eventModel));
        if(feed.getData()==null){
            return ;
        }
        feedService.addFeed(feed);
        List<Integer> followers=followService.getFollowers(EntityType.ENTITY_USER,eventModel.getActorId(),Integer.MAX_VALUE);
        for (int follower:followers
             ) {
            String timelineKey=RedisKeyUtil.getTimeLineKey(follower);
            jedisAdapter.lpush(timelineKey,String.valueOf(feed.getId()));
        }






    }


    @Override
    public List<EventType> getSupportEventTypes() {

        return Arrays.asList(new EventType[]{EventType.FOLLOW,EventType.COMMENT});
    }
}
