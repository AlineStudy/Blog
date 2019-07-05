package com.xzp.service;

import com.xzp.model.User;

public interface UserService {

    //用户提交
    public int regist(User user);

    //用户登录
    public User login(String name,String password);

    //根据用户邮箱查询用户
    public User findByEmail(String email);

    //根据手机号码查询用户和
    public User findByPhone(String phone);

    //根据ID查询用户
    public User findById(Long id);

    //根据邮箱账号删除用户
    public void deleteByEmail(String email);

    //更新用户信息
    public void update(User user);
}
