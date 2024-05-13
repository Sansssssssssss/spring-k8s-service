package com.losaxa.framework.user.domian;

import com.losaxa.core.domain.BaseDo;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

import static com.losaxa.framework.user.constant.DbField.CODE;
import static com.losaxa.framework.user.constant.DbField.NAME;
import static com.losaxa.framework.user.constant.DbField.PERMISSION;

@Data
@Document("role")
public class Role extends BaseDo {

    @Field(NAME)
    private String name;

    @Field(CODE)
    private String code;

    @Field(PERMISSION)
    private List<String> permission;

}
