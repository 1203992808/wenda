package com.example.controller;

import com.example.model.*;
import com.example.service.*;
import com.example.util.WwwUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.swing.text.html.ObjectView;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    QuestionService questionService;
   @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;
    @Autowired
    LikeService likeService;
    @Autowired
    FollowService followService;

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

   @RequestMapping(value="/question/add",method={RequestMethod.POST})
   @ResponseBody
    public String addQuestion(@RequestParam("title")String title,@RequestParam("content")String content){
        try{
            Question question=new Question();
            question.setCommentCount(0);
            question.setCreatedDate(new Date());
            question.setTitle(title);
            question.setContent(content);
            if(hostHolder.getUser()==null){
                return WwwUtils.getJSONString(999);

            }else{
                question.setUserId(hostHolder.getUser().getId());
            }
            if(questionService.addQuestion(question)>0){
                return WwwUtils.getJSONString(0);
            }
            ;

        }catch(Exception e){
            logger.error("增加失败"+e.getMessage());

        }
        return WwwUtils.getJSONString(1,"失败");
    }
    @RequestMapping(value="/question/{qid}")
    public String QuestionDetail(@PathVariable("qid")int qid,Model model){
        Question question=questionService.getQuestionById(qid);
        List<Comment> commentList=commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject>  comments=new ArrayList<ViewObject>();
        for (Comment comment:commentList
             ) {
            ViewObject vo=new ViewObject();
            vo.set("comment",comment);
            if(hostHolder.getUser()==null){
                vo.set("liked",0);
            }else{
                vo.set("liked",likeService.getLikeStatus(hostHolder.getUser().getId(),EntityType.ENTITY_COMMENT,comment.getId()));
            }
            vo.set("user",userService.getUser(comment.getUserId()));
            long count=likeService.getLikeCount(EntityType.ENTITY_COMMENT,comment.getId());
            vo.set("likeCount",count);

            comments.add(vo);
            
        }
        model.addAttribute("question",question);
        model.addAttribute("comments",comments);
        model.addAttribute("user",userService.getUser(question.getUserId()));
        List<ViewObject> followUsers = new ArrayList<ViewObject>();
        // 获取关注的用户信息
        List<Integer> users = followService.getFollowers(EntityType.ENTITY_QUESTION, qid, 20);
        for (Integer userId : users) {
            ViewObject vo = new ViewObject();
            User u = userService.getUser(userId);
            if (u == null) {
                continue;
            }
            vo.set("name", u.getName());
            vo.set("headUrl", u.getHeadUrl());
            vo.set("id", u.getId());
            followUsers.add(vo);
        }
        model.addAttribute("followUsers", followUsers);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followed", followService.isFollwers(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, qid));
        } else {
            model.addAttribute("followed", false);
        }



        return "detail";

    }
}
