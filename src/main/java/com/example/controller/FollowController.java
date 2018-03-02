package com.example.controller;

import com.example.async.EventModel;
import com.example.async.EventProducer;
import com.example.async.EventType;
import com.example.model.*;
import com.example.service.CommentService;
import com.example.service.FollowService;
import com.example.service.QuestionService;
import com.example.service.UserService;
import com.example.util.WwwUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class FollowController {

    private static final Logger logger= LoggerFactory.getLogger(CommentController.class);
    @Autowired
    HostHolder hostHolder;
    @Autowired
    FollowService followService;
    @Autowired
    EventProducer eventProducer;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    QuestionService questionService;
    @ResponseBody
    @RequestMapping(path = {"/followUser"}, method = RequestMethod.POST)
    public String follow(@RequestParam("userId")int userId){
        if(hostHolder.getUser()==null){
            WwwUtils.getJSONString(999);
        }

        boolean ret=followService.follow(hostHolder.getUser().getId(),EntityType.ENTITY_USER,userId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setEntityType(EntityType.ENTITY_USER).setActorId(hostHolder.getUser().getId())
                .setEntityId(userId).setEntityOwnerId(userId));
        return WwwUtils.getJSONString(ret?0:1,String.valueOf(followService.getFolloweesCount(hostHolder.getUser().getId(),EntityType.ENTITY_USER)));

    }

    @ResponseBody
    @RequestMapping(path = {"/unfollowUser"}, method = RequestMethod.POST)
    public String unfollow(@RequestParam("userId")int userId){
        if(hostHolder.getUser()==null){
            WwwUtils.getJSONString(999);
        }
        boolean ret=followService.unfollow(hostHolder.getUser().getId(),EntityType.ENTITY_USER,userId);
      /*  eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                .setEntityType(EntityType.ENTITY_USER).setActorId(hostHolder.getUser().getId())
                .setEntityId(userId).setEntityOwnerId(userId));*/
        return WwwUtils.getJSONString(ret?0:1,String.valueOf(followService.getFolloweesCount(hostHolder.getUser().getId(),EntityType.ENTITY_USER)));


    }


    @ResponseBody
    @RequestMapping(path = {"/followQuestion"}, method = RequestMethod.POST)
    public String followQuestion(@RequestParam("questionId")int questionId){
        if(hostHolder.getUser()==null){
            WwwUtils.getJSONString(999);
        }
        Question question=questionService.getQuestionById(questionId);
        if(question==null){
            return WwwUtils.getJSONString(1,"问题不存在");
        }
        Map<String,Object> info=new HashMap<>();
        info.put("id",hostHolder.getUser().getId());
        info.put("name",hostHolder.getUser().getName());
        info.put("headUrl",hostHolder.getUser().getHeadUrl());
        info.put("count",followService.getFollowersCount(EntityType.ENTITY_QUESTION,questionId));
        boolean ret=followService.follow(hostHolder.getUser().getId(),EntityType.ENTITY_QUESTION,questionId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setEntityType(EntityType.ENTITY_QUESTION).setActorId(hostHolder.getUser().getId())
                .setEntityId(questionId).setEntityOwnerId(question.getUserId()));
        return WwwUtils.getJSONString(ret?0:1,info);


    }


    @ResponseBody
    @RequestMapping(path = {"/unfollowQuestion"}, method = RequestMethod.POST)
    public String unfollowQuestion(@RequestParam("questionId")int questionId){
        if(hostHolder.getUser()==null){
            WwwUtils.getJSONString(999);
        }
        Question question=questionService.getQuestionById(questionId);
        if(question==null){
            return WwwUtils.getJSONString(1,"问题不存在");
        }
        Map<String,Object> info=new HashMap<>();
        info.put("id",hostHolder.getUser().getId());
        info.put("name",hostHolder.getUser().getName());
        info.put("headUrl",hostHolder.getUser().getHeadUrl());
        info.put("count",followService.getFollowersCount(EntityType.ENTITY_QUESTION,questionId));
        boolean ret=followService.unfollow(hostHolder.getUser().getId(),EntityType.ENTITY_QUESTION,questionId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setEntityType(EntityType.ENTITY_QUESTION).setActorId(hostHolder.getUser().getId())
                .setEntityId(questionId).setEntityOwnerId(question.getUserId()));
        return WwwUtils.getJSONString(ret?0:1,info);
    }


    @RequestMapping(path = {"/user/{uid}/followees"}, method = RequestMethod.GET)
   public String followees(@PathVariable("uid")int userId, Model model){
        List<Integer> followeesIds=followService.getFollowees(userId,EntityType.ENTITY_USER,0,10);
        if(hostHolder.getUser()!=null){
            model.addAttribute("followees",getUsersInfo(hostHolder.getUser().getId(),followeesIds));
        }else{
            model.addAttribute("followees",getUsersInfo(0,followeesIds));

        }
        model.addAttribute("followeeCount", followService.getFolloweesCount(userId, EntityType.ENTITY_USER));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followees";





    }

    @RequestMapping(path = {"/user/{uid}/followers"}, method = RequestMethod.GET)
    public String followers( Model model,@PathVariable("uid")int userId){
        List<Integer> followersIds=followService.getFollowers(EntityType.ENTITY_USER,userId,0,10);
        if(hostHolder.getUser()!=null){
            model.addAttribute("followers",getUsersInfo(hostHolder.getUser().getId(),followersIds));
        }else{
            model.addAttribute("followers",getUsersInfo(0,followersIds));

        }
        model.addAttribute("followerCount", followService.getFollowersCount(EntityType.ENTITY_USER, userId));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followers";



    }
    private  List<ViewObject>  getUsersInfo(int localUserId,List<Integer> userIds){
        List<ViewObject> usersInfo=new ArrayList<>();
        for (Integer id:userIds
             ) {
            User user=userService.getUser(id);
            if(user==null){
                continue;
            }
            ViewObject vo=new ViewObject();
            vo.set("user",user);
            vo.set("followersCount",followService.getFollowersCount(EntityType.ENTITY_USER,id));
            vo.set("followeesCount",followService.getFollowersCount(id,EntityType.ENTITY_USER));
            vo.set("commentCount", commentService.getUserCommentCount(id));
            if (localUserId != 0) {
                vo.set("followed", followService.isFollwers(localUserId, EntityType.ENTITY_USER, id));
            } else {
                vo.set("followed", false);
            }
            usersInfo.add(vo);


        }
        return usersInfo;
    }
}
