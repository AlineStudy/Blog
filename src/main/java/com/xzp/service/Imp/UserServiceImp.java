package com.xzp.service.Imp;

import com.xzp.dao.UserMapper;
import com.xzp.model.User;
import com.xzp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserServiceImp implements UserService {
    

    private UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Transactional
    public int regist(User user) {
        int i = userMapper.insert(user);
        return i;
    }
    

    @Override
    public User login(String name, String password) {
        User user = new User();
        user.setEmail(name);
        user.setPassword(password);
        return userMapper.selectOne(user);
    }

    @Override
    public User findByEmail(String email) {
        User user = new User();
        user.setEmail(email);
        return userMapper.selectOne(user);
    }

    @Override
    public User findByPhone(String phone) {
        User user = new User();
        user.setPhone(phone);
        return userMapper.selectOne(user);
    }

    @Override
    public User findById(Long id) {
        User user = new User();
        user.setId(id);
        return userMapper.selectOne(user);
    }

    @Transactional
    public void deleteByEmail(String email) {
        User user = new User();
        user.setEmail(email);
        userMapper.delete(user);
    }

    @Transactional
    public void update(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Transactional
    public void deleteByEmailAndFalse(String email) {
        User user = new User();
        user.setEmail( email );
        userMapper.delete( user );
    }
}
