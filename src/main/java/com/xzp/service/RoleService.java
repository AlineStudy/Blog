package com.xzp.service;

import com.xzp.model.Role;

public interface RoleService {

    /**
     * 根据id查询角色
     * @param id
     * @return
     */
    Role findById(Long id);

    /**
     * 添加角色
     * @param role
     * @return
     */
    int add(Role role);

}
