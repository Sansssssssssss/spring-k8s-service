package com.losaxa.framework.user.domian;

import com.losaxa.core.autoservice.support.DoDataInit;
import com.losaxa.core.domain.BaseDo;
import com.losaxa.framework.user.enums.PermissionType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static com.losaxa.framework.user.constant.DbField.*;

@ApiModel(description = "权限菜单")
@Data
@Document("permission")
public class Permission extends BaseDo implements DoDataInit {

    @ApiModelProperty("父级id")
    @Field(PARENT_ID)
    private Long parentId;

    @ApiModelProperty("名称/菜单名称")
    @Field(NAME)
    private String name;

    @ApiModelProperty("编码")
    @Field(CODE)
    private String code;

    @ApiModelProperty("菜单类型")
    @Field(TYPE)
    private PermissionType type;

    @ApiModelProperty("后端path")
    @Field(PATH)
    private String path;

    @ApiModelProperty("前端route")
    @Field(ROUTE)
    private String route;

    @ApiModelProperty("前端图标")
    @Field(ICON)
    private String icon;

    @ApiModelProperty("排序")
    @Field(SORT)
    private Integer sort;

    @Override
    public void beforeInsertDataInit() {
        if (sort == null) {
            sort = 0;
        }
    }
}
