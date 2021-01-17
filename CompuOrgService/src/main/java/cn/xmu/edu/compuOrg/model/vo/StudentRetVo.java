package cn.xmu.edu.compuOrg.model.vo;

import cn.xmu.edu.compuOrg.model.bo.Student;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StudentRetVo {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "学号")
    private String studentNo;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "学生姓名")
    private String studentName;

    @ApiModelProperty(value = "性别")
    private Byte gender; //true for female

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "电话号码")
    private String mobile;

    public StudentRetVo(Student student){
        this.id = student.getId();
        this.studentNo = student.getStudentNo();
        this.password = student.getPassword();
        this.studentName = student.getStudentName();
        this.gender = student.getGender();
        this.email = student.getDecryptEmail();
        this.mobile = student.getDecryptMobile();
    }
}
