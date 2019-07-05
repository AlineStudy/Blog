package com.xzp.service.Imp;

import com.xzp.dao.RoleMapper;
import com.xzp.model.Role;
import com.xzp.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImp implements RoleService {


    private RoleMapper roleMapper;

    @Autowired
    public void setRoleMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    public Role findById(Long id) {
        Role role = new Role();
        role.setId(id);
        return roleMapper.selectOne(role);
    }

    @Override
    public int add(Role role) {
        return roleMapper.insert(role);
    }
}
