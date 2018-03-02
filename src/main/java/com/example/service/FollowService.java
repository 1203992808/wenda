package com.example.service;

import com.example.util.JedisAdapter;
import com.example.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class FollowService {

    @Autowired
    JedisAdapter jedisAdapter;
    public boolean follow(int userId,int entityType,int entityId ){
        String followerKey=RedisKeyUtil.getFollwerKey(entityType,entityId);
        String followeeKey=RedisKeyUtil.getFollweeKey(userId,entityType);
        Date date=new Date();
        Jedis jedis=jedisAdapter.getJedis();
        Transaction tx=jedisAdapter.multi(jedis);
        tx.zadd(followerKey,date.getTime(),String.valueOf(userId));
        tx.zadd(followeeKey,date.getTime(),String.valueOf(entityId));
        List<Object>  ret=jedisAdapter.exec(tx,jedis);
        return ret.size()==2&&(Long)ret.get(0)>0&&(Long)ret.get(1)>0;
    }


    public boolean unfollow(int userId,int entityType,int entityId ){
        String followerKey=RedisKeyUtil.getFollwerKey(entityType,entityId);
        String followeeKey=RedisKeyUtil.getFollweeKey(userId,entityType);
        Date date=new Date();
        Jedis jedis=jedisAdapter.getJedis();
        Transaction tx=jedisAdapter.multi(jedis);
        tx.zrem(followerKey,String.valueOf(userId));
        tx.zrem(followeeKey,String.valueOf(entityId ));
        List<Object>  ret=jedisAdapter.exec(tx,jedis);
        return ret.size()==2&&(Long)ret.get(0)>0&&(Long)ret.get(1)>0;
    }
    private List<Integer>  getIdsFromSets(Set<String> set){
        List<Integer> list=new ArrayList<>();
        for (String str:set
             ) {
            list.add(Integer.parseInt(str));

        }
        return list;
    }
    public List<Integer> getFollowers(int entityType,int entityId,int count){
        String followersKey=RedisKeyUtil.getFollwerKey(entityType,entityId);
        return getIdsFromSets(jedisAdapter.zrevrange(followersKey,0,count));
    }

    public List<Integer> getFollowers(int entityType,int entityId,int offset,int count){
        String followersKey=RedisKeyUtil.getFollwerKey(entityType,entityId);
        return getIdsFromSets(jedisAdapter.zrevrange(followersKey,offset,count));
    }
    public List<Integer> getFollowees(int entityType,int entityId,int count){
        String followeesKey=RedisKeyUtil.getFollweeKey(entityType,entityId);
        return getIdsFromSets(jedisAdapter.zrevrange(followeesKey,0,count));
    }
    public List<Integer> getFollowees(int entityType,int entityId,int offset,int count){
        String followeesKey=RedisKeyUtil.getFollweeKey(entityType,entityId);
        return getIdsFromSets(jedisAdapter.zrevrange(followeesKey,offset,count));
    }
    public long getFolloweesCount(int entityType,int entityId){
        String followeesKey=RedisKeyUtil.getFollweeKey(entityType,entityId);
        return jedisAdapter.zcard(followeesKey);
    }
    public long getFollowersCount(int entityType,int entityId){
        String followersKey=RedisKeyUtil.getFollwerKey(entityType,entityId);
        return jedisAdapter.zcard(followersKey);
    }
    public boolean isFollwers(int userId,int entityType,int entityId){
        String followersKey=RedisKeyUtil.getFollwerKey(entityType,entityId);
        return jedisAdapter.zscore(followersKey,String.valueOf(userId))!=null;

    }

}
