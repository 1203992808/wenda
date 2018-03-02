package com.example.service;

import com.example.dao.FeedDAO;
import com.example.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedService {
    @Autowired
    FeedDAO feedDAO;
    public boolean addFeed(Feed feed){
        feedDAO.addFeed(feed);
        return feed.getId()>0;
    }
    public Feed getFeedById(int id){
        return feedDAO.selectFeedById(id);
    }

    public List<Feed> getUserFeeds(int maxId,List<Integer> userIds,int count){
        return feedDAO.selectUserFeeds(maxId,userIds,count);
    }
}
