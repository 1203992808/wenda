package com.example.service;

import com.example.dao.LoginTicketDAO;
import com.example.dao.UserDao;
import com.example.model.LoginTicket;
import com.example.model.User;
import com.example.util.WwwUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDao userDAO;
    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public User getUser(int id) {

        return userDAO.selectById(id);
    }
    public Map<String,String>  register(String username,String password){
        Map<String,String> map=new HashMap<>();
        if(StringUtils.isEmpty(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isEmpty(password)){
            map.put("msg","密码不能为空");
            return map;
        }
        User user=userDAO.selectByName(username);
        if(user!=null){
            map.put("msg","用户名已经被注册");
            return map;
        }
         user=new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(head);
        user.setPassword(WwwUtils.MD5(password+user.getSalt()));
        userDAO.addUser(user);

        String  ticket=addTicket(user.getId());
        map.put("ticket",ticket);
        return map;

    }

    public Map<String,String>  login(String username,String password){
        Map<String,String> map=new HashMap<>();
        if(StringUtils.isEmpty(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isEmpty(password)){
            map.put("msg","密码不能为空");
            return map;
        }
        User user=userDAO.selectByName(username);
        if(user==null){
            map.put("msg","用户名不存在！");
            return map;
        }


        if(!WwwUtils.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msg","密码错误！！！");
            return map;

        }
        String  ticket=addTicket(user.getId());
        map.put("ticket",ticket);
        return map;

    }
    public String addTicket(int userId){
        LoginTicket loginTicket=new LoginTicket();
        loginTicket.setUserId(userId);
        Date now=new Date();
        now.setTime(3600*24*1000+now.getTime());
        loginTicket.setExpired(now);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    public void logout(String ticket){

        loginTicketDAO.updateStatus(ticket,1);
    }
    public User getUserByName(String name){
        return userDAO.selectByName(name);
    }
}
