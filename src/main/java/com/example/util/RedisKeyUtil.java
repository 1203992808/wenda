package com.example.util;

public class RedisKeyUtil {
    public static String SPLIT=":";
    public static String BIZ_LIKE="LIKE";
    public static String BIZ_DISLIKE="DISLIKE";
    public static String BIZ_EVENTQUEUE="EVENT_QUEUE";
    public static String BIZ_FOLLOWER="BIZ_FOLLOWER";
    public static String BIZ_FOLLOWEE="BIZ_FOLLOWEE";
    public static String BIZ_TIMELINE="BIZ_TIMELINE";
    public static String getLikeKey(int entityType,int entityId){
        return BIZ_LIKE+SPLIT+String.valueOf(entityType)+SPLIT+String.valueOf(entityId);
    }
    public static String getDislikeKey(int entityType,int entityId){
        return BIZ_DISLIKE+SPLIT+String.valueOf(entityType)+SPLIT+String.valueOf(entityId);
    }
    public static String getEventQueueKey(){
        return BIZ_EVENTQUEUE;
    }
    public static String getFollwerKey(int entityType,int entityId){
        return BIZ_FOLLOWER+SPLIT+String.valueOf(entityType)+SPLIT+String.valueOf(entityId);
    }

    public static String getFollweeKey(int userId,int entityType){
        return BIZ_FOLLOWEE+SPLIT+String.valueOf(userId)+SPLIT+String.valueOf(entityType);
    }

    public static String getTimeLineKey(int userId){
          return BIZ_TIMELINE + SPLIT + String.valueOf(userId);
    }



}
