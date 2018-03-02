package com.example.controller;

import com.example.model.EntityType;
import com.example.model.Feed;
import com.example.model.HostHolder;
import com.example.service.FeedService;
import com.example.service.FollowService;
import com.example.util.JedisAdapter;
import com.example.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FeedController {
    @Autowired
    HostHolder hostHolder;
    @Autowired
    FollowService followService;
    @Autowired
    FeedService feedService;
    @Autowired
    JedisAdapter jedisAdapter;

    @RequestMapping(path = {"/pullfeeds"}, method ={RequestMethod.POST,RequestMethod.GET})
    private String  getPullFeeds(Model model){
        int localUserId=hostHolder.getUser()==null?0:hostHolder.getUser().getId();
        List<Integer> userIds=new ArrayList<>();
        if(localUserId!=0){
            userIds=followService.getFollowees(localUserId, EntityType.ENTITY_USER,Integer.MAX_VALUE);
        }
        List<Feed> feeds=feedService.getUserFeeds(Integer.MAX_VALUE,userIds,10);
        model.addAttribute("feeds",feeds);
        return "feeds";
    }


    @RequestMapping(path = {"/pushfeeds"}, method ={RequestMethod.POST,RequestMethod.GET})
    private String  getPushFeeds(Model model){
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<String> feedIds = jedisAdapter.lrange(RedisKeyUtil.getTimeLineKey(localUserId), 0, 10);
        List<Feed> feeds = new ArrayList<Feed>();
        for (String feedId : feedIds) {
            Feed feed = feedService.getFeedById(Integer.parseInt(feedId));
            if (feed != null) {
                feeds.add(feed);
            }
        }
        model.addAttribute("feeds", feeds);
        return "feeds";
    }

}
