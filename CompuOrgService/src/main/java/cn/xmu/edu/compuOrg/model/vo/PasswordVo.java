package cn.xmu.edu.compuOrg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author snow create 2021/03/27 20:55
 */
@Data
@ApiModel(description = "用户验证密码对象")
public class PasswordVo {

    @ApiModelProperty(value = "密码")
    @NotNull(message = "密码不能为空")
    private String password;
}
