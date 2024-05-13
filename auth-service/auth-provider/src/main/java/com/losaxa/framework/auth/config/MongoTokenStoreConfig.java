package com.losaxa.framework.auth.config;

import com.losaxa.core.mongo.BaseMongoRepository;
import com.losaxa.core.mongo.BaseMongoRepositoryFactoryBean;
import com.losaxa.framework.auth.mongo.MongoTokenStore;
import com.losaxa.framework.auth.mongo.repository.OauthAccessTokenRepository;
import com.losaxa.framework.auth.mongo.repository.OauthRefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.oauth2.provider.token.TokenStore;

@EnableMongoRepositories(basePackages = "com.losaxa.framework.auth.mongo.repository"
        //,mongoTemplateRef = "authMongoTemplate"
        , repositoryBaseClass = BaseMongoRepository.class
        , repositoryFactoryBeanClass = BaseMongoRepositoryFactoryBean.class)
@Configuration
public class MongoTokenStoreConfig {

    //@Bean("authMongoTemplate")
    //public MongoTemplate authMongoTemplate(MongoClient mongoClient,
    //                                        MappingMongoConverter converter) {
    //    MongoTemplate authMongoTemplate = new MongoTemplate(
    //            new SimpleMongoClientDatabaseFactory(mongoClient, "auth"), converter);
    //    converter.setTypeMapper(new DefaultMongoTypeMapper(null));
    //    return authMongoTemplate;
    //}

    @Autowired
    private OauthAccessTokenRepository  oauthAccessTokenRepository;
    @Autowired
    private OauthRefreshTokenRepository oauthRefreshTokenRepository;

    /**
     * 配置token存储方式
     */
    @Bean
    public TokenStore mongoTokenStore() {
        return new MongoTokenStore(oauthAccessTokenRepository, oauthRefreshTokenRepository);
    }

}
