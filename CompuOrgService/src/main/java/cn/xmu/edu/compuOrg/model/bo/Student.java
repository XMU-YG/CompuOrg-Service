package cn.xmu.edu.compuOrg.model.bo;

import cn.xmu.edu.Core.model.VoObject;
import cn.xmu.edu.Core.util.AES;
import cn.xmu.edu.compuOrg.model.po.StudentPo;
import cn.xmu.edu.compuOrg.model.vo.StudentRetVo;
import cn.xmu.edu.compuOrg.model.vo.StudentVo;
import lombok.Data;

import java.io.Serializable;

@Data
public class Student implements VoObject, Serializable {
    public static String AES_PASS = "CompuOrg2021/01/17";

    private Long id;
    private String password;
    private String studentNo;
    private String studentName;
    private String email;
    private String mobile;
    private Byte gender;

    public Student(StudentPo studentPo){
        this.id = studentPo.getId();
        this.password = studentPo.getPassword();
        this.studentNo = studentPo.getStudentNo();
        this.studentName = studentPo.getStudentName();
        this.email = studentPo.getEmail();
        this.mobile = studentPo.getMobile();
        this.gender = studentPo.getGender();
    }

    public Student(StudentVo studentVo){
        this.gender = studentVo.getGender();
        this.studentNo = studentVo.getStudentNo();
        this.password = AES.encrypt(studentVo.getPassword(), AES_PASS);
        this.studentName = studentVo.getStudentName();
        if(studentVo.getEmail() != null) {
            this.email = AES.encrypt(studentVo.getEmail(), AES_PASS);
        }
        if(studentVo.getMobile() != null) {
            this.mobile = AES.encrypt(studentVo.getMobile(), AES_PASS);
        }
    }

    public StudentPo createStudentPo(){
        StudentPo studentPo = new StudentPo();
        studentPo.setId(this.id);
        studentPo.setStudentNo(this.studentNo);
        studentPo.setPassword(this.password);
        studentPo.setStudentName(this.studentName);
        studentPo.setGender(this.gender);
        studentPo.setEmail(this.email);
        studentPo.setMobile(this.mobile);
        return studentPo;
    }

    public String getDecryptEmail(){
        if(this.email != null) {
            return AES.decrypt(this.email, AES_PASS);
        }
        return null;
    }

    public String getDecryptMobile(){
        if(this.mobile != null) {
            return AES.decrypt(this.mobile, AES_PASS);
        }
        return null;
    }

    @Override
    public Object createVo() {
        return new StudentRetVo(this);
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
