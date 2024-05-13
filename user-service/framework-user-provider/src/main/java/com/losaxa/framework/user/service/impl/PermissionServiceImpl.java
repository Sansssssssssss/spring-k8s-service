package com.losaxa.framework.user.service.impl;

import com.losaxa.core.autoservice.BaseServiceImpl;
import com.losaxa.framework.user.domian.Permission;
import com.losaxa.framework.user.enums.PermissionType;
import com.losaxa.framework.user.repository.PermissionRepository;
import com.losaxa.framework.user.service.PermissionService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.losaxa.framework.user.constant.DbField.TYPE;

@Primary
@Service
public class PermissionServiceImpl extends BaseServiceImpl<Permission,Long, PermissionRepository> implements PermissionService {

    public PermissionServiceImpl(PermissionRepository repository) {
        super(repository, Permission.class);
    }

    @Override
    public List<Permission> getPathPermission() {
        List<PermissionType> types = new ArrayList<>();
        types.add(PermissionType.MENU);
        types.add(PermissionType.BUTTON);
        return repository.findIn(types,TYPE);
    }

}
