package com.example.async.handler;

import com.example.async.EventHandler;
import com.example.async.EventModel;
import com.example.async.EventType;
import com.example.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class LoginHandler implements EventHandler {
    @Autowired
    MailSender mailSender;
    @Override
    public void doHandle(EventModel eventModel) {
        Map<String,Object> map=new HashMap<>();
        map.put("username",eventModel.getExt("username"));
        mailSender.sendWithHTMLTemplate(eventModel.getExt("email"),"登录异常","mail/login_exception.html", map);


    }

    @Override
    public List<EventType> getSupportEventTypes() {

        return Arrays.asList(EventType.LOGIN);
    }
}
