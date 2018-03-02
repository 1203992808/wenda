package com.example.service;

import com.example.util.JedisAdapter;
import com.example.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;
    public long getLikeCount(int entityType,int entityId){
        String likeKey= RedisKeyUtil.getLikeKey(entityType,entityId);
        long count=jedisAdapter.scard(likeKey);

        return count;

    }
    public int getLikeStatus(int userId,int entityType,int entityId){
        String likeKey= RedisKeyUtil.getLikeKey(entityType,entityId);
        if(jedisAdapter.sismember(likeKey,String.valueOf(userId))){
            return 1;
        }

        String dislikeKey= RedisKeyUtil.getDislikeKey(entityType,entityId);
        return jedisAdapter.sismember(dislikeKey,String.valueOf(userId))?-1:0;


    }
    public long like(int userId,int entityType,int entityId){
        String likeKey= RedisKeyUtil.getLikeKey(entityType,entityId);
        jedisAdapter.sadd(likeKey,String.valueOf(userId));
        String dislikeKey= RedisKeyUtil.getDislikeKey(entityType,entityId);
        jedisAdapter.srem(dislikeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }

    public long dislike(int userId,int entityType,int entityId){
        String dislikeKey= RedisKeyUtil.getDislikeKey(entityType,entityId);
        jedisAdapter.sadd(dislikeKey,String.valueOf(userId));
        String likeKey= RedisKeyUtil.getDislikeKey(entityType,entityId);
        jedisAdapter.srem(likeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }


}
