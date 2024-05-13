package com.losaxa.framework.user.service;

import com.losaxa.core.autoservice.BaseService;
import com.losaxa.framework.user.domian.Role;
import com.losaxa.framework.user.repository.RoleRepository;

import java.util.Collection;
import java.util.List;

public interface RoleService extends BaseService<Role,Long, RoleRepository> {

    /**
     * 查询角色权限
     * @param codes
     * @return
     */
    List<String> getRolePermission(Collection<String> codes);

}
