package cn.xmu.edu.compuOrg.model.vo;

import cn.xmu.edu.compuOrg.model.bo.Teacher;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author snow create 2021/01/18 12:33
 */
@Data
public class TeacherRetVo {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "工号")
    private String studentNo;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "老师姓名")
    private String studentName;

    @ApiModelProperty(value = "性别")
    private String gender; //true for female

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "电话号码")
    private String mobile;

    public TeacherRetVo(Teacher teacher){
        this.id = teacher.getId();
        this.gender = teacher.getGender();
        this.studentNo = teacher.getUserNo();
        this.password = teacher.getPassword();
        this.email = teacher.getDecryptEmail();
        this.mobile = teacher.getDecryptMobile();
        this.studentName = teacher.getRealName();
    }
}