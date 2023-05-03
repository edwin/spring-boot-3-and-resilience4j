package com.edw.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Random;

/**
 * <pre>
 *     com.edw.service.UserService
 * </pre>
 *
 * @author Muhammad Edwin < edwin at redhat dot com >
 * 03 Mei 2023 18:40
 */
@Service
public class UserService {

    @Autowired
    private RestTemplate restTemplate;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public String getUser(Integer id) throws IOException {

        if(new Random().nextBoolean()) {
            logger.debug("==== simulate random exception ====");
            throw new IOException();
        }

        ResponseEntity<String> users = restTemplate.getForEntity("https://reqres.in/api/user/"+id, String.class);
        return users.getBody();
    }

}
