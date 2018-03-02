package com.example.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;

import java.util.List;
import java.util.Set;

@Service
public class JedisAdapter implements InitializingBean{
    private static final Logger logger= LoggerFactory.getLogger(JedisAdapter.class);
    JedisPool pool;

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis://localhost:6379/10");
    }
    public long sadd(String key,String value){
        Jedis jedis=null;
        try {
             jedis=pool.getResource();
           return  jedis.sadd(key,value);

        } catch (Exception e ){

            logger.error("发生错误"+e.getMessage());

        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return 0;
    }


    public List<String> lrange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lrange(key, start, end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }


    public long lpush(String key,String value){
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return  jedis.lpush(key,value);

        } catch (Exception e ){

            logger.error("发生错误"+e.getMessage());

        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return 0;
    }

    public long srem(String key,String value){
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return  jedis.srem(key,value);

        } catch (Exception e ){

            logger.error("发生错误"+e.getMessage());

        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return 0;
    }
    public long scard(String key){
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return  jedis.scard(key);

        } catch (Exception e ){

            logger.error("发生错误"+e.getMessage());

        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return 0;

    }

    public boolean sismember(String key,String value){
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return  jedis.sismember(key,value);

        } catch (Exception e ){

            logger.error("发生错误"+e.getMessage());

        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return false;

    }


    public Jedis getJedis(){
        return pool.getResource();
    }
    public Transaction multi(Jedis jedis){
        try {

            return  jedis.multi();

        } catch (Exception e ){

            logger.error("发生错误"+e.getMessage());

        }
        return null;

    }
    public List<Object> exec(Transaction tx,Jedis jedis){

        try {

            return  tx.exec();

        } catch (Exception e ){

            logger.error("发生错误"+e.getMessage());

        }finally {
          try {
              if(tx!=null){
                  tx.close();
              }

          }catch (Exception e){
              logger.error("关闭事务错误");

          }

            try {
                if(jedis!=null){
                    jedis.close();
                }

            }catch (Exception e){
                logger.error("关闭jedis错误");

            }
        }
        return null;


    }
    public long zadd(String key,int score,String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zadd(key, score, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;

    }


    public Set<String> zrange(String key, int start, int end){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrange(key, start, end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;

    }

    public long zcard(String key){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;

    }
    public Double zscore(String key,String member){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zscore(key, member);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;

    }

    public Set<String> zrevrange(String key, int start, int end){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;

    }
    public List<String> brpop(int timeout,String key){
        Jedis jedis=null;
        try {
            jedis=pool.getResource();
            return  jedis.brpop(timeout,key);

        } catch (Exception e ){

            logger.error("发生错误"+e.getMessage());

        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return null;

    }


    public static void print(int index, Object object){
        System.out.println(String.format("%d,%s",index, object.toString()));
    }



    public static void main(String[] args) {
        Jedis jedis=new Jedis("redis://localhost:6379/9");
        jedis.set("hello","world");
         jedis.rename("hello","newhello");
         jedis.setex("hello2",15,"world2");
        // jedis.set("pv","100");
         jedis.incr("pv");

         print(2,jedis.get("pv"));
        print(3,jedis.get("pv"));
        jedis.incrBy("pv",6);
        print(2,jedis.get("pv"));
        jedis.decrBy("pv",3);
        print(2,jedis.get("pv"));

        String listName="list";
        for (int i = 0; i <10 ; i++) {
            jedis.lpush(listName,String.valueOf(i));

        }
        print(3,jedis.lrange(listName,0,12));
        print(4,jedis.llen(listName));
        print(5,jedis.lpop(listName));

        print(5,jedis.brpop(0,listName));
        print(6,jedis.llen(listName));
        print(7,jedis.lindex(listName,4));
        print(8,jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER,"1","xx"));
        print(8,jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE,"1","xx"));
        print(9,jedis.lrange(listName,0,10));
        String userKey="userXX";
       jedis.hset(userKey,"name","lutt");
        jedis.hset(userKey,"age","23");
        jedis.hset(userKey,"address","QingDao");
        print(10,jedis.hget(userKey,"name") );
       print(11,jedis.hgetAll(userKey));
       jedis.hdel(userKey,"address");
        print(12,jedis.hgetAll(userKey));
        print(13,jedis.hexists(userKey,"name"));
        print(14,jedis.hkeys(userKey));
        print(15,jedis.hvals(userKey));
        jedis.hsetnx(userKey,"colleage","zjUniversity");
        print(16,jedis.hgetAll(userKey));


        String likeKey1="commentLike1";
        String likeKey2="commentLike2";
        String likeKey3="commentLike3";
        for (int i = 0; i <10 ; i++) {
            jedis.sadd(likeKey1,"a"+String.valueOf(i));
            jedis.sadd(likeKey2,"a"+String.valueOf(i*i));
            
        }
        print(17,jedis.smembers(likeKey1));
        print(18,jedis.smembers(likeKey2));
        print(19,jedis.sunion(likeKey1,likeKey2));
        print(20,jedis.sdiff(likeKey1,likeKey2));
        print(21,jedis.sinter(likeKey1,likeKey2));
        print(22,jedis.sismember(likeKey1,"12"));
        print(23,jedis.srem(likeKey1,"a1"));
        jedis.smove(likeKey1,likeKey2,"a5");
        print(24,jedis.smembers(likeKey2));
        print(25,jedis.scard(likeKey1));
        print(26,jedis.srandmember(likeKey1,3 ));

        String rankKey="rankKey";
        jedis.zadd(rankKey,60,"lutt");
        jedis.zadd(rankKey,65,"ytt");
        jedis.zadd(rankKey,70,"zyh");
        jedis.zadd(rankKey,63,"ln");
        jedis.zadd(rankKey,80,"yyy");
        jedis.zadd(rankKey,90,"zzt");
        print(27,jedis.zcard(rankKey));
        print(28,jedis.zcount(rankKey,60,100));
        jedis.zincrby(rankKey,20,"lutt");
        print(29,jedis.zscore(rankKey,"lutt"));
        print(30,jedis.zrange(rankKey,0,100));
        print(31,jedis.zrange(rankKey,1,3));
        print(32,jedis.zrevrange(rankKey,1,3));
        for (Tuple tuple:jedis.zrangeByScoreWithScores(rankKey,70,100)
             ) {
            System.out.println(tuple.getElement()+" : "+String.valueOf(tuple.getScore()));
            
        }
        print(33,jedis.zrank(rankKey,"lutt"));
        print(34,jedis.zrevrank(rankKey,"lutt"));

        String setKey="setKey";
        jedis.zadd(setKey,1,"a");
        jedis.zadd(setKey,1,"b");
        jedis.zadd(setKey,1,"c");
        jedis.zadd(setKey,1,"d");
        print(35,jedis.zlexcount(setKey,"-","+"));
        print(35,jedis.zlexcount(setKey,"[c","[d"));
        jedis.zremrangeByLex(setKey,"[c","+");
        print(36,jedis.zrange(setKey,0,10));

        JedisPool pool=new JedisPool();
        for (int i = 0; i <10 ; i++) {
            Jedis j=pool.getResource();
            j.set("a","lutt");
            print(37,j.get("a"));
            j.close();

        }
        User user=new User();
        user.setId(1);
        user.setPassword("123");
        user.setHeadUrl("a.png");
        user.setSalt("rr343");
        user.setName("lutt");

        print(38, JSONObject.toJSONString(user));
        jedis.set("user",JSONObject.toJSONString(user));
        print(39,jedis.get("user"));
        String value=jedis.get("user");
        User user1= JSON.parseObject(value,User.class);
        print(40,user1.getName());




    }


}
