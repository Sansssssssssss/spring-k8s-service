package com.losaxa.framework.user;

import com.losaxa.core.autoservice.EnableAutoService;
import com.losaxa.core.mongo.BaseMongoRepository;
import com.losaxa.core.mongo.BaseMongoRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.kubernetes.fabric8.discovery.KubernetesDiscoveryClient;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableAutoService(basePackages = "com.losaxa.framework.user.service")
@EnableMongoRepositories(basePackages = "com.losaxa.framework.user.repository"
        , repositoryBaseClass = BaseMongoRepository.class
        , repositoryFactoryBeanClass = BaseMongoRepositoryFactoryBean.class)
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
