package cn.xmu.edu.compuOrg.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author snow create 2021/01/23 13:37
 */
@Data
public class UserBasicInfoVo {

    @ApiModelProperty(value = "学号/工号")
    private String userNo;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty(value = "电话号码")
    private String mobile;
}
