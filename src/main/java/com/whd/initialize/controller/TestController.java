package com.whd.initialize.controller;

import com.whd.initialize.domain.Test;
import com.whd.initialize.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @RequestMapping("/getAll")
    @ResponseBody
    public List<Test> getAll(){
        return testService.getAll();
    }

}
