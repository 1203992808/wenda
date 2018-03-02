package com.example.controller;

import com.example.model.HostHolder;

import com.example.model.Message;
import com.example.model.User;
import com.example.model.ViewObject;
import com.example.service.MessageService;
import com.example.service.UserService;
import com.example.util.WwwUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    private static final Logger logger= LoggerFactory.getLogger(MessageController.class);
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;
    @RequestMapping(path = {"/msg/addMessage"}, method = RequestMethod.POST)
    @ResponseBody
    public String  addMessage(@RequestParam("toName")String toName,@RequestParam("content")String content){
        try {
                if(hostHolder.getUser()==null){
                    return WwwUtils.getJSONString(999,"没有登录");
                }
            User user=userService.getUserByName(toName);
            if(user==null){
                return WwwUtils.getJSONString(1,"用户不存在");
            }
            Message message=new Message();
            message.setCreatedDate(new Date());
            message.setFromId(hostHolder.getUser().getId());
            message.setContent(content);
            message.setToId(user.getId());
            messageService.addMessage(message);
            return WwwUtils.getJSONString(0);
        }catch(Exception e){
            logger.error("发送消息失败"+e.getMessage());
            return WwwUtils.getJSONString(1,"发送消息失败！");
        }

    }
    @RequestMapping(path={"/msg/list"},method={RequestMethod.GET})
    public String getConversationList(Model model){
        if(hostHolder.getUser()==null){
            return "redirect:/relogin";
        }
        int localUserId=hostHolder.getUser().getId();
        List<Message> messages=messageService.getConversationList(localUserId,0,10);
        List<ViewObject> conversations=new ArrayList<>();
        for (Message message:messages
             ) {
            ViewObject vo=new ViewObject();
            vo.set("conversation",message);
            int userId=message.getFromId()==localUserId?message.getToId():message.getFromId();
            vo.set("user",userService.getUser(userId));
            vo.set("unread",messageService.getConversationUnreadCount(localUserId,message.getConversationId()));
            conversations.add(vo);

        }
        model.addAttribute("conversations",conversations);
        return "letter";
    }
   @RequestMapping(path={"/msg/detail"},method={RequestMethod.GET})
    public String getConversationDetail(Model model,@RequestParam("conversationId")String conversationId){
        try {
            messageService.setConversationHasread(conversationId);
            List<Message> messageList=messageService.getConversationDetail(conversationId,0,10);
            List<ViewObject>  messages=new ArrayList<>();
            for (Message message:messageList
                 ) {

                ViewObject vo=new ViewObject();
                vo.set("message",message);
                vo.set("user",userService.getUser(message.getFromId()));

                messages.add(vo);

            }
            model.addAttribute("messages",messages);

        }catch(Exception e){
            logger.error("获取详情失败"+e.getMessage());

        }
        return "letterDetail";
    }
}
