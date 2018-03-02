package com.example.async.handler;

import com.example.async.EventHandler;
import com.example.async.EventModel;
import com.example.async.EventType;
import com.example.model.EntityType;
import com.example.model.Message;
import com.example.model.User;
import com.example.service.MessageService;
import com.example.service.UserService;
import com.example.util.WwwUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class FollowHandler implements EventHandler {
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;
    @Override
    public void doHandle(EventModel eventModel) {
        Message message=new Message();
        message.setFromId(WwwUtils.SYSTEM_USERID);
        message.setToId(eventModel.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user=userService.getUser(eventModel.getActorId());
        if(eventModel.getEntityType()== EntityType.ENTITY_QUESTION){

            message.setContent("用户"+user.getName()+"关注了你的问题 http://127.0.0.1:8080/question/"+ eventModel.getEntityId());
        }else{
            message.setContent("用户"+user.getName()+"关注了你 http://127.0.0.1:8080/user/"+ eventModel.getActorId());

        }
        messageService.addMessage(message);


    }


    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW);
    }
}
