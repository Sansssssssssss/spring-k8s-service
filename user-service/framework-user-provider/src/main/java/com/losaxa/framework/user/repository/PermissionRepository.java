package com.losaxa.framework.user.repository;

import com.losaxa.core.mongo.BaseMongoRepository;
import com.losaxa.framework.user.domian.Permission;

public interface PermissionRepository extends BaseMongoRepository<Permission, Long> {
}
