package com.wyf.springbootinit.bizmq;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MyMessageConsumerTest {
    @Resource
    private MyMessageConsumer myMessageConsumer;

    @Test
    void receiveMessage() {

    }
}