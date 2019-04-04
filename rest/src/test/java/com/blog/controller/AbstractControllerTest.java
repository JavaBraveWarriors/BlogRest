package com.blog.controller;

import com.blog.JsonConverter;
import com.blog.controller.config.ControllerTestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = ControllerTestConfiguration.class)
public abstract class AbstractControllerTest {
    @Autowired
    protected JsonConverter jsonConverter;

    protected abstract String getEndpoint();
}