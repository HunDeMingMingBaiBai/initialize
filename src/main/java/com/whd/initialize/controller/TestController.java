package com.whd.initialize.controller;

import com.whd.initialize.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author WHD
 * @date 2019/9/9 15:41
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @RequestMapping("/getAll")
    @ResponseBody
    public Object getAll(){
        return testService.getAll();
    }

}
