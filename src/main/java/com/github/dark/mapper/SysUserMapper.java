package com.github.dark.mapper;

import com.github.dark.entity.SysUser;
import com.github.dark.vo.response.getFileEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.User;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface SysUserMapper extends Mapper<SysUser>,JpaSpecificationExecutor<SysUser> {
    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    SysUser selectUserByUserName(@Param("username") String userName);

    List<getFileEntity> getFile();
    List<getFileEntity> getFiles();
}
