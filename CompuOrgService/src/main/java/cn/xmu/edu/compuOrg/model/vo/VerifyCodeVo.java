package cn.xmu.edu.compuOrg.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class VerifyCodeVo {

    @ApiModelProperty(value = "验证码")
    @NotNull
    private String verifyCode;
}
