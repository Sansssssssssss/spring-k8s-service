package com.losaxa.framework.user.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.losaxa.core.web.Result;
import com.losaxa.framework.user.domian.User;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@Api(tags = "搜索引擎接口")
@RequestMapping("/es")
@RestController
public class EsController {

    @Autowired
    ElasticsearchClient client;

    @GetMapping
    public Result<?> get(long id) throws IOException {
        SearchResponse<User> search = client.search(s -> s
                .index("user")
                .query(q -> q
                        .term(t -> t
                                .field("id")
                                .value(v -> v.longValue(id)))), User.class);
        for (Hit<User> hit : search.hits().hits()) {
            User source = hit.source();
            log.info("{}", source);
        }
        return Result.ok();
    }

    @GetMapping("/all")
    public Result<?> all() throws IOException {
        SearchResponse<User> search = client.search(s -> s
                .index("user").query(q -> q.matchAll(a -> a)), User.class);
        for (Hit<User> hit : search.hits().hits()) {
            User source = hit.source();
            log.info("{}", source);
        }
        return Result.ok();
    }

    @GetMapping("/page")
    public Result<?> page(Pageable pageable) throws IOException {
        SearchResponse<User> search = client.search(s ->
                s.index("user").from((int) pageable.getOffset()).size(pageable.getPageSize()), User.class);
        for (Hit<User> hit : search.hits().hits()) {
            User source = hit.source();
            log.info("{}", source);
        }
        return Result.ok();
    }

    @PostMapping
    public Result<?> add(@RequestBody User user) throws IOException {
        client.create(builder -> builder.index("user").id(user.getId().toString()).document(user));
        return Result.ok(user);
    }

}
