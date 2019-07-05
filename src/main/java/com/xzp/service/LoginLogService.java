package com.xzp.service;

import com.xzp.model.LoginLog;

import java.util.List;

public interface LoginLogService {


    /**
     * 添加日志
     * @param loginLog
     * @return
     */
    int add(LoginLog loginLog);

    /**
     * 查询所有日志
     * @return
     */
    List<LoginLog> findAll();


    /**
     * 更具用户id查询日志集合
     * @param id
     * @return
     */
    List<LoginLog> findByUserId(Long id);
}
