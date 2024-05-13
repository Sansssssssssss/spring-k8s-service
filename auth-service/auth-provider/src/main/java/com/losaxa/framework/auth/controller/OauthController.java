package com.losaxa.framework.auth.controller;

import com.losaxa.core.web.Result;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/oauth")
public class OauthController {

    private final TokenEndpoint tokenEndpoint;

    @GetMapping("/token")
    @ApiImplicitParams({
            @ApiImplicitParam(dataTypeClass = String.class, name = "grant_type", required = true, value = "授权类型", paramType = "query"),
            @ApiImplicitParam(dataTypeClass = String.class, name = "username", required = false, value = "用户名", paramType = "query"),
            @ApiImplicitParam(dataTypeClass = String.class, name = "password", required = false, value = "密码", paramType = "query"),
            @ApiImplicitParam(dataTypeClass = String.class, name = "client_id", required = false, value = "客户端id", paramType = "query"),
            @ApiImplicitParam(dataTypeClass = String.class, name = "client_secret", required = false, value = "客户端密钥", paramType = "query"),
            @ApiImplicitParam(dataTypeClass = String.class, name = "scope", required = false, value = "使用范围", paramType = "query"),
    })
    public Result<?> getAccessToken(@ApiIgnore Principal principal,
                                    @ApiIgnore @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        return Result.ok(tokenEndpoint.getAccessToken(principal, parameters).getBody());
    }

    @PostMapping("/token")
    @ApiImplicitParams({
            @ApiImplicitParam(dataTypeClass = String.class, name = "grant_type", required = true, value = "授权类型", paramType = "query"),
            @ApiImplicitParam(dataTypeClass = String.class, name = "username", required = false, value = "用户名", paramType = "query"),
            @ApiImplicitParam(dataTypeClass = String.class, name = "password", required = false, value = "密码", paramType = "query"),
            @ApiImplicitParam(dataTypeClass = String.class, name = "client_id", required = false, value = "客户端id", paramType = "query"),
            @ApiImplicitParam(dataTypeClass = String.class, name = "client_secret", required = false, value = "客户端密钥", paramType = "query"),
            @ApiImplicitParam(dataTypeClass = String.class, name = "scope", required = false, value = "使用范围", paramType = "query"),
    })
    public Result<?> postAccessToken(Principal principal,
                                     @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        return Result.ok(tokenEndpoint.postAccessToken(principal, parameters).getBody());
    }

}
