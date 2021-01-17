package cn.xmu.edu.compuOrg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author snow create 2021/01/17 23:46
 */
@Data
@ApiModel(description = "学生修改密码对象")
public class StudentModifyPasswordVo {

    @ApiModelProperty(value = "学号")
    private String studentNo;

    @ApiModelProperty(value = "验证码")
    private String verifyCode;

    @ApiModelProperty(value = "新密码")
    private String password;
}
