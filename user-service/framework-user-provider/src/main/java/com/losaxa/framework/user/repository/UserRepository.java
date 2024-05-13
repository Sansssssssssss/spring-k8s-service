package com.losaxa.framework.user.repository;

import com.losaxa.core.mongo.BaseMongoRepository;
import com.losaxa.framework.user.domian.User;

public interface UserRepository extends BaseMongoRepository<User, Long> {

}
