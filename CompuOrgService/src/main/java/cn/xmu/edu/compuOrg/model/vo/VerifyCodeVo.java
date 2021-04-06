package cn.xmu.edu.compuOrg.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class VerifyCodeVo {

    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String verifyCode;
}
