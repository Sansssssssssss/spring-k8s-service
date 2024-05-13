package com.losaxa.framework.auth.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.web.AuthenticationEntryPoint;


/**
 * 自定义oauth异常返回,返回自定义json返回
 */
public class CoreClientCredentialsTokenEndpointFilter extends ClientCredentialsTokenEndpointFilter {

    private AuthenticationEntryPoint              authenticationEntryPoint;
    private AuthorizationServerSecurityConfigurer configurer;

    public CoreClientCredentialsTokenEndpointFilter(AuthorizationServerSecurityConfigurer configurer) {
        this.configurer = configurer;
    }


    public void setAuthenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
        //必要设置
        super.setAuthenticationEntryPoint(null);
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected AuthenticationManager getAuthenticationManager() {
        //必要设置
        return configurer.and().getSharedObject(AuthenticationManager.class);
    }

    @Override
    public void afterPropertiesSet() {
        //返回自定义json返回
        setAuthenticationFailureHandler((request, response, e) ->
                authenticationEntryPoint.commence(request, response, e));
        //必要设置,client认证成功后进入/oauth/token
        setAuthenticationSuccessHandler((request, response, authentication) -> {
        });
    }

}
