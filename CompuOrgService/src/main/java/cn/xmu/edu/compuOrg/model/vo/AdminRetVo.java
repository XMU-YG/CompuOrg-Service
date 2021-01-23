package cn.xmu.edu.compuOrg.model.vo;

import cn.xmu.edu.compuOrg.model.bo.Admin;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author snow create 2021/01/23 18:56
 */
@Data
public class AdminRetVo {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "管理员号")
    private String adminNo;

    @ApiModelProperty(value = "管理员姓名")
    private String adminName;

    @ApiModelProperty(value = "性别")
    private String gender; //true for female

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "电话号码")
    private String mobile;

    public AdminRetVo(Admin admin){
        this.id = admin.getId();
        this.gender = admin.getGender();
        this.adminNo = admin.getUserNo();
        this.email = admin.getDecryptEmail();
        this.mobile = admin.getDecryptMobile();
        this.adminName = admin.getRealName();
    }
}
