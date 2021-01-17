package cn.xmu.edu.compuOrg.model.bo;

import cn.xmu.edu.compuOrg.model.po.StudentPo;
import lombok.Data;

@Data
public class Student {
    public static String AES_PASS = "CompuOrg2021/01/17";

    private Long id;
    private String password;
    private String studentNo;
    private String studentName;

    public Student(StudentPo studentPo){
        this.id = studentPo.getId();
        this.password = studentPo.getPassword();
        this.studentNo = studentPo.getStudentName();
        this.studentName = studentPo.getStudentName();
    }
}
