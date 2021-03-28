package cn.xmu.edu.compuOrg.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author snow create 2021/01/23 16:47
 */
@Data
public class UserModifyEmailVo {

    @ApiModelProperty(value = "验证码")
    @NotNull(message = "验证码不能为空")
    private String verifyCode;

    @ApiModelProperty(value = "新邮箱")
    @NotNull(message = "新邮箱不能为空")
    private String email;
}
