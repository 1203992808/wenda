/*
package com.example;

import com.example.model.EntityType;
import com.example.service.FollowService;
import com.example.util.JedisAdapter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootApplication
@Sql("/init-schema.sql")
public class InitDatabaseTests {



    @Autowired
    FollowService followService;

    @Autowired
    JedisAdapter jedisAdapter;

    @Test
    public void contextLoads() {
        Random random = new Random();
        jedisAdapter.getJedis().flushDB();
        for (int i = 0; i < 11; ++i) {


            for (int j = 1; j < i; ++j) {
                followService.follow(j, EntityType.ENTITY_USER, i);
            }


        }

    }


}
*/
