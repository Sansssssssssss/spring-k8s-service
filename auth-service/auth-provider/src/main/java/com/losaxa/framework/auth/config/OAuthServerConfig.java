package com.losaxa.framework.auth.config;

import com.google.common.collect.ImmutableMap;
import com.losaxa.core.security.CoreAuthenticationEntryPoint;
import com.losaxa.core.security.LoginUser;
import com.losaxa.framework.auth.filter.CoreClientCredentialsTokenEndpointFilter;
import com.losaxa.framework.auth.granter.CaptchaTokenGranter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.*;

/**
 * oauth2认证服务配置,基于access toekn进行认证授权
 */
@EnableAuthorizationServer
@Configuration
public class OAuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private ClientDetailsService         clientDetailsService;
    @Autowired
    private AuthenticationManager        authenticationManager;
    @Autowired
    private TokenStore                   tokenStore;
    @Autowired
    private PasswordEncoder              passwordEncoder;
    @Autowired
    private CoreAuthenticationEntryPoint coreAuthenticationEntryPoint;

    /**
     * jwt token生成器(可选项,可以以jwt为载体保存客户信息)
     *
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey("corejwtsigningkey000");
        return jwtAccessTokenConverter;
    }

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (oAuth2AccessToken, oAuth2Authentication) -> {
            // 添加额外信息的map
            final Map<String, Object> additionMessage = new HashMap<>(3);
            // 对于客户端鉴权模式，直接返回token
            if (oAuth2Authentication.getUserAuthentication() == null) {
                return oAuth2AccessToken;
            }
            // 获取当前登录的用户
            LoginUser user = (LoginUser) oAuth2Authentication.getUserAuthentication().getPrincipal();
            // 如果用户不为空 则把id放入jwt token中
            if (user != null) {
                additionMessage.put(LoginUser.LOGIN_USER, ImmutableMap.of(
                        LoginUser.ID, user.getId(),
                        LoginUser.USER_TYPE, user.getUserType(),
                        LoginUser.PERMISSION, user.getAuthorities()));
            }
            ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(additionMessage);
            return oAuth2AccessToken;
        };
    }

    @Autowired
    private TokenEnhancer tokenEnhancer;

    /**
     * 配置AuthorizationServerTokenServices
     *
     * @return
     */
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        services.setClientDetailsService(clientDetailsService);
        services.setTokenStore(tokenStore);
        //AccessToken 2天过期
        services.setAccessTokenValiditySeconds(172800);
        //支持RefreshToken
        services.setSupportRefreshToken(true);
        //RefreshToken 7天过期
        services.setRefreshTokenValiditySeconds(604800);
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        // 把jwt增强，与额外信息增强加入到增强链
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer, jwtAccessTokenConverter));
        services.setTokenEnhancer(tokenEnhancerChain);
        return services;
    }

    /**
     * 先获取已经有的五种授权模式，然后添加自定义的进去
     *
     * @param endpoints
     * @return
     */
    public TokenGranter tokenGranter(final AuthorizationServerEndpointsConfigurer endpoints,
                                     AuthorizationServerTokenServices tokenServices) {

        List<TokenGranter> granters = new ArrayList<>(Collections.singletonList(endpoints.getTokenGranter()));
        // 验证码模式
        granters.add(new CaptchaTokenGranter(authenticationManager, tokenServices,
                endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory()));
        // 增加密码模式
        granters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices,
                endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory()));
        return new CompositeTokenGranter(granters);
    }

    /**
     * 必要配置
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        DefaultTokenServices tokenServices = tokenServices();
        endpoints
                .tokenGranter(tokenGranter(endpoints, tokenServices))
                .authenticationManager(authenticationManager)
                .tokenServices(tokenServices);
    }

    /**
     * 配置内存客户端
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //添加本地客户端.使用本地用户的账号密码进行认证鉴权
        clients.inMemory()
                .withClient("localhost")
                .secret(passwordEncoder.encode("localhost"))
                // 配置 password grant type
                .authorizedGrantTypes("password", "refresh_token", CaptchaTokenGranter.GRANT_TYPE)
                .scopes("all");
    }

    /**
     * 配置令牌端点(Token Endpoint)的安全约束
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        //设置自定义oauth异常响应
        CoreClientCredentialsTokenEndpointFilter endpointFilter = new CoreClientCredentialsTokenEndpointFilter(security);
        endpointFilter.afterPropertiesSet();
        endpointFilter.setAuthenticationEntryPoint(coreAuthenticationEntryPoint);
        security.addTokenEndpointAuthenticationFilter(endpointFilter);
        security
                .authenticationEntryPoint(coreAuthenticationEntryPoint)
                // 访问 /auth/token_key 需要认证
                .tokenKeyAccess("isAuthenticated()")
                // 访问 /auth/check_token 需要认证
                .checkTokenAccess("isAuthenticated()");
        /*允许表单认证,由于设置了自定义oauth异常响应,这里则不需再配置*/
        //.allowFormAuthenticationForClients();
    }

}
