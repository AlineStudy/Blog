package com.xzp.service;

import com.xzp.model.RoleUser;
import com.xzp.model.User;

import java.util.List;

public interface RoleUserService {


    /**
     * 根据用户查询角色用户集合
     * @param user
     * @return
     */
    List<RoleUser> findByUser(User user);


    /**
     * 添加用户角色
     * @param roleUser
     * @return
     */
    int add(RoleUser roleUser);


    /**
     * 根据用户id删除
     * @param userId
     */
    void deleteByUserId(Long userId);
}
