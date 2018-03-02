package com.example.service;

import com.example.dao.MessageDAO;
import com.example.model.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class MessageService {
    @Autowired
    MessageDAO messageDAO;
    @Autowired
    SensitiveService sensitiveService;
    public int addMessage(Message message){
        message.setContent(sensitiveService.filter(message.getContent()));
       return  messageDAO.addMessage(message)>0?message.getId():0;

    }
    public List<Message> getConversationDetail(String conversationId,int offset,int limit){
        return messageDAO.selectConversationDetail(conversationId,offset,limit);
    }

    public List<Message> getConversationList(int userId,int offset,int limit){
        return messageDAO.selectConversationList(userId,offset,limit);
    }

    public int getConversationUnreadCount(int userId,String conversationId){
        return messageDAO.getConversationUnreadCount(userId,conversationId);
    }
    public void setConversationHasread(String conversationId){
        messageDAO.setConversationHasread(conversationId);
    }
}
