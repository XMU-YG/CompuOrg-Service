package cn.xmu.edu.compuOrg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author snow create 2021/01/17 22:48
 */
@Data
@ApiModel(description = "学生重置密码对象")
public class StudentResetPasswordVo {

    @ApiModelProperty(value = "学号")
    private String studentNo;

    @ApiModelProperty(value = "邮箱")
    private String email;
}
