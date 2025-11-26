package com.miniBili.web;

import com.miniBili.component.ESsearchComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class initRun implements ApplicationRunner {

    @Autowired
    private ESsearchComponent eSsearchComponent;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        eSsearchComponent.createIndex();
    }
}
