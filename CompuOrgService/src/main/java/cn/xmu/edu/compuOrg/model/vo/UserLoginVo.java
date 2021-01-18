package cn.xmu.edu.compuOrg.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author snow create 2021/01/17 19:46
 */
@Data
public class UserLoginVo {

    @ApiModelProperty(value = "学号/工号")
    @NotNull
    private String userNo;

    @ApiModelProperty(value = "密码")
    @NotNull
    private String password;
}
