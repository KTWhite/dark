package com.github.dark.mapper;

import com.github.dark.entity.User;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface LoginMapper extends Mapper<User> {

    List<User> selectAlls();
}
