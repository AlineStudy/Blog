package com.xzp.service;

import com.xzp.model.UserInfo;

public interface UserInfoService {

    /**
     * 根据用户ID查找用户详细信息
     * @param id
     * @return
     */
    public UserInfo findByUserId(Long id);

    /**
     * 更新用户详细信息
     * @param userInfo
     */
    public void update(UserInfo userInfo);


    /**
     * 添加用户详细信息
     * @param userInfo
     */
    public void add(UserInfo userInfo);
}
