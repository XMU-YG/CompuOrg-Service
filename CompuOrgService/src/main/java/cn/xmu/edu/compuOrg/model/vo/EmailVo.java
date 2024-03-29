package cn.xmu.edu.compuOrg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author snow create 2021/03/27 22:16
 */
@Data
@ApiModel(description = "用户验证邮箱对象")
public class EmailVo {

    @ApiModelProperty(value = "邮箱")
    @NotBlank(message = "邮箱不能为空")
    @NotNull(message = "邮箱不能为空")
    private String email;
}
