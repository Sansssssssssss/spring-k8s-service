package com.losaxa.framework.user.service.impl;

import com.losaxa.core.autoservice.BaseServiceImpl;
import com.losaxa.framework.user.domian.Role;
import com.losaxa.framework.user.repository.RoleRepository;
import com.losaxa.framework.user.service.RoleService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.losaxa.framework.user.constant.DbField.CODE;

@Primary
@Service
public class RoleServiceImpl extends BaseServiceImpl<Role, Long, RoleRepository> implements RoleService {
    public RoleServiceImpl(RoleRepository repository) {
        super(repository, Role.class);
    }

    /**
     * 查询角色权限
     *
     * @param codes
     * @return
     */
    @Override
    public List<String> getRolePermission(Collection<String> codes) {
        List<Role> roles = repository.findIn(codes,CODE);
        return roles.stream().flatMap(e -> e.getPermission().stream()).distinct().collect(Collectors.toList());
    }
}
