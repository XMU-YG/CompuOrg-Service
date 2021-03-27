package cn.xmu.edu.compuOrg.model.vo;

import cn.xmu.edu.compuOrg.model.bo.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserRetVo implements Serializable {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "角色")
    private Byte role; //true for female

    @ApiModelProperty(value = "性别")
    private Byte gender; //true for female

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "电话号码")
    private String mobile;

    public UserRetVo(User user){
        this.id = user.getId();
        this.role = user.getRole();
        this.gender = user.getGender();
        this.userName = user.getUserName();
        this.email = user.getDecryptEmail();
        this.mobile = user.getDecryptMobile();
        this.realName = user.getRealName();
    }
}
