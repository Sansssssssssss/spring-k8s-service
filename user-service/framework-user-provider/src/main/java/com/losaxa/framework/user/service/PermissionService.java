package com.losaxa.framework.user.service;

import com.losaxa.core.autoservice.BaseService;
import com.losaxa.framework.user.domian.Permission;
import com.losaxa.framework.user.repository.PermissionRepository;

import java.util.List;

public interface PermissionService extends BaseService<Permission, Long, PermissionRepository> {

    List<Permission> getPathPermission();

}
