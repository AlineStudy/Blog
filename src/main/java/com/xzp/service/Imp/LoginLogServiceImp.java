package com.xzp.service.Imp;

import com.xzp.dao.LoginLogMapper;
import com.xzp.model.LoginLog;
import com.xzp.service.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LoginLogServiceImp implements LoginLogService {


    private LoginLogMapper loginLogMapper;

    @Autowired
    public void setLoginLogMapper(LoginLogMapper loginLogMapper) {
        this.loginLogMapper = loginLogMapper;
    }

    @Override
    public int add(LoginLog loginLog) {
        return loginLogMapper.insert(loginLog);
    }

    @Override
    public List<LoginLog> findAll() {
        return loginLogMapper.select(null);
    }

    @Override
    public List<LoginLog> findByUserId(Long id) {
        LoginLog loginLog = new LoginLog();
        loginLog.setUserId(id);
        return loginLogMapper.select(loginLog);
    }
}
