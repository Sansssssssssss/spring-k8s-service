package com.losaxa.framework.user.api;

import com.losaxa.core.web.Result;
import com.losaxa.framework.user.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/auth")
@FeignClient(name = "framework-user", url = "http://localhost:8920")
//@FeignClient(name = "framework-user")
public interface UserAuthRemote {

    @GetMapping("/username")
    Result<UserDto> getByUsername(@RequestParam("username") String username);

}
