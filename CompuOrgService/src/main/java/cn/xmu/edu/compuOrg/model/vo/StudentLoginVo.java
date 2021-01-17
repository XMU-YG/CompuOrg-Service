package cn.xmu.edu.compuOrg.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author snow create 2021/01/17 19:46
 */
@Data
public class StudentLoginVo {

    @ApiModelProperty(value = "学号")
    private String studentNo;

    @ApiModelProperty(value = "密码")
    private String password;
}
