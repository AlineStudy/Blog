package com.xzp.service.Imp;

import com.xzp.dao.RoleUserMapper;
import com.xzp.model.RoleUser;
import com.xzp.model.User;
import com.xzp.service.RoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleUserServiceImp implements RoleUserService {


    private RoleUserMapper roleUserMapper;

    @Autowired
    public void setRoleUserMapper(RoleUserMapper roleUserMapper) {
        this.roleUserMapper = roleUserMapper;
    }

    @Override
    public List<RoleUser> findByUser(User user) {
        RoleUser roleUser = new RoleUser();
        roleUser.setUserId(user.getId());
        return roleUserMapper.select(roleUser);
    }

    @Override
    public int add(RoleUser roleUser) {
        return roleUserMapper.insert(roleUser);
    }

    @Override
    public void deleteByUserId(Long userId) {
        RoleUser roleUser = new RoleUser();
        roleUser.setUserId(userId);
        roleUserMapper.delete(roleUser);
    }
}
