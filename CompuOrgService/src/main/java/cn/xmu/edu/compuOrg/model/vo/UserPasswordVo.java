package cn.xmu.edu.compuOrg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author snow create 2021/01/17 22:48
 */
@Data
@ApiModel(description = "用户重置密码对象")
public class UserPasswordVo {

    @ApiModelProperty(value = "学号/工号")
    @NotNull
    private String userNo;

    @ApiModelProperty(value = "邮箱")
    @NotNull
    private String email;
}
