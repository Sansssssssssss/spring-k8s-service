package com.losaxa.framework.user.config;

import com.losaxa.core.security.config.CoreResourceServerConfig;
import com.losaxa.framework.user.domian.Permission;
import com.losaxa.framework.user.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;

import java.util.List;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends CoreResourceServerConfig {

    @Autowired
    private PermissionService permissionService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests = http.authorizeRequests();
        authorizeRequests.mvcMatchers("/auth/username")
                .access("@internalAccessService.isInternal(request,authentication)");
        List<Permission> pathPermission = permissionService.getPathPermission();
        for (Permission permission : pathPermission) {
            authorizeRequests.antMatchers(permission.getPath()).hasAnyAuthority(permission.getCode());
            //hasAnyAuthority("any") 会验证 any
            //hasAnyRole("any") 会验证 ROLE_any
        }
        super.configure(http);
    }

}
