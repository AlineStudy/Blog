package com.xzp.service;

import com.xzp.model.RoleResource;

import java.util.List;

public interface RoleResourceService {


    /**
     * 添加roleResource
     * @param roleResource
     */
    void add(RoleResource roleResource);


    /**
     * 根据id查询RoleResource
     * @param id
     * @return
     */
    RoleResource findById(Long id);


    /**
     * 根据角色id查询角色资源集合
     * @param id
     * @return
     */
    List<RoleResource> findByRoleId(Long id);


    /**
     * 根据id删除RoleResource
     * @param id
     */
    void deleteById(Long id);
}
