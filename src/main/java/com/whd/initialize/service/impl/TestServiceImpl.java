package com.whd.initialize.service.impl;

import com.whd.initialize.domain.Test;
import com.whd.initialize.mapper.TestMapper;
import com.whd.initialize.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestMapper testMapper;

    public List<Test> getAll(){
        return testMapper.getAll();
    }
}
