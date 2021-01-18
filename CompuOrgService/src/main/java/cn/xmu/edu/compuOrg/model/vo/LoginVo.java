package cn.xmu.edu.compuOrg.model.vo;

import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author snow create 2021/01/15 11:55
 */
@Data
public class LoginVo {

    @ApiModelProperty(value = "学号")
 private String studentNo;

    @ApiModelProperty(value = "密码")
    private String password;
}
