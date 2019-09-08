package com.whd.initialize.mapper;

import com.whd.initialize.domain.Test;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestMapper {

    List<Test> getAll();

}
