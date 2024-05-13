package com.losaxa.framework.user.domian;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.losaxa.core.domain.BaseDo;
import com.losaxa.framework.user.enums.UserType;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static com.losaxa.framework.user.constant.DbField.PASSWORD;
import static com.losaxa.framework.user.constant.DbField.PERMISSION;
import static com.losaxa.framework.user.constant.DbField.ROLE;
import static com.losaxa.framework.user.constant.DbField.TYPE;
import static com.losaxa.framework.user.constant.DbField.USER_NAME;


@Data
@Document("user")
public class User extends BaseDo {

    @Field(USER_NAME)
    private String userName;

    @Field(PASSWORD)
    @JsonIgnore
    private String password;

    @Field(TYPE)
    private UserType type;

    @Field(ROLE)
    private List<String> roles;

    @Field(PERMISSION)
    private List<String> permission;

    public void encodePassword(PasswordEncoder encoder) {
        this.password = encoder.encode(this.password);
    }

}
