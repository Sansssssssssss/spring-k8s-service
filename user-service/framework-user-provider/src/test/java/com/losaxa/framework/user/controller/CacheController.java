package com.losaxa.framework.user.controller;

import com.losaxa.core.common.cache.Cache;
import com.losaxa.core.common.cache.RBucket;
import com.losaxa.core.common.lock.DistributedLock;
import com.losaxa.core.web.Result;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@Slf4j
@Api(tags = "缓存接口")
@RequestMapping("/cache")
@RestController
public class CacheController {

    @Autowired
    Cache cache;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    DistributedLock lock;

    @GetMapping("/get")
    public Result<?> get(@RequestParam String key) {
        RBucket<?> bucket = cache.getBucket(key);
        return Result.ok(bucket.get());
    }

    @GetMapping("/getstr")
    public Result<String> getStr(@RequestParam String key) {
        String value = stringRedisTemplate.opsForValue().get(key);
        return Result.ok(value);
    }

    @PostMapping("/set")
    public Result<Void> set(@RequestParam String key,
                            @RequestParam String value) {
        cache.getBucket(key).set(value);
        return Result.ok();
    }

    @PostMapping("/testlock")
    public Result<Void> testLock(@RequestParam String key) {
        lock.tryLock(key,200,60000,TimeUnit.MILLISECONDS, () -> {
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        return Result.ok();
    }


}
