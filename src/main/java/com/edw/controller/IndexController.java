package com.edw.controller;

import com.edw.service.UserService;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;

/**
 * <pre>
 *     com.edw.controller.IndexController
 * </pre>
 *
 * @author Muhammad Edwin < edwin at redhat dot com >
 * 03 Mei 2023 14:41
 */
@RestController
public class IndexController {

    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @GetMapping(path = "/")
    public HashMap index() {
        return new HashMap(){{
            put("hello", "world");
        }};
    }

    @GetMapping(path = "/user/{id}")
    @Retry(name = "instance1", fallbackMethod = "fallbackGetUser")
    public String getUser(@PathVariable("id") Integer id) throws IOException {
        return userService.getUser(id);
    }

    public String fallbackGetUser(Integer id, Exception ex) {
        logger.debug("==== giving default response ====");
        return "{}";
    }
}
