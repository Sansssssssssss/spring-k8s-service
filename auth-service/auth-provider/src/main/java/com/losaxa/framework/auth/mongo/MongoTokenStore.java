package com.losaxa.framework.auth.mongo;

import com.losaxa.framework.auth.mongo.domain.MongoOauthAccessToken;
import com.losaxa.framework.auth.mongo.domain.MongoOauthRefreshToken;
import com.losaxa.framework.auth.mongo.repository.OauthAccessTokenRepository;
import com.losaxa.framework.auth.mongo.repository.OauthRefreshTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class MongoTokenStore implements TokenStore {

    private final OauthAccessTokenRepository  accessTokenRepository;
    private final OauthRefreshTokenRepository refreshTokenRepository;

    private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

    public MongoTokenStore(OauthAccessTokenRepository oauthAccessTokenRepository, OauthRefreshTokenRepository oauthRefreshTokenRepository) {
        this.accessTokenRepository = oauthAccessTokenRepository;
        this.refreshTokenRepository = oauthRefreshTokenRepository;
    }

    /**
     * Read the authentication stored under the specified token value.
     *
     * @param token The token value under which the authentication is stored.
     * @return The authentication, or null if none.
     */
    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        return readAuthentication(token.getValue());
    }

    /**
     * Read the authentication stored under the specified token value.
     *
     * @param token The token value under which the authentication is stored.
     * @return The authentication, or null if none.
     */
    @Override
    public OAuth2Authentication readAuthentication(String token) {
        try {
            Optional<MongoOauthAccessToken> accessTokenOptional = accessTokenRepository.findById(extractTokenKey(token), true, "tokenId", "authentication");
            if (!accessTokenOptional.isPresent()) {
                if (log.isInfoEnabled()) {
                    log.info("Failed to find access token for token " + token);
                }
                return null;
            }
            return deserializeAuthentication(accessTokenOptional.get().getAuthentication());
        } catch (IllegalArgumentException e) {
            log.warn("Failed to deserialize authentication for " + token, e);
            removeAccessToken(token);
            return null;
        }
    }

    /**
     * Store an access token.
     *
     * @param token          The token to store.
     * @param authentication The authentication associated with the token.
     */
    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        String refreshToken = null;
        if (token.getRefreshToken() != null) {
            refreshToken = token.getRefreshToken().getValue();
        }
        if (readAccessToken(token.getValue()) != null) {
            removeAccessToken(token.getValue());
        }
        MongoOauthAccessToken entity = new MongoOauthAccessToken()
                .setTokenId(extractTokenKey(token.getValue()))
                .setToken(serializeAccessToken(token))
                .setAuthenticationId(authenticationKeyGenerator.extractKey(authentication))
                .setUserName(authentication.isClientOnly() ? null : authentication.getName())
                .setClientId(authentication.getOAuth2Request().getClientId())
                .setAuthentication(serializeAuthentication(authentication))
                .setRefreshToken(extractTokenKey(refreshToken));
        accessTokenRepository.insert(entity);
    }

    /**
     * Read an access token from the store.
     *
     * @param tokenValue The token value.
     * @return The access token to read.
     */
    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        try {
            Optional<MongoOauthAccessToken> accessTokenOptional = accessTokenRepository.findById(extractTokenKey(tokenValue), true, "tokenId", "token");
            if (!accessTokenOptional.isPresent()) {
                if (log.isInfoEnabled()) {
                    log.info("Failed to find access token for token " + tokenValue);
                }
                return null;
            }
            return deserializeAccessToken(accessTokenOptional.get().getToken());
        } catch (IllegalArgumentException e) {
            log.warn("Failed to deserialize access token for " + tokenValue, e);
            removeAccessToken(tokenValue);
            return null;
        }
    }

    /**
     * Remove an access token from the store.
     *
     * @param token The token to remove from the store.
     */
    @Override
    public void removeAccessToken(OAuth2AccessToken token) {
        removeAccessToken(token.getValue());
    }

    public void removeAccessToken(String tokenValue) {
        accessTokenRepository.deleteById(extractTokenKey(tokenValue));
    }

    /**
     * Store the specified refresh token in the store.
     *
     * @param refreshToken   The refresh token to store.
     * @param authentication The authentication associated with the refresh token.
     */
    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        MongoOauthRefreshToken entity = new MongoOauthRefreshToken()
                .setTokenId(extractTokenKey(refreshToken.getValue()))
                .setToken(serializeRefreshToken(refreshToken))
                .setAuthentication(serializeAuthentication(authentication));
        refreshTokenRepository.insert(entity);
    }

    /**
     * Read a refresh token from the store.
     *
     * @param tokenValue The value of the token to read.
     * @return The token.
     */
    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        try {
            Optional<MongoOauthRefreshToken> refreshTokenOptional = refreshTokenRepository.findById(extractTokenKey(tokenValue), true, "tokenId", "token");
            if (!refreshTokenOptional.isPresent()) {
                if (log.isInfoEnabled()) {
                    log.info("Failed to find refresh token for token " + tokenValue);
                }
                return null;
            }
            return deserializeRefreshToken(refreshTokenOptional.get().getToken());
        } catch (IllegalArgumentException e) {
            log.warn("Failed to deserialize refresh token for token " + tokenValue, e);
            removeRefreshToken(tokenValue);
            return null;
        }
    }

    /**
     * @param token a refresh token
     * @return the authentication originally used to grant the refresh token
     */
    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        return readAuthenticationForRefreshToken(token.getValue());
    }

    public OAuth2Authentication readAuthenticationForRefreshToken(String tokenValue) {
        OAuth2Authentication authentication = null;
        try {
            Optional<MongoOauthRefreshToken> refreshTokenOptional = refreshTokenRepository.findById(extractTokenKey(tokenValue), true, "tokenId", "authentication");
            if (!refreshTokenOptional.isPresent()) {
                if (log.isInfoEnabled()) {
                    log.info("Failed to find access token for token " + tokenValue);
                }
                return null;
            }
            return deserializeAuthentication(refreshTokenOptional.get().getAuthentication());
        } catch (IllegalArgumentException e) {
            log.warn("Failed to deserialize access token for " + tokenValue, e);
            removeRefreshToken(tokenValue);
        }

        return authentication;
    }

    /**
     * Remove a refresh token from the store.
     *
     * @param token The token to remove from the store.
     */
    @Override
    public void removeRefreshToken(OAuth2RefreshToken token) {
        removeRefreshToken(token.getValue());
    }

    public void removeRefreshToken(String token) {
        refreshTokenRepository.deleteById(extractTokenKey(token));
    }

    /**
     * Remove an access token using a refresh token. This functionality is necessary so refresh tokens can't be used to
     * create an unlimited number of access tokens.
     *
     * @param refreshToken The refresh token.
     */
    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        removeAccessTokenUsingRefreshToken(refreshToken.getValue());
    }

    public void removeAccessTokenUsingRefreshToken(String refreshToken) {
        accessTokenRepository.delete("refreshToken", refreshToken);
    }

    /**
     * Retrieve an access token stored against the provided authentication key, if it exists.
     *
     * @param authentication the authentication key for the access token
     * @return the access token or null if there was none
     */
    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        OAuth2AccessToken accessToken = null;

        String key = authenticationKeyGenerator.extractKey(authentication);
        try {
            Optional<MongoOauthAccessToken> accessTokenOptional = accessTokenRepository.findOne(Query.query(Criteria.where("authenticationId").is(key)),
                    true, "tokenId", "token");
            if (!accessTokenOptional.isPresent()) {
                if (log.isDebugEnabled()) {
                    log.debug("Failed to find access token for authentication " + authentication);
                }
            } else {
                accessToken = deserializeAccessToken(accessTokenOptional.get().getToken());
            }
        } catch (IllegalArgumentException e) {
            log.error("Could not extract access token for authentication " + authentication, e);
        }
        if (accessToken != null
                && !key.equals(authenticationKeyGenerator.extractKey(readAuthentication(accessToken.getValue())))) {
            removeAccessToken(accessToken.getValue());
            // Keep the store consistent (maybe the same user is represented by this authentication but the details have
            // changed)
            storeAccessToken(accessToken, authentication);
        }
        return accessToken;
    }

    /**
     * @param clientId the client id to search
     * @param userName the user name to search
     * @return a collection of access tokens
     */
    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        List<OAuth2AccessToken> accessTokens = new ArrayList<>();
        List<MongoOauthAccessToken> oauthAccessTokens = accessTokenRepository.find(Query.query(Criteria.where("userName").is(userName).and("clientId").is(clientId)),
                true, "tokenId", "token");
        if (oauthAccessTokens.isEmpty()) {
            if (log.isInfoEnabled()) {
                log.info("Failed to find access token for clientId " + clientId + " and userName " + userName);
            }
            return accessTokens;
        }
        accessTokens = getOauth2AccessTokens(oauthAccessTokens);
        return accessTokens;
    }

    /**
     * @param clientId the client id to search
     * @return a collection of access tokens
     */
    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        List<OAuth2AccessToken> accessTokens = new ArrayList<>();
        List<MongoOauthAccessToken> oauthAccessTokens = accessTokenRepository.find(Query.query(Criteria.where("clientId").is(clientId)),
                true, "tokenId", "token");
        if (oauthAccessTokens.isEmpty()) {
            if (log.isInfoEnabled()) {
                log.info("Failed to find access token for clientId " + clientId);
            }
            return accessTokens;
        }
        accessTokens = getOauth2AccessTokens(oauthAccessTokens);
        return accessTokens;
    }

    private List<OAuth2AccessToken> getOauth2AccessTokens(List<MongoOauthAccessToken> oauthAccessTokens) {
        List<OAuth2AccessToken> accessTokens;
        accessTokens = oauthAccessTokens.stream().map(e -> {
            try {
                return deserializeAccessToken(e.getToken());
            } catch (IllegalArgumentException ex) {
                accessTokenRepository.deleteById(e.getTokenId());
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
        return accessTokens;
    }

    public void setAuthenticationKeyGenerator(AuthenticationKeyGenerator authenticationKeyGenerator) {
        this.authenticationKeyGenerator = authenticationKeyGenerator;
    }

    protected String extractTokenKey(String value) {
        if (value == null) {
            return null;
        }
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
        }

        byte[] bytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));
        return String.format("%032x", new BigInteger(1, bytes));
    }

    protected byte[] serializeAccessToken(OAuth2AccessToken token) {
        return SerializationUtils.serialize(token);
    }

    protected OAuth2AccessToken deserializeAccessToken(byte[] token) {
        return SerializationUtils.deserialize(token);
    }

    protected byte[] serializeAuthentication(OAuth2Authentication authentication) {
        return SerializationUtils.serialize(authentication);
    }

    protected OAuth2Authentication deserializeAuthentication(byte[] authentication) {
        return SerializationUtils.deserialize(authentication);
    }

    protected byte[] serializeRefreshToken(OAuth2RefreshToken token) {
        return SerializationUtils.serialize(token);
    }

    protected OAuth2RefreshToken deserializeRefreshToken(byte[] token) {
        return SerializationUtils.deserialize(token);
    }

}
