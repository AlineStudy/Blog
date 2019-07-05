package com.xzp.service.Imp;

import com.xzp.dao.UserInfoMapper;
import com.xzp.model.UserInfo;
import com.xzp.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserInfoServiceImp implements UserInfoService {


    private UserInfoMapper userInfoMapper;

    @Autowired
    public void setUserInfoMapper(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

    @Override
    public UserInfo findByUserId(Long id) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(id);
        return userInfoMapper.selectOne(userInfo);
    }

    @Override
    public void update(UserInfo userInfo) {
        userInfoMapper.updateByPrimaryKey(userInfo);
    }

    @Override
    public void add(UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
    }
}
