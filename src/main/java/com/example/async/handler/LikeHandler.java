package com.example.async.handler;

import com.example.async.EventHandler;
import com.example.async.EventModel;
import com.example.async.EventType;
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
public class LikeHandler implements EventHandler {
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
            message.setContent("用户"+user.getName()+"给你的评论点了赞 http://127.0.0.1:8080/question/"+ eventModel.getExt("questionId"));
            messageService.addMessage(message);

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
