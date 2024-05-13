package com.losaxa.framework.auth.mongo.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Accessors(chain = true)
@Document("oauth_access_token")
public class MongoOauthAccessToken {

    @Id
    private String tokenId;
    private byte[] token;
    private String authenticationId;
    private String userName;
    private String clientId;
    private byte[] authentication;
    private String refreshToken;


}
